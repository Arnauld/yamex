package yamex.feature;

import org.junit.Test;
import yamex.BrokerId;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultsTest {

    @Test
    public void usecase() {
        Defaults defaults = new Defaults();
        assertThat(defaults.get(BrokerId.class, Defaults.Key.ORDER_BROKER_ID)).isEqualTo(new BrokerId("Broker-Default"));

        defaults.put(Defaults.Key.ORDER_BROKER_ID, new BrokerId("B1"));
        assertThat(defaults.get(BrokerId.class, Defaults.Key.ORDER_BROKER_ID)).isEqualTo(new BrokerId("B1"));

        defaults.reset(Defaults.Key.ORDER_BROKER_ID);
        assertThat(defaults.get(BrokerId.class, Defaults.Key.ORDER_BROKER_ID)).isEqualTo(new BrokerId("Broker-Default"));
    }
}