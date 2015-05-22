package yamex.order.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.assertj.core.api.Assertions;
import yamex.order.Context;
import yamex.order.LimitOrder;
import yamex.order.OrderBook;
import yamex.order.OrderConverters;
import yamex.order.Way;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBookSteps {

    private final Context context;

    public OrderBookSteps(Context context) {
        this.context = context;
    }

    @Given("^an empty order book$")
    public void an_empty_order_book() throws Throwable {
        context.setOrderBook(new OrderBook());
    }

    @Given("^the following orders have been placed:$")
    public void placeOrders(List<OrderRow> rows) throws Throwable {
        rows.forEach(r -> r.placeOrder(context.getOrderBook()));
    }

    @Then("^the order book should be updated with this new order$")
    public void the_order_book_should_be_updated_with_this_new_order() throws Throwable {
        OrderBook orderBook = context.getOrderBook();
        long currentOrderId = context.getCurrentOrderId();
        LimitOrder currentOrder = context.getCurrentOrder();

        assertThat(orderBook
                .records()
                .filter(r -> r.id() == currentOrderId && r.order().equals(currentOrder))
                .findFirst()).isNotEqualTo(Optional.empty());
    }

    @Then("^the order book's best bid price should be (.+)€$")
    public void the_order_book_s_best_bid_price_should_be_€(BigDecimal expectedPrice) throws Throwable {
        OrderBook orderBook = context.getOrderBook();
        assertThat(orderBook.bestBidPrice()).isEqualTo(Optional.of(expectedPrice));
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
}
