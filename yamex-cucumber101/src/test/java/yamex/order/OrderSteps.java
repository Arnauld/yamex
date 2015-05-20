package yamex.order;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;
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

    @When("^a buy limit order is passed for (\\d+) (.+) at (.+)€$")
    public void passBuyLimitOrder(int qty, String instrument, BigDecimal priceLimit) throws Throwable {
        currentOrder = new LimitOrder(instrument, qty, priceLimit);
        currentOrderId = orderBook.passOrder(currentOrder);
    }

    @Then("^the order book should be updated with this new order$")
    public void the_order_book_should_be_updated_with_this_new_order() throws Throwable {
        assertThat(orderBook
                .records()
                .filter(r -> r.id() == currentOrderId && r.order().equals(currentOrder))
                .findFirst()).isNotEqualTo(Optional.empty());
    }

}
