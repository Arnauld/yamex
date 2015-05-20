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

    @Given("^the following order have been passed:$")
    public void passOrders(List<OrderRow> rows) throws Throwable {
        rows.forEach(r -> r.passOrder(orderBook));
    }

    public static class OrderRow {
        public String instrument;
        public String orderType = "limitorder";
        public String way;
        public int qty;
        public BigDecimal price;

        public void passOrder(OrderBook book) {
            Way w = parseWay(way);

            if (orderType.replaceAll("\\s+", "").equalsIgnoreCase("limitorder")) {
                book.passOrder(new LimitOrder(instrument, w, qty, price));
            } else {
                Assertions.fail("Unsupported order type: " + orderType);
            }
        }
    }

    private static Way parseWay(String way) {
        if (way == null || way.equalsIgnoreCase("buy"))
            return Way.Buy;
        else if (way.equalsIgnoreCase("sell"))
            return Way.Sell;
        Assertions.fail("Unsupported way: " + way);
        return null;
    }

    @When("^a (buy|sell) limit order is passed for (\\d+) (.+) at (.+)€$")
    public void passBuyLimitOrder(String way, int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        currentOrder = new LimitOrder(instrument, parseWay(way), qty, priceLimit);
        currentOrderId = orderBook.passOrder(currentOrder);
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
