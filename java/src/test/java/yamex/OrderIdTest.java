package yamex;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static yamex.OrderId.orderId;

public class OrderIdTest {

    @Test
    public void type_and_null_sanity() {
        assertThat(orderId("A")).isNotEqualTo((OrderId) null);
        assertThat(orderId("A")).isNotEqualTo("A");
    }

    @Test
    public void identical_ids_should_be_equal() {
        assertThat(orderId("A")).isEqualTo(orderId("AB".substring(0, 1)));
    }
}