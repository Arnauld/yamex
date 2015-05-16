package yamex;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Order {

    public enum Way {
        Buy,
        Sell
    }

    private final OrderId orderId;
    private final BrokerId brokerId;
    private final Way way;
    private final Quantity qty;
    private final Price price;

    public Order(OrderId orderId, BrokerId brokerId, Way way, Quantity qty, Price price) {
        this.orderId = orderId;
        this.brokerId = brokerId;
        this.way = way;
        this.qty = qty;
        this.price = price;
    }

    public OrderId orderId() {
        return orderId;
    }

    public BrokerId brokerId() {
        return brokerId;
    }

    public Way way() {
        return way;
    }

    public Quantity quantity() {
        return qty;
    }

    public Price price() {
        return price;
    }
}
