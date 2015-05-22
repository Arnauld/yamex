package yamex.order;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Context {
    private OrderBook orderBook;
    private LimitOrder currentOrder;
    private long currentOrderId;

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setCurrentOrder(long orderId, LimitOrder currentOrder) {
        this.currentOrderId = orderId;
        this.currentOrder = currentOrder;
    }

    public long getCurrentOrderId() {
        return currentOrderId;
    }

    public LimitOrder getCurrentOrder() {
        return currentOrder;
    }
}
