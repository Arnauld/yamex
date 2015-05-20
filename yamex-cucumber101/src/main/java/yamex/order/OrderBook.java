package yamex.order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBook {

    private final AtomicLong idGen = new AtomicLong();
    private final List<Record> records = new ArrayList<>();

    public long passOrder(LimitOrder limitOrder) {
        long nextId = idGen.incrementAndGet();
        records.add(new Record(nextId, limitOrder));
        return nextId;
    }

    public Stream<Record> records() {
        return records.stream();
    }

    public static class Record {
        private final long id;
        private final LimitOrder order;

        public Record(long id, LimitOrder order) {
            this.id = id;
            this.order = order;
        }

        public long id() {
            return id;
        }

        public LimitOrder order() {
            return order;
        }
    }
}
