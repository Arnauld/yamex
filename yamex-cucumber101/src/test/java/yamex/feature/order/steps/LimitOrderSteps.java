package yamex.feature.order.steps;

import cucumber.api.java.en.When;
import yamex.feature.order.Context;
import yamex.feature.order.LimitOrder;
import yamex.feature.order.OrderBook;
import yamex.feature.order.OrderConverters;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LimitOrderSteps {

    private final Context context;

    public LimitOrderSteps(Context context) {
        this.context = context;
    }

    @When("^a (buy|sell) limit order is placed for (\\d+) (.+) at (.+)€$")
    public void passBuyLimitOrder(String way, int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        OrderBook orderBook = context.getOrderBook();

        LimitOrder order = new LimitOrder(instrument, OrderConverters.parseWay(way), qty, priceLimit);
        long orderId = orderBook.placeOrder(order);
        context.setCurrentOrder(orderId, order);
    }

}
