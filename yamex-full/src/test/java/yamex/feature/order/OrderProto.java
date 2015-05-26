package yamex.feature.order;

import org.assertj.core.api.Assertions;
import yamex.BrokerId;
import yamex.feature.Context;
import yamex.feature.Defaults;
import yamex.order.*;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderProto {

    public String broker;
    public String instrument;
    public String orderType;
    public String way;
    public Integer qty;
    public BigDecimal price;
    public String timeInForce;

    public Order toOrder(Context context) {
        Objects.requireNonNull(way);

        String instr = getInstrument(context);
        Way w = OrderConverters.parseWay(way);
        BrokerId brokerId = getBroker(context);
        int quantity = getQuantity(context);
        BigDecimal price = getPrice(context);

        OrderType type = getOrderType(context);
        switch (type) {
            case LimitOrder:
                return new LimitOrder(brokerId, instr, w, quantity, price);
            case MarketOrder:
                return new MarketOrder(brokerId, instr, w, quantity);
            case StopOrder:
                return new StopOrder(brokerId, instr, w, quantity, price);
            default:
                Assertions.fail("Unsupported order type: " + orderType + " (" + type + ")");
        }
        return null;
    }

    public void placeOrder(OrderBook book, Context context) {
        Order order = toOrder(context);
        OrderBook.placeOrder(book, order);
    }

    private BrokerId getBroker(Context context) {
        if (broker == null)
            return context.getDefaults().get(BrokerId.class, Defaults.Key.ORDER_BROKER_ID);
        return new BrokerId(broker);
    }

    private String getInstrument(Context context) {
        if (instrument == null)
            return context.getDefaults().get(String.class, Defaults.Key.ORDER_INSTRUMENT);
        return instrument;
    }

    private BigDecimal getPrice(Context context) {
        if (price == null)
            return context.getDefaults().get(BigDecimal.class, Defaults.Key.ORDER_PRICE);
        return price;
    }

    private int getQuantity(Context context) {
        if (qty == null)
            return context.getDefaults().get(Integer.class, Defaults.Key.ORDER_QTY);
        return qty;
    }

    private OrderType getOrderType(Context context) {
        if (orderType == null)
            return context.getDefaults().get(OrderType.class, Defaults.Key.ORDER_TYPE);
        return OrderConverters.parseOrderType(orderType);
    }

    public static OrderProto from(OrderBook.Record r) {
        OrderProto proto = from(r.order());
        proto.qty = r.remainingQuantity();
        return proto;
    }

    public static OrderProto from(Order order) {

        OrderProto proto = new OrderProto();
        proto.orderType = OrderConverters.orderTypeToString(order.type());
        proto.broker = order.brokerId().raw();
        proto.instrument = order.instrument();
        proto.qty = order.quantity();
        proto.way = order.way().name();
        proto.timeInForce = "none";

        OrderType orderType = order.type();
        switch (orderType) {
            case LimitOrder:
                proto.price = ((LimitOrder) order).priceLimit();
                break;
            case MarketOrder:
                break;
            case StopOrder:
                proto.price = ((StopOrder) order).priceLimit();
                break;
            default:
                Assertions.fail("Unsupported order type: " + orderType);
        }
        return proto;
    }

    @Override
    public String toString() {
        return "OrderProto{" +
                "broker='" + broker + '\'' +
                ", instrument='" + instrument + '\'' +
                ", orderType='" + orderType + '\'' +
                ", way='" + way + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
