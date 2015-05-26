package yamex.order;

import yamex.OrderId;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface OrderBook {

    public static void placeOrder(OrderBook book, Order order) {
        switch (order.type()) {
            case LimitOrder:
                book.placeOrder((LimitOrder) order);
                break;
            case MarketOrder:
                book.placeOrder((MarketOrder) order);
                break;
            case StopOrder:
                book.placeOrder((StopOrder) order);
                break;
        }
    }

    OrderId placeOrder(LimitOrder order);

    OrderId placeOrder(MarketOrder order);

    OrderId placeOrder(StopOrder order);

    Stream<Record> records();

    Optional<BigDecimal> bestBidPrice();

    Optional<BigDecimal> bestAskPrice();

    int availableQty(Way way, BigDecimal priceThreshold);

    CumulativeView cumulativeView();

    public interface Record {
        OrderId orderId();

        int remainingQuantity();

        Order order();
    }
}
