package yamex;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static yamex.BrokerId.brokerId;

public class BrokerIdTest {

    @Test
    public void type_and_null_sanity() {
        assertThat(brokerId("A")).isNotEqualTo((BrokerId)null);
        assertThat(brokerId("A")).isNotEqualTo("A");
    }

    @Test
    public void identical_ids_should_be_equal() {
        assertThat(brokerId("A")).isEqualTo(brokerId("AB".substring(0, 1)));
    }
}