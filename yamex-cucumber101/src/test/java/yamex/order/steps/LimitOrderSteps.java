package yamex.order.steps;

import cucumber.api.java.en.When;
import yamex.order.Context;
import yamex.order.LimitOrder;
import yamex.order.OrderBook;
import yamex.order.OrderConverters;

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
