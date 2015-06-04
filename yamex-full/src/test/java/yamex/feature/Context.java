package yamex.feature;

import yamex.BrokerId;
import yamex.OrderId;
import yamex.fixprotocol.FieldDictionary;
import yamex.fixprotocol.FixProtocol;
import yamex.order.Execution;
import yamex.order.ExecutionBus;
import yamex.order.Order;
import yamex.order.OrderBook;
import yamex.order.memory.OrderBookBasic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Context {
    private static final Object CURRENT_ORDER = "current-order";
    private static final Object CURRENT_ORDER_ID = "current-order-id";

    private final Defaults defaults = new Defaults();
    private final Map<Object, Object> data = new HashMap<>();
    //
    private OrderBook orderBook;
    private BrokerId brokerId = new BrokerId("broker-1");
    //
    private ExecutionBus executionBus;
    private final List<Execution> executions = new ArrayList<>();
    private Random random;
    private FixProtocol fixProtocol;
    private FieldDictionary fieldDictionary;

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public OrderBook getOrderBook() {
        if (orderBook == null) {
            resetOrderBook();
        }
        return orderBook;
    }

    public BrokerId currentBrokerId() {
        return brokerId;
    }

    public void setCurrentOrder(OrderId id, Order order) {
        data.put(CURRENT_ORDER_ID, id);
        data.put(CURRENT_ORDER, order);
    }

    public OrderId getCurrentOrderId() {
        return (OrderId) data.get(CURRENT_ORDER_ID);
    }

    public <T extends Order> T getCurrentOrder(Class<T> type) {
        return type.cast(getCurrentOrder());
    }

    public Order getCurrentOrder() {
        return Order.class.cast(data.get(CURRENT_ORDER));
    }


    public ExecutionBus getExecutionBus() {
        if (executionBus == null) {
            executionBus = executions::add;
        }
        return executionBus;
    }

    public List<Execution> getExecutions() {
        return executions;
    }

    public void resetOrderBook() {
        AtomicLong idGen = new AtomicLong();
        Supplier<OrderId> orderIdGen = () -> new OrderId("#" + idGen.incrementAndGet());

        setOrderBook(new OrderBookBasic(orderIdGen, getExecutionBus(), new OrderBookListenerLogger()));
    }

    public Defaults getDefaults() {
        return defaults;
    }

    public Random getRandom() {
        if (random == null)
            random = new Random(42L);
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void put(Object key, Object value) {
        data.put(key, value);
    }

    public <T> T get(Class<T> type, Object key) {
        return type.cast(data.get(key));
    }

    public FixProtocol getFixProtocol() {
        if (fixProtocol == null)
            fixProtocol = new FixProtocol();
        return fixProtocol;
    }

    public FieldDictionary getFieldDictionary() {
        if(fieldDictionary == null) {
            fieldDictionary = new FieldDictionary();
            fieldDictionary.registerDefaults();
        }
        return fieldDictionary;
    }
}
