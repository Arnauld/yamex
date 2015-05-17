package yamex;

import java.util.concurrent.atomic.AtomicInteger;

import static yamex.BrokerId.brokerId;
import static yamex.Order.Way.Buy;
import static yamex.Order.Way.Sell;
import static yamex.OrderId.orderId;
import static yamex.Price.price;
import static yamex.Quantity.quantity;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderSamples {

    private final AtomicInteger orderIdGen = new AtomicInteger();
    private final AtomicInteger brokerIdGen = new AtomicInteger();

    private String orderId;
    private String brokerId;

    public OrderSamples usingOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    protected String nextOrderId() {
        if (orderId == null)
            return "O" + orderIdGen.incrementAndGet();
        return orderId;
    }

    public OrderSamples usingBrokerId(String brokerId) {
        this.brokerId = brokerId;
        return this;
    }

    protected String nextBrokerId() {
        if (brokerId == null)
            return "B" + brokerIdGen.incrementAndGet();
        return brokerId;
    }

    public Order buy(int qty, double price) {
        return order(Buy, qty, price);
    }

    public Order sell(int qty, double price) {
        return order(Sell, qty, price);
    }

    public Order order(Order.Way way, int qty, double price) {
        return new Order(
                orderId(nextOrderId()),
                brokerId(nextBrokerId()),
                way,
                quantity(qty),
                price(price));
    }
}
