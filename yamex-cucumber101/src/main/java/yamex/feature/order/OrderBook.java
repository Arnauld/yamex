package yamex.feature.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBook {

    private final AtomicLong idGen = new AtomicLong();
    private final List<Record> records = new ArrayList<>();

    public long placeOrder(LimitOrder limitOrder) {
        long nextId = idGen.incrementAndGet();
        records.add(new Record(nextId, limitOrder));
        return nextId;
    }

    public Stream<Record> records() {
        return records.stream();
    }

    public Optional<BigDecimal> bestBidPrice() {
        return records()
                .filter(r -> r.way() == Way.Buy)
                .max((r1, r2) -> r1.price().compareTo(r2.price()))
                .map(Record::price);
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

        public Way way() {
            return order.way();
        }

        public BigDecimal price() {
            return order.getPriceLimit();
        }
    }
}
