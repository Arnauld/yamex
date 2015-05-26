package yamex.order;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WayTest {

    @Test
    public void other_of_Buy_should_be_Sell() {
        assertThat(Way.Buy.other()).isEqualTo(Way.Sell);
    }

    @Test
    public void other_of_Sell_should_be_Buy() {
        assertThat(Way.Sell.other()).isEqualTo(Way.Buy);
    }
}