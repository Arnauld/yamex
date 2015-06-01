package yamex.feature.order;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import yamex.BrokerId;
import yamex.feature.Context;
import yamex.order.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class InvariantSteps {

    public static final String PLACED_STATS = "placed-stats";
    private final Context context;

    public InvariantSteps(Context context) {
        this.context = context;
    }

    protected yamex.order.OrderBook getOrderBook() {
        return context.getOrderBook();
    }

    @When("^(\\d+) of \\((.+)\\) orders are placed to \\((.+)\\) (.+) qty at (.+)€$")
    public void placeAnyOrders(int nbOrders, String orderTypes, String ways, String qty, String price) throws Throwable {
        Random r = context.getRandom();
        AtomicInteger brokerIdGen = new AtomicInteger();

        Supplier<BrokerId> brokerIdSupplier = () -> new BrokerId("B" + brokerIdGen.incrementAndGet());
        Supplier<OrderType> typeSupplier = randomTypeSupplier(orderTypes, r);
        Supplier<Way> waySupplier = randomWaySupplier(ways, r);
        Supplier<Integer> qtySupplier = randomQtySupplier(qty, r);
        Supplier<BigDecimal> priceSupplier = randomPriceSupplier(price, r);
        Supplier<String> instrSupplier = () -> "FFLY";
        Supplier<Order> orderSupplier = randomOrderSupplier(brokerIdSupplier,
                typeSupplier,
                waySupplier,
                qtySupplier,
                priceSupplier,
                instrSupplier);

        Stats placedStats = new Stats();
        OrderBook book = context.getOrderBook();

        for (int i = 0; i < nbOrders; i++) {
            Order order = orderSupplier.get();
            placedStats.updateStats(order);
            OrderBook.placeOrder(book, order);
        }

        context.put(PLACED_STATS, placedStats);
    }

    private Supplier<Order> randomOrderSupplier(Supplier<BrokerId> brokerIdSupplier,
                                                Supplier<OrderType> typeSupplier,
                                                Supplier<Way> waySupplier,
                                                Supplier<Integer> qtySupplier,
                                                Supplier<BigDecimal> priceSupplier,
                                                Supplier<String> instrSupplier) {
        return () -> {
            BrokerId brokerId = brokerIdSupplier.get();
            OrderType orderType = typeSupplier.get();
            switch (orderType) {
                case LimitOrder:
                    return new LimitOrder(brokerId, instrSupplier.get(), waySupplier.get(), qtySupplier.get(), priceSupplier.get());
                case MarketOrder:
                    return new MarketOrder(brokerId, instrSupplier.get(), waySupplier.get(), qtySupplier.get());
                case StopOrder:
                    return new StopOrder(brokerId, instrSupplier.get(), waySupplier.get(), qtySupplier.get(), priceSupplier.get());
            }
            throw new IllegalArgumentException("Unsupported order type: " + orderType);
        };
    }

    private Supplier<BigDecimal> randomPriceSupplier(String price, Random r) {
        Supplier<BigDecimal> priceSupplier;
        if ("<any>".equalsIgnoreCase(price)) {
            priceSupplier = () -> new BigDecimal(BigInteger.valueOf(r.nextInt(1000_000)), 4);
        } else {
            priceSupplier = () -> new BigDecimal(price);
        }
        return priceSupplier;
    }

    private Supplier<Integer> randomQtySupplier(String qty, Random r) {
        Supplier<Integer> qtySupplier;
        if ("<any>".equalsIgnoreCase(qty)) {
            qtySupplier = () -> r.nextInt(100);
        } else {
            int fixed = Integer.parseInt(qty);
            qtySupplier = () -> fixed;
        }
        return qtySupplier;
    }

    private Supplier<Way> randomWaySupplier(String ways, Random r) {
        List<Way> wayList = Stream.of(ways.split("[|/]")).map(s -> {
            switch (s.trim().toLowerCase()) {
                case "buy":
                    return Way.Buy;
                case "sell":
                    return Way.Sell;
            }
            throw new IllegalArgumentException("Unrecognized way: " + s);
        }).collect(toList());
        return () -> wayList.get(r.nextInt(wayList.size()));
    }

    private Supplier<OrderType> randomTypeSupplier(String orderTypes, Random r) {
        List<OrderType> orderTypeList = Stream.of(orderTypes.split("[|/]")).map(s -> {
            switch (s.trim().toLowerCase()) {
                case "limit":
                    return OrderType.LimitOrder;
                case "market":
                    return OrderType.MarketOrder;
                case "stop":
                    return OrderType.StopOrder;
            }
            throw new IllegalArgumentException("Unrecognized type: " + s);
        }).collect(toList());
        return () -> orderTypeList.get(r.nextInt(orderTypeList.size()));
    }


    @Then("^no sell market order should remain if there are any buy limit orders remaining$")
    public void no_sell_market_order_should_remain_if_there_are_any_buy_limit_orders_remaining() throws Throwable {
        long nbSellMO = getOrderBook().records()
                .filter(r -> r.order().way() == Way.Sell && r.order().type() == OrderType.MarketOrder)
                .count();
        if (nbSellMO > 0) {
            assertThat(getOrderBook()
                    .records()
                    .filter(r -> r.order().way() == Way.Buy)
                    .collect(toList())).isEmpty();
        }
    }


    @Then("^no buy market order should remain if there are any sell limit orders remaining$")
    public void no_buy_market_order_should_remain_if_there_are_any_sell_limit_orders_remaining() throws Throwable {
        long nbBuyMO = getOrderBook().records()
                .filter(r -> r.order().way() == Way.Buy && r.order().type() == OrderType.MarketOrder)
                .count();
        if (nbBuyMO > 0) {
            assertThat(getOrderBook()
                    .records()
                    .filter(r -> r.order().way() == Way.Sell)
                    .collect(toList())).isEmpty();
        }
    }

    //@Then("^the sum of quantities still in order book and executions should be identical to the sum of quantities in placed orders$")
    @Then("^\\(∑ quantities in order book\\) \\+ \\(∑ quantities in executions\\) should be identical to \\(∑ quantities in placed orders\\)$")
    public void the_sum_of_quantities_still_in_order_book_and_executions_should_be_identical_to_the_sum_of_quantities_in_placed_orders() throws Throwable {
        Stats stats = context.get(Stats.class, PLACED_STATS);
        int remainingSellQty = getOrderBook().records()
                .filter(r -> r.order().way() == Way.Sell)
                .mapToInt(OrderBook.Record::remainingQuantity)
                .sum();
        int remainingBuyQty = getOrderBook().records()
                .filter(r -> r.order().way() == Way.Buy)
                .mapToInt(OrderBook.Record::remainingQuantity)
                .sum();
        int executionQuantity = context.getExecutions().stream().mapToInt(Execution::quantity).sum();

        assertThat(remainingBuyQty + executionQuantity).isEqualTo(stats.buyQuantityTotal());
        assertThat(remainingSellQty + executionQuantity).isEqualTo(stats.sellQuantityTotal());
    }
}
