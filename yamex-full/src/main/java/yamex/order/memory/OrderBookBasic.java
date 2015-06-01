package yamex.order.memory;

import yamex.BrokerId;
import yamex.OrderId;
import yamex.order.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBookBasic implements OrderBook {

    private final Supplier<OrderId> orderIdGen;
    private final ExecutionBus executionBus;
    private final OrderBookListener listener;
    // ---
    private List<R> marketRecords = new ArrayList<>();
    private List<LR> limitRecords = new ArrayList<>();
    private List<SR> stopRecords = new ArrayList<>();
    private BigDecimal marketPrice;

    public OrderBookBasic(Supplier<OrderId> orderIdGen,
                          ExecutionBus executionBus,
                          OrderBookListener listener) {
        this.orderIdGen = orderIdGen;
        this.executionBus = executionBus;
        this.listener = listener;
    }

    @Override
    public OrderId placeOrder(LimitOrder order) {
        affectMarketPriceIfAbsent(order.priceLimit());

        OrderId orderId = orderIdGen.get();

        LR lr = new LR(orderId, order, order.priceLimit());
        limitRecords.add(lr);
        listener.orderPlaced(orderId, order);

        resolveMarketOrders();
        resolveLimitOrder(lr);
        resolveStopOrders();
        dismissEmptyOrders();

        return orderId;
    }

    private void affectMarketPriceIfAbsent(BigDecimal marketPrice) {
        if (this.marketPrice == null)
            this.marketPrice = marketPrice;
    }

    @Override
    public OrderId placeOrder(StopOrder order) {
        OrderId orderId = orderIdGen.get();
        stopRecords.add(new SR(orderId, order, order.priceLimit()));
        listener.orderPlaced(orderId, order);
        resolveOrders();
        return orderId;
    }

    @Override
    public OrderId placeOrder(MarketOrder order) {
        OrderId orderId = orderIdGen.get();
        marketRecords.add(new R(orderId, order));
        listener.orderPlaced(orderId, order);
        resolveOrders();
        return orderId;
    }

    protected void resolveOrders() {
        resolveMarketOrders();
        resolveLimitOrders();
        resolveStopOrders();
        dismissEmptyOrders();
    }

    protected void dismissEmptyOrders() {
        Predicate<R> cleaner = r -> {
            boolean toKeep = r.remainingQuantity() > 0 || r.cancelled();
            if (!toKeep) {
                listener.orderDismissed(r.orderId(), r.order());
            }
            return toKeep;

        };

        marketRecords = marketRecords.stream().filter(cleaner).collect(toList());
        limitRecords = limitRecords.stream().filter(cleaner).collect(toList());
        stopRecords = stopRecords.stream().filter(cleaner).collect(toList());
    }

    protected void resolveMarketOrders() {
        marketRecords.forEach(this::resolveMarketOrder);
    }

    private void resolveMarketOrder(R r) {
        listener.resolvingOrder(r.orderId(), r.order(), r.remainingQuantity());

        Way otherWay = r.way().other();

        Predicate<R> otherWithRemaining = lr -> lr.way() == otherWay && lr.remainingQuantity() > 0;

        Stream<LR> records =
                limitRecords
                        .stream()
                        .filter(otherWithRemaining);

        if (marketPrice != null) {
            records = Stream.concat(
                    marketRecords.stream()
                            .filter(otherWithRemaining)
                            .map(or -> new LR(or, marketPrice)),
                    records);
        }


        records.sorted(LR.priceComparator)
                .forEach(lr -> generateExecution(lr, r));
    }

    protected void resolveLimitOrders() {
        limitRecords.forEach(this::resolveLimitOrder);
    }

    private void resolveLimitOrder(LR tlr) {
        listener.resolvingOrder(tlr.orderId(), tlr.order(), tlr.remainingQuantity());

        final int sign;
        if (tlr.way() == Way.Sell)
            sign = -1;
        else
            sign = +1;

        Way otherWay = tlr.way().other();
        limitRecords
                .stream()
                .filter(lr -> lr.way() == otherWay
                        && lr.remainingQuantity() > 0
                        && lr.priceCrosses(tlr))
                .sorted((lr1, lr2) -> sign * lr1.price().compareTo(lr2.price()))
                .forEach(lr -> generateExecution(lr, tlr));
    }

    private void generateExecution(LR l, R r) {
        if (r.remainingQuantity() == 0 || l.remainingQuantity() == 0)
            return;

        OrderId buyId;
        BrokerId buyerId;
        OrderId sellId;
        BrokerId sellerId;

        if (l.way() == Way.Buy) {
            buyId = l.orderId();
            buyerId = l.order().brokerId();
            sellId = r.orderId();
            sellerId = r.order().brokerId();
        } else {
            buyId = r.orderId();
            buyerId = r.order().brokerId();
            sellId = l.orderId();
            sellerId = l.order().brokerId();
        }

        int transferable = Math.min(r.remainingQuantity(), l.remainingQuantity());

        Optional<OrderBookListener> listenerOpt = Optional.of(listener);
        l.decreaseQuantity(transferable, listenerOpt);
        r.decreaseQuantity(transferable, listenerOpt);

        BigDecimal executionPrice = l.price();
        if (r instanceof LR) {
            executionPrice = executionPrice.max(((LR) r).price());
        }

        Execution execution = new Execution(sellId, sellerId, buyId, buyerId, transferable, executionPrice);

        marketPrice = executionPrice;
        executionBus.triggerExecution(execution);
        listener.executionTriggered(execution);
    }

    protected void resolveStopOrders() {
        stopRecords.forEach(this::resolveStopOrder);
    }

    private void resolveStopOrder(SR sr) {
        listener.resolvingOrder(sr.orderId(), sr.order(), sr.remainingQuantity());

        Way otherWay = sr.way().other();
        stopRecords
                .stream()
                .filter(lr -> lr.way() == otherWay
                        && lr.remainingQuantity() > 0
                        && lr.priceCrosses(sr))
                .forEach(this::mutateToMarketOrder);
    }

    private void mutateToMarketOrder(SR sr) {
        sr.consumeQty();
        MarketOrder marketOrder = ((StopOrder) sr.order()).asMarketOrder();

        listener.orderMutated(sr.orderId(), sr.order(), marketOrder);

        R nr = new R(sr.orderId(), marketOrder);
        marketRecords.add(nr);
        resolveMarketOrder(nr);
    }

    @Override
    public Stream<Record> records() {
        return Stream.concat(
                marketRecords.stream(),
                Stream.concat(
                        limitRecords.stream(),
                        stopRecords.stream()));
    }

    @Override
    public Optional<BigDecimal> bestBidPrice() {
        return limitRecords.stream()
                .filter(r -> r.way() == Way.Buy
                        && r.orderType() == OrderType.LimitOrder)
                .max(LR.priceComparator)
                .map(LR::price);
    }

    @Override
    public Optional<BigDecimal> bestAskPrice() {
        return limitRecords.stream()
                .filter(r -> r.way() == Way.Sell
                        && r.orderType() == OrderType.LimitOrder)
                .min(LR.priceComparator)
                .map(LR::price);
    }

    @Override
    public int availableQty(Way way, BigDecimal priceThreshold) {
        return cumulativeView()
                .entries()
                .filter(r -> r.way() == way)
                .reduce(0, (found, r) -> {
                    int c = priceThreshold.compareTo(r.price());
                    if (way == Way.Sell && c >= 0) {
                        return Math.max(found, r.cumulativeSum());
                    } else if (way == Way.Buy && c <= 0) {
                        return Math.max(found, r.cumulativeSum());
                    }
                    return found;
                }, Math::max);
    }

    @Override
    public CumulativeView cumulativeView() {
        List<CumulativeView.Entry> entries = new ArrayList<>();

        collectCumulativeRows(entries::add, Way.Buy, (e1, e2) -> -e1.getKey().compareTo(e2.getKey()));
        collectCumulativeRows(entries::add, Way.Sell, (e1, e2) -> +e1.getKey().compareTo(e2.getKey()));

        return entries::stream;
    }

    private void collectCumulativeRows(Consumer<CumulativeView.Entry> rows,
                                       Way way, Comparator<Map.Entry<BigDecimal, List<LR>>> comparator) {
        Map<BigDecimal, List<LR>> collectBuys =
                Stream.concat(
                        marketRecords
                                .stream()
                                .map(or -> new LR(or.orderId, or.order, marketPrice)),
                        limitRecords.stream())
                        .filter(r -> r.order().way() == way)
                        .collect(groupingBy(LR::price));

        collectBuys.entrySet()
                .stream()
                .sorted(comparator)
                .reduce(0, (acc, e) -> {
                    int tot = e.getValue().stream().mapToInt(R::remainingQuantity).sum();
                    int cumQty = acc + tot;
                    rows.accept(new CumulativeViewEntryBasic(way, e.getKey(), tot, cumQty));
                    return acc + tot;
                }, (acc1, acc2) -> acc1 + acc2);
    }

    public static class R implements Record {
        private final R delegate;
        private final OrderId orderId;
        private final Order order;
        private int remainingQty;

        public R(OrderId orderId, Order order) {
            this(orderId, order, order.quantity());
        }

        public R(OrderId orderId, Order order, int remainingQty) {
            this.delegate = null;
            this.orderId = orderId;
            this.order = order;
            this.remainingQty = remainingQty;
        }

        public R(R delegate) {
            this.delegate = delegate;
            this.orderId = delegate.orderId;
            this.order = delegate.order;
            this.remainingQty = delegate.remainingQuantity();
        }

        @Override
        public OrderId orderId() {
            return orderId;
        }

        public OrderType orderType() {
            return order.type();
        }

        public Way way() {
            return order.way();
        }

        @Override
        public int remainingQuantity() {
            return remainingQty;
        }

        @Override
        public Order order() {
            return order;
        }

        protected void decreaseQuantity(int transferable, Optional<OrderBookListener> listener) {
            if (delegate != null) {
                delegate.decreaseQuantity(transferable, listener);
            } else {
                remainingQty -= transferable;
                if (listener.isPresent())
                    listener.get().orderConsumed(orderId(), order(), transferable, remainingQty);
            }
        }

        public boolean cancelled() {
            return false;
        }
    }

    public static class LR extends R {

        public static Comparator<? super LR> priceComparator = (lr1, lr2) -> lr1.price().compareTo(lr2.price());

        private final BigDecimal price;

        public LR(OrderId orderId, Order order, BigDecimal price) {
            super(orderId, order);
            this.price = price;
        }

        public LR(OrderId orderId, Order order, int remainingQty, BigDecimal price) {
            super(orderId, order, remainingQty);
            this.price = price;
        }

        public LR(R or, BigDecimal price) {
            super(or);
            this.price = price;
        }

        public BigDecimal price() {
            return price;
        }

        public boolean priceCrosses(LR other) {
            return priceCrosses(other.price());
        }

        public boolean priceCrosses(BigDecimal priceThreshold) {
            if (way() == Way.Sell)
                return price().compareTo(priceThreshold) <= 0;
            else
                return price().compareTo(priceThreshold) >= 0;
        }

        @Override
        public String toString() {
            return "LR{" +
                    "price=" + price +
                    '}';
        }
    }

    public static class SR extends LR {

        public SR(OrderId orderId, Order order, BigDecimal price) {
            super(orderId, order, price);
        }

        public void consumeQty() {
            decreaseQuantity(remainingQuantity(), Optional.empty());
        }

        @Override
        public String toString() {
            return "SR{" +
                    "price=" + price() +
                    '}';
        }
    }

}
