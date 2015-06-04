package yamex.fixprotocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FixMessage {
    private final Map<Integer, Object> kvs;

    public FixMessage(Map<Integer, Object> kvs) {
        this.kvs = kvs;
    }

    public Object getValue(FieldDictionary.Field f) {
        return kvs.get(f.tag());
    }

    public static class Builder {
        private final Map<Integer, Object> kvs = new HashMap<>();

        public Builder define(int tag, String value) {
            kvs.put(tag, value);
            return this;
        }

        public FixMessage build(FixContext context) {
            return new FixMessage(kvs);
        }
    }
}
