package yamex.order;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderSteps {

    private OrderBook orderBook;
    private LimitOrder currentOrder;
    private long currentOrderId;

    @Given("^an empty order book$")
    public void an_empty_order_book() throws Throwable {
        orderBook = new OrderBook();
    }

    @Given("^the following orders have been placed:$")
    public void placeOrders(List<OrderRow> rows) throws Throwable {
        rows.forEach(r -> r.placeOrder(orderBook));
    }

    public static class OrderRow {
        public String instrument;
        public String orderType = "limit-order";
        public String way;
        public int qty;
        public BigDecimal price;

        public void placeOrder(OrderBook book) {
            Way w = OrderConverters.parseWay(way);

            switch (OrderConverters.parseOrderType(orderType)) {
                case LimitOrder:
                    book.placeOrder(new LimitOrder(instrument, w, qty, price));
                    break;
                default:
                    Assertions.fail("Unsupported order type: " + orderType);
            }
        }
    }

    @When("^a (buy|sell) limit order is placed for (\\d+) (.+) at (.+)€$")
    public void passBuyLimitOrder(String way, int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        currentOrder = new LimitOrder(instrument, OrderConverters.parseWay(way), qty, priceLimit);
        currentOrderId = orderBook.placeOrder(currentOrder);
    }

    @Then("^the order book should be updated with this new order$")
    public void the_order_book_should_be_updated_with_this_new_order() throws Throwable {
        assertThat(orderBook
                .records()
                .filter(r -> r.id() == currentOrderId && r.order().equals(currentOrder))
                .findFirst()).isNotEqualTo(Optional.empty());
    }

    @Then("^the order book's best bid price should be (.+)€$")
    public void the_order_book_s_best_bid_price_should_be_€(BigDecimal expectedPrice) throws Throwable {
        assertThat(orderBook.bestBidPrice()).isEqualTo(Optional.of(expectedPrice));
    }

}
