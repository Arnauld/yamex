package yamex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBook {

    private final AtomicLong recordIdGen = new AtomicLong();
    private List<Record> records = new ArrayList<>();

    public void takeOrder(Order order) {
        records.add(newRecord(order));
    }

    protected Record newRecord(Order order) {
        return new Record(recordIdGen.incrementAndGet(), order);
    }

    public Stream<Record> records() {
        return records.stream();
    }
}
