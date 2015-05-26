package yamex.order;

import org.junit.Test;
import yamex.BrokerId;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class LimitOrderTest {

    private static final BrokerId BROKER_ID = new BrokerId("KBOOM");

    @Test
    public void ctor() throws Exception {
        LimitOrder order = new LimitOrder(BROKER_ID, "FFLY", Way.Sell, 100, new BigDecimal("10.2"));

        assertThat(order.type()).isEqualTo(OrderType.LimitOrder);
        assertThat(order.instrument()).isEqualTo("FFLY");
        assertThat(order.way()).isEqualTo(Way.Sell);
        assertThat(order.quantity()).isEqualTo(100);
        assertThat(order.priceLimit()).isEqualTo(new BigDecimal("10.2"));
    }
}