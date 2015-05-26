package yamex.feature.order;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import yamex.OrderId;
import yamex.feature.Context;
import yamex.order.CumulativeView;
import yamex.order.Order;
import yamex.order.OrderBook;
import yamex.order.Way;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static yamex.order.Way.Buy;
import static yamex.order.Way.Sell;

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
        context.resetOrderBook();
    }

    @Then("^the order book should be updated with this new order$")
    public void the_order_book_should_be_updated_with_this_new_order() throws Throwable {
        OrderBook orderBook = context.getOrderBook();
        OrderId currentOrderId = context.getCurrentOrderId();
        Order currentOrder = context.getCurrentOrder(Order.class);

        assertThat(orderBook
                .records()
                .filter(r -> r.orderId() == currentOrderId && r.order().equals(currentOrder))
                .findFirst()).isNotEqualTo(Optional.empty());
    }

    @Then("^the order book's best bid price should be (.+)€$")
    public void the_order_book_s_best_bid_price_should_be_€(BigDecimal expectedPrice) throws Throwable {
        OrderBook orderBook = context.getOrderBook();
        assertThat(orderBook.bestBidPrice()).isEqualTo(Optional.of(expectedPrice));
    }

    @Then("^the order book's best ask price should be (.+)€$")
    public void the_order_book_s_best_ask_price_should_be_€(BigDecimal expectedPrice) throws Throwable {
        OrderBook orderBook = context.getOrderBook();
        assertThat(orderBook.bestAskPrice()).isEqualTo(Optional.of(expectedPrice));
    }

    @Then("^the order book should be composed of the following orders:$")
    public void the_order_book_should_be_composed_of_the_following_orders(DataTable expectedTable) throws Throwable {
        List<OrderProto> actuals = context
                .getOrderBook()
                .records()
                .map(OrderProto::from)
                .collect(toList());

        //DataTable actualTable = DataTable.create(actuals);
        //List<OrderProto> expected = expectedTable.asList(OrderProto.class);

        expectedTable.unorderedDiff(actuals);
    }

    @Then("^the following execution[s]? should have been triggered:$")
    public void the_following_execution_should_have_been_triggered(DataTable expectedTable) throws Throwable {
        List<ExecutionProto> actuals = context
                .getExecutions()
                .stream()
                .map(ExecutionProto::from)
                .collect(toList());

        expectedTable.unorderedDiff(actuals);
    }


    @Then("^the order book's available quantities for sell under (.+)€ should be (\\d+)$")
    public void the_order_book_s_available_quantities_for_sell_over_€_should_be(BigDecimal price, int expectedQty) throws Throwable {
        Way w = Sell;

        OrderBook orderBook = context.getOrderBook();
        assertThat(orderBook.availableQty(w, price)).isEqualTo(expectedQty);
    }

    @Then("^the order book's available quantities for buy over (.+)€ should be (\\d+)$")
    public void the_order_book_s_available_quantities_for_buy_under_€_should_be(BigDecimal price, int expectedQty) throws Throwable {
        Way w = Buy;

        OrderBook orderBook = context.getOrderBook();
        assertThat(orderBook.availableQty(w, price)).isEqualTo(expectedQty);
    }

    @Then("^the order book's view should look like:$")
    public void the_order_book_s_view_should_look_like(DataTable expectedView) throws Throwable {
        OrderBook orderBook = context.getOrderBook();

        Map<Way, List<CumulativeView.Entry>> entries =
                orderBook.cumulativeView()
                        .entries()
                        .collect(groupingBy(CumulativeView.Entry::way));
        Iterator<CumulativeView.Entry> bidEntries = sortEntries(entries.get(Buy), CumulativeView.Entry.PriceComparatorDsc);
        Iterator<CumulativeView.Entry> askEntries = sortEntries(entries.get(Sell), CumulativeView.Entry.PriceComparatorAsc);

        // zip bid and ask entries
        List<CumulativeViewRowProto> actuals = new ArrayList<>();
        while (bidEntries.hasNext() || askEntries.hasNext()) {
            CumulativeView.Entry bidEntry = bidEntries.hasNext() ? bidEntries.next() : null;
            CumulativeView.Entry askEntry = askEntries.hasNext() ? askEntries.next() : null;
            actuals.add(CumulativeViewRowProto.from(bidEntry, askEntry));
        }

        expectedView.unorderedDiff(actuals);
    }

    private static Iterator<CumulativeView.Entry> sortEntries(List<CumulativeView.Entry> entries,
                                                              Comparator<CumulativeView.Entry> comparator) {
        return entries.stream().sorted(comparator).iterator();
    }

}
