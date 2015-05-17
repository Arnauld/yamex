package yamex;

import java.util.Comparator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Record {
    private final long sequence;
    private final Order order;

    public Record(long sequence, Order order) {
        this.sequence = sequence;
        this.order = order;
    }

    public long sequence() {
        return sequence;
    }

    public Price price() {
        return order.price();
    }

    public Quantity quantity() {
        return order.quantity();
    }

    public Order.Way way() {
        return order.way();
    }

    public static Comparator<Record> priceComparator() {
        return (r1, r2) -> r1.price().compareTo(r2.price());
    }

    public static Comparator<Record> sequenceComparator() {
        return (r1, r2) -> Long.compare(r1.sequence(), r2.sequence());
    }
}
