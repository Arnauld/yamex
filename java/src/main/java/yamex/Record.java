package yamex;

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
}
