package yamex;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderId {
    private final String id;

    public static OrderId orderId(String id) {
        return new OrderId(id);
    }

    private OrderId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OrderId orderId = (OrderId) o;
        return id.equals(orderId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "OrderId{" + id + '}';
    }
}
