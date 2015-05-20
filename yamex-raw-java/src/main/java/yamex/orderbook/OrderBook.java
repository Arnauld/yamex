package yamex.orderbook;

import yamex.Execution;
import yamex.ExecutionBus;
import yamex.Order;
import yamex.Price;
import yamex.Quantity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static yamex.Order.Way.Buy;
import static yamex.Order.Way.Sell;
import static yamex.util.MyCollectors.maxList;
import static yamex.util.MyCollectors.minList;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBook {

    private final AtomicLong recordIdGen = new AtomicLong();
    private final ExecutionBus executionBus;
    private List<Record> records = new ArrayList<>();
    private OrderBookListener listener = OrderBookListener.NULL_LISTENER;

    public OrderBook(ExecutionBus executionBus) {
        this.executionBus = executionBus;
    }

    public void setListener(OrderBookListener listener) {
        this.listener = listener;
    }

    public void takeOrder(Order order) {
        Record r = newRecord(order);

        triggerOrderPlaced(r);
        records.add(r);
        resolveMatching(r);
    }

    private void triggerOrderPlaced(Record record) {
        listener.onOrderPlaced(record);
    }

    private void triggerOrderConsumed(List<Record> records) {
        records.stream().forEach(listener::onOrderConsumed);
    }

    private void triggerExecution(Execution execution) {
        listener.onExecution(execution);
        executionBus.triggerExecution(execution);
    }

    private void resolveMatching(Record record) {
        records()
                .filter(r -> r.way() != record.way() && r.priceCrosses(record))
                .sorted(priceThenSequenceComparator())
                .forEach(r -> record.processExecutionIfPossible(this::triggerExecution, r));

        removeEmptyRecords();
    }


    private static Comparator<Record> priceThenSequenceComparator() {
        Comparator<Record> priceComparator = Record.priceComparator();
        Comparator<Record> sequenceComparator = Record.sequenceComparator();

        return (r1, r2) -> {
            int ord = priceComparator.compare(r1, r2);
            if (ord == 0)
                return sequenceComparator.compare(r1, r2);
            else
                return -ord; // reverse order
        };
    }

    private void removeEmptyRecords() {
        Map<Boolean, List<Record>> collect = records()
                .collect(Collectors.partitioningBy(Record::hasRemaining));

        triggerOrderConsumed(collect.get(Boolean.FALSE));
        records = notNull(collect.get(Boolean.TRUE));
    }


    private List<Record> notNull(List<Record> records) {
        if (records == null)
            return new ArrayList<>();
        else
            return records;
    }

    protected Record newRecord(Order order) {
        return new Record(recordIdGen.incrementAndGet(), order);
    }

    public Stream<Record> records() {
        return records.stream();
    }

    public Optional<Record> topAsk() {
        List<Record> allMin = records()
                .filter(r -> r.way() == Sell)
                .collect(minList(Record.priceComparator()));

        return allMin
                .stream()
                .sorted(Record.sequenceComparator())
                .findFirst();
    }

    public Optional<Record> topBid() {
        List<Record> allMax = records()
                .filter(r -> r.way() == Buy)
                .collect(maxList(Record.priceComparator()));

        return allMax
                .stream()
                .sorted(Record.sequenceComparator())
                .findFirst();
    }

    public Optional<Quantity> availableForBuyAt(Price price) {
        return records()
                .filter(r -> r.way() == Buy)
                .filter(r -> r.price().lowerOrEqualsThan(price))
                .map(Record::remainingQuantity)
                .reduce(Quantity::add);
    }

    public Optional<Quantity> availableForSellAt(Price price) {
        return records()
                .filter(r -> r.way() == Sell)
                .filter(r -> r.price().lowerOrEqualsThan(price))
                .map(Record::remainingQuantity)
                .reduce(Quantity::add);
    }
}
