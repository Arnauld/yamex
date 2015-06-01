package yamex.feature.order;

import yamex.order.Order;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Stats {

    private int nbLimitOrders = 0;
    private int nbMarketOrders = 0;
    private int nbStopOrders = 0;
    private int nbOtherOrders = 0;
    //
    private int buyQtyNb = 0;
    private int sellQtyNb = 0;

    public void updateStats(Order order) {
        switch (order.way()) {
            case Sell:
                sellQtyNb += order.quantity();
                break;
            case Buy:
                buyQtyNb += order.quantity();
                break;
        }

        switch (order.type()) {
            case LimitOrder:
                nbLimitOrders++;
                break;
            case MarketOrder:
                nbMarketOrders++;
                break;
            case StopOrder:
                nbStopOrders++;
                break;
            default:
                nbOtherOrders++;
        }
    }

    public int buyQuantityTotal() {
        return this.buyQtyNb;
    }
    public int sellQuantityTotal() {
        return this.sellQtyNb;
    }
}
