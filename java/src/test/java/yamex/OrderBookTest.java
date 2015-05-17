package yamex;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static yamex.Order.Way.Buy;
import static yamex.Order.Way.Sell;
import static yamex.Price.price;
import static yamex.Quantity.quantity;
import static yamex.T.t;

public class OrderBookTest {

    private OrderSamples orderSamples = new OrderSamples();

    @Test
    public void orderBook_should_add_records_for_each_orders() {
        OrderBook book = new OrderBook();
        book.takeOrder(orderSamples.buy(10, 11.4));
        book.takeOrder(orderSamples.sell(13, 12.4));
        book.takeOrder(orderSamples.buy(11, 11.9));

        assertThat(book.records().count()).isEqualTo(3);

        List<T> buys = extract(book, Buy);
        assertThat(buys).containsExactly(
                t(quantity(10), price(11.4)),
                t(quantity(11), price(11.9)));

        List<T> sells = extract(book, Sell);
        assertThat(sells).containsExactly(
                t(quantity(13), price(12.4)));
    }

    @Test
    public void orderBook_should_return_the_record_with_lowest_price_as_top_ask() {
        OrderBook book = new OrderBook();
        book.takeOrder(orderSamples.buy(10, 11.4));
        book.takeOrder(orderSamples.sell(20, 12.5));
        book.takeOrder(orderSamples.sell(15, 12.2));
        book.takeOrder(orderSamples.buy(15, 11.9));
        book.takeOrder(orderSamples.sell(15, 12.4));

        Optional<T> found = book.topAsk().map(OrderBookTest::asTuple);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(t(quantity(15), price(12.2)));
    }

    @Test
    public void orderBook_should_return_the_record_with_lowest_price_but_first_arrived__as_top_ask() {
        OrderBook book = new OrderBook();
        book.takeOrder(orderSamples.buy(10, 11.4));
        book.takeOrder(orderSamples.sell(20, 12.5));
        book.takeOrder(orderSamples.sell(15, 12.2));
        book.takeOrder(orderSamples.sell(5, 12.2));
        book.takeOrder(orderSamples.buy(15, 11.9));
        book.takeOrder(orderSamples.sell(15, 12.4));

        Optional<T> found = book.topAsk().map(OrderBookTest::asTuple);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(t(quantity(15), price(12.2)));
    }

    @Test
    public void orderBook_should_return_the_record_with_highest_price_as_top_bid() {
        OrderBook book = new OrderBook();
        book.takeOrder(orderSamples.buy(10, 11.4));
        book.takeOrder(orderSamples.sell(20, 12.5));
        book.takeOrder(orderSamples.sell(15, 12.2));
        book.takeOrder(orderSamples.buy(15, 11.9));
        book.takeOrder(orderSamples.sell(15, 12.4));

        Optional<T> found = book.topBid().map(OrderBookTest::asTuple);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(t(quantity(15), price(11.9)));
    }

    @Test
    public void orderBook_should_return_the_record_with_highest_price_but_first_arrived_as_top_bid() {
        OrderBook book = new OrderBook();
        book.takeOrder(orderSamples.buy(10, 11.4));
        book.takeOrder(orderSamples.sell(20, 12.5));
        book.takeOrder(orderSamples.sell(15, 12.2));
        book.takeOrder(orderSamples.buy(15, 11.9));
        book.takeOrder(orderSamples.sell(15, 12.4));
        book.takeOrder(orderSamples.buy(5, 11.9));

        Optional<T> found = book.topBid().map(OrderBookTest::asTuple);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get()).isEqualTo(t(quantity(15), price(11.9)));
    }

    private List<T> extract(OrderBook book, Order.Way way) {
        return book.records()
                .filter(r -> r.way() == way)
                .map(OrderBookTest::asTuple)
                .collect(toList());
    }

    private static T asTuple(Record r) {
        return t(r.quantity(), r.price());
    }

}