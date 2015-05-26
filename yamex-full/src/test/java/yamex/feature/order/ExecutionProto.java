package yamex.feature.order;

import yamex.order.Execution;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ExecutionProto {
    public String sellerBroker;
    public String buyerBroker;
    public int qty;
    public BigDecimal price;

    public static ExecutionProto from(Execution r) {
        ExecutionProto proto = new ExecutionProto();
        proto.buyerBroker = r.buyerId().raw();
        proto.sellerBroker = r.sellerId().raw();
        proto.qty = r.quantity();
        proto.price = r.price();
        return proto;
    }
}
