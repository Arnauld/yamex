package yamex.order;

import org.junit.Test;
import yamex.BrokerId;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketOrderTest {

    private static final BrokerId BROKER_ID = new BrokerId("KBOOM");

    @Test
    public void ctor() throws Exception {
        MarketOrder marketOrder = new MarketOrder(BROKER_ID, "FFLY", Way.Sell, 100);

        assertThat(marketOrder.type()).isEqualTo(OrderType.MarketOrder);
        assertThat(marketOrder.instrument()).isEqualTo("FFLY");
        assertThat(marketOrder.way()).isEqualTo(Way.Sell);
        assertThat(marketOrder.quantity()).isEqualTo(100);
    }
}