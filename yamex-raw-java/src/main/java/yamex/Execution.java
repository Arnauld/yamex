package yamex;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Execution {
    private final OrderId sellOrder;
    private final OrderId buyOrder;
    private final Quantity qty;

    public Execution(OrderId sellOrder, OrderId buyOrder, Quantity qty) {
        this.sellOrder = sellOrder;
        this.buyOrder = buyOrder;
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Execution execution = (Execution) o;
        return buyOrder.equals(execution.buyOrder)
                && qty.equals(execution.qty)
                && sellOrder.equals(execution.sellOrder);
    }

    @Override
    public int hashCode() {
        int result = sellOrder.hashCode();
        result = 31 * result + buyOrder.hashCode();
        result = 31 * result + qty.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Execution{" + qty + " " + sellOrder + "->" + buyOrder + '}';
    }
}
