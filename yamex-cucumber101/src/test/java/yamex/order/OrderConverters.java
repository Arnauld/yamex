package yamex.order;

import org.assertj.core.api.Assertions;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderConverters {
    public static Way parseWay(String way) {
        if (way == null || way.equalsIgnoreCase("buy"))
            return Way.Buy;
        else if (way.equalsIgnoreCase("sell"))
            return Way.Sell;
        Assertions.fail("Unsupported way: " + way);
        return null;
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
            default:
                throw new IllegalArgumentException("Unrecognized order type '" + orderType + "' (" + fmt + ")");
        }
    }
}
