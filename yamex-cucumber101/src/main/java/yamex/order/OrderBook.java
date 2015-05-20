package yamex.order;

import java.util.stream.Stream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBook {
    public long passOrder(LimitOrder limitOrder) {
        return 1L;
    }

    public Stream<Record> records() {
        return Stream.empty();
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
