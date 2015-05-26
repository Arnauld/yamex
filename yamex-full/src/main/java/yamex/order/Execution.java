package yamex.order;

import yamex.BrokerId;
import yamex.OrderId;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Execution {
    private final OrderId sellId;
    private final BrokerId sellerId;
    private final OrderId buyId;
    private final BrokerId buyerId;
    private final int quantity;
    private final BigDecimal price;

    public Execution(OrderId sellId,
                     BrokerId sellerId,
                     OrderId buyId,
                     BrokerId buyerId,
                     int quantity,
                     BigDecimal executionPrice) {
        this.sellId = sellId;
        this.sellerId = sellerId;
        this.buyId = buyId;
        this.buyerId = buyerId;
        this.quantity = quantity;
        this.price = executionPrice;
    }

    public OrderId sellId() {
        return sellId;
    }

    public BrokerId sellerId() {
        return sellerId;
    }

    public OrderId buyId() {
        return buyId;
    }

    public BrokerId buyerId() {
        return buyerId;
    }

    public BigDecimal price() {
        return price;
    }

    public int quantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Execution{" + sellerId + "->" + buyerId + " " + quantity + "@" + price + '}';
    }
}
