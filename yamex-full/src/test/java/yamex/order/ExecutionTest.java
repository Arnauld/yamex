package yamex.order;

import org.junit.Test;
import yamex.BrokerId;
import yamex.OrderId;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecutionTest {

    @Test
    public void execution_should_return_provided_data() {
        Execution exec = new Execution(
                new OrderId("sellId"), new BrokerId("sellerId"),
                new OrderId("buyId"), new BrokerId("buyerId"),
                127,
                new BigDecimal("110.4"));

        assertThat(exec.price()).isEqualTo(new BigDecimal("110.4"));
        assertThat(exec.quantity()).isEqualTo(127);
        assertThat(exec.sellerId()).isEqualTo(new BrokerId("sellerId"));
        assertThat(exec.sellId()).isEqualTo(new OrderId("sellId"));
        assertThat(exec.buyerId()).isEqualTo(new BrokerId("buyerId"));
        assertThat(exec.buyId()).isEqualTo(new OrderId("buyId"));
    }

}