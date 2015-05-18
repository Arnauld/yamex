package yamex.orderbook;

import yamex.ExecutionBus;
import yamex.Order;
import yamex.Price;
import yamex.Quantity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
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

    public OrderBook(ExecutionBus executionBus) {
        this.executionBus = executionBus;
    }

    public void takeOrder(Order order) {
        Record r = newRecord(order);
        records.add(r);
        resolveMatching(r);
    }


    private void resolveMatching(Record record) {

        Comparator<Record> priceComparator = Record.priceComparator();//
        Comparator<Record> sequenceComparator = Record.sequenceComparator();

        Comparator<Record> comparator = (r1, r2) -> {
            int ord = priceComparator.compare(r1, r2);
            if (ord == 0)
                return sequenceComparator.compare(r1, r2);
            else
                return -ord; // reverse order
        };

        records()
                .filter(r -> r.way() != record.way() && r.priceCrosses(record))
                .sorted(comparator)
                .forEach(r -> record.processExecutionIfPossible(executionBus, r));

        records = records()
                .filter(Record::hasRemaining)
                .collect(toList());
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
