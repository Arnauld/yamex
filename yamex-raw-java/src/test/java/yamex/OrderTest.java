package yamex;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static yamex.BrokerId.brokerId;
import static yamex.Order.Way.Buy;
import static yamex.OrderId.orderId;
import static yamex.Quantity.quantity;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderTest {

    @Test
    public void new_order() {
        OrderId orderId = orderId("A");
        BrokerId brokerId = brokerId("A");
        Quantity qty = quantity(10);
        Price price = Price.price(10.4);

        Order order = new Order(orderId, brokerId, Buy, qty, price);
        assertThat(order.orderId()).isEqualTo(orderId);
        assertThat(order.brokerId()).isEqualTo(brokerId).isNotEqualTo(orderId);
        assertThat(order.way()).isEqualTo(Buy);
        assertThat(order.quantity()).isEqualTo(qty);
        assertThat(order.price()).isEqualTo(price);
    }
}
