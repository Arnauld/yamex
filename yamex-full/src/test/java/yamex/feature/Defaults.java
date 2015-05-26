package yamex.feature;

import yamex.BrokerId;
import yamex.order.OrderType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Defaults {
    public enum Key {
        ORDER_QTY(100),
        ORDER_PRICE(new BigDecimal("10.1")),
        ORDER_BROKER_ID(new BrokerId("Broker-Default")),
        ORDER_TYPE(OrderType.LimitOrder),
        ORDER_INSTRUMENT("FFLY");

        private final Object v;

        Key(Object v) {
            this.v = v;
        }
    }

    private final Map<Key, Object> values = new HashMap<>();

    public void reset(Key key) {
        values.remove(key);
    }

    public void put(Key key, Object value) {
        values.put(key, value);
    }

    public <T> T get(Class<T> type, Key key) {
        return type.cast(values.getOrDefault(key, key.v));
    }

}
