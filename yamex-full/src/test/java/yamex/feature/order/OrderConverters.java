package yamex.feature.order;

import yamex.order.OrderType;
import yamex.order.Way;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderConverters {

    public static Way parseWay(String way) {
        if (way == null || way.equalsIgnoreCase("buy"))
            return Way.Buy;
        else if (way.equalsIgnoreCase("sell"))
            return Way.Sell;

        throw new IllegalArgumentException("Unrecognized way '" + way + "'");
    }

    public static OrderType parseOrderType(String orderType) {
        String fmt = orderType
                .replaceAll("(\\S)\\s+(\\S)", "$1-$2")
                .replaceAll("\\s+", "")
                .toLowerCase();

        switch (fmt) {
            case "limit-order":
            case "lo":
                return OrderType.LimitOrder;
            case "market-order":
            case "mo":
                return OrderType.MarketOrder;
            case "stop-order":
            case "so":
                return OrderType.StopOrder;
            default:
                throw new IllegalArgumentException("Unrecognized order type '" + orderType + "' (" + fmt + ")");
        }
    }

    public static String orderTypeToString(OrderType type) {
        switch (type) {
            case LimitOrder:
                return "Limit Order";
            case MarketOrder:
                return "Market Order";
            case StopOrder:
                return "Stop Order";
            default:
                throw new IllegalArgumentException("Unrecognized order type '" + type + "'");
        }
    }
}
