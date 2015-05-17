package yamex;

import org.junit.Test;

import java.util.List;

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

    private List<T> extract(OrderBook book, Order.Way way) {
        return book.records()
                .filter(r -> r.way() == way)
                .map(r -> t(r.quantity(), r.price()))
                .collect(toList());
    }

}