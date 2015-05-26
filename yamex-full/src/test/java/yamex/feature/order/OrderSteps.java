package yamex.feature.order;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import yamex.BrokerId;
import yamex.OrderId;
import yamex.feature.Context;
import yamex.order.LimitOrder;
import yamex.order.MarketOrder;
import yamex.order.Order;
import yamex.order.StopOrder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderSteps {

    private final Context context;

    public OrderSteps(Context context) {
        this.context = context;
    }

    protected yamex.order.OrderBook getOrderBook() {
        return context.getOrderBook();
    }

    @Given("^the following orders have been placed(?: in order)?:$")
    public void placeOrders(List<OrderProto> rows) throws Throwable {
        rows.forEach(r -> r.placeOrder(context.getOrderBook(), context));
    }

    @Given("^a default limit order to sell$")
    public void a_default_limit_order_to_sell() throws Throwable {
        OrderProto proto = new OrderProto();
        proto.way = "sell";
        context.setCurrentOrder(null, proto.toOrder(context));
    }

    @When("^a limit order is placed to (buy|sell) (\\d+) (\\S+) at (.+)€$")
    public void placeLimitOrder(String way, int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        placeLimitOrder(way, qty, instrument, priceLimit, context.currentBrokerId());
    }

    @When("^a limit order is placed to (buy|sell) (\\d+) (\\S+) at (.+)€ by \"([^\"]+)\"$")
    public void placeLimitOrder(String way, int qty, String instrument, BigDecimal priceLimit, String brokerId) throws Throwable {
        placeLimitOrder(way, qty, instrument, priceLimit, new BrokerId(brokerId));
    }

    public void placeLimitOrder(String way, int qty, String instrument, BigDecimal priceLimit, BrokerId brokerId) {
        LimitOrder order = new LimitOrder(brokerId, instrument, OrderConverters.parseWay(way), qty, priceLimit);
        OrderId id = getOrderBook().placeOrder(order);
        context.setCurrentOrder(id, order);
    }

    @When("^a market order is placed to (buy|sell) (\\d+) (\\S+)$")
    public void placeMarketOrder(String way, int qty, String instrument) throws Throwable {
        MarketOrder order = new MarketOrder(context.currentBrokerId(), instrument, OrderConverters.parseWay(way), qty);
        OrderId id = getOrderBook().placeOrder(order);
        context.setCurrentOrder(id, order);
    }

    @Given("^a stop order has been placed to (buy|sell) (\\d+) (\\S+) at (.+)€$")
    public void stopOrderPlaced(String way, int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        placeStopOrder(way, qty, instrument, priceLimit, context.currentBrokerId());
    }

    @Given("^a stop order has been placed to (buy|sell) (\\d+) (\\S+) at (.+)€ by \"([^\"]+)\"$")
    public void stopOrderPlaced(String way, int qty, String instrument, BigDecimal priceLimit, String brokerId) throws Throwable {
        placeStopOrder(way, qty, instrument, priceLimit, new BrokerId(brokerId));
    }

    @When("^a stop order is placed to (buy|sell) (\\d+) (\\S+) at (.+)€$")
    public void placeStopOrder(String way, int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        placeStopOrder(way, qty, instrument, priceLimit, context.currentBrokerId());
    }

    @When("^a stop order is placed to (buy|sell) (\\d+) (\\S+) at (.+)€ by \"([^\"]+)\"$")
    public void placeStopOrder(String way, int qty, String instrument, BigDecimal priceLimit, String brokerId) throws Throwable {
        placeStopOrder(way, qty, instrument, priceLimit, new BrokerId(brokerId));
    }

    public void placeStopOrder(String way, int qty, String instrument, BigDecimal priceLimit, BrokerId brokerId) {
        StopOrder order = new StopOrder(brokerId, instrument, OrderConverters.parseWay(way), qty, priceLimit);
        OrderId id = getOrderBook().placeOrder(order);
        context.setCurrentOrder(id, order);
    }


    @Then("^the order should have the following properties:$")
    public void the_order_should_have_the_following_properties(DataTable expectedTable) throws Throwable {
        Order currentOrder = context.getCurrentOrder();
        OrderProto orderProto = OrderProto.from(currentOrder);

        System.err.println("OrderSteps.the_order_should_have_the_following_properties::" + orderProto);

        List<OrderProto> actuals = Arrays.asList(orderProto);

        //DataTable actualTable = DataTable.create(actuals);
        //List<OrderProto> expected = expectedTable.asList(OrderProto.class);

        expectedTable.unorderedDiff(actuals);
    }
}
