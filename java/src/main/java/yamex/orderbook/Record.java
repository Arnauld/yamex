package yamex.orderbook;

import yamex.Execution;
import yamex.ExecutionBus;
import yamex.Order;
import yamex.OrderId;
import yamex.Price;
import yamex.Quantity;

import java.util.Comparator;

import static yamex.Order.Way.Buy;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Record {
    private final long sequence;
    private final Order order;
    private Quantity remainingQty;

    public Record(long sequence, Order order) {
        this.sequence = sequence;
        this.order = order;
        this.remainingQty = order.quantity();
    }

    public OrderId orderId() {
        return order.orderId();
    }

    public long sequence() {
        return sequence;
    }

    public Price price() {
        return order.price();
    }

    public Quantity remainingQuantity() {
        return remainingQty;
    }

    public boolean hasRemaining() {
        return remainingQuantity().isPositive();
    }

    public Order.Way way() {
        return order.way();
    }

    public static Comparator<Record> priceComparator() {
        return (r1, r2) -> r1.price().compareTo(r2.price());
    }

    public static Comparator<Record> sequenceComparator() {
        return (r1, r2) -> Long.compare(r1.sequence(), r2.sequence());
    }

    public void processExecutionIfPossible(ExecutionBus executionBus, Record r) {
        if (!hasRemaining()) {
            return;
        }

        Quantity min = Quantity.min(remainingQty, r.remainingQty);
        decreaseRemaining(min);
        r.decreaseRemaining(min);

        executionBus.triggerExecution(new Execution(
                way() == Buy ? r.orderId() : orderId(),
                way() == Buy ? orderId() : r.orderId(),
                min
        ));
    }

    private void decreaseRemaining(Quantity quantity) {
        remainingQty = remainingQty.decreasedBy(quantity);
    }

    public boolean priceCrosses(Record record) {
        int priceOrd = record.price().compareTo(price());
        switch (way()) {
            case Buy:
                return priceOrd <= 0;
            case Sell:
                return priceOrd >= 0;
            default:
                throw new IllegalStateException("Unknown way: " + way());
        }
    }
}
