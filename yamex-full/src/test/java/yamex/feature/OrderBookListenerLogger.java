package yamex.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamex.OrderId;
import yamex.order.Execution;
import yamex.order.Order;
import yamex.order.OrderBookListener;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class OrderBookListenerLogger implements OrderBookListener {
    private Logger log = LoggerFactory.getLogger(OrderBookListenerLogger.class);

    @Override
    public void resolvingOrder(OrderId id, Order order, int qtyRemaining) {
        log.debug("Resolving order {}, {}, {}", id, order, qtyRemaining);
    }

    @Override
    public void orderPlaced(OrderId id, Order order) {
        log.info("Order placed {}, {}", id, order);

    }

    @Override
    public void orderMutated(OrderId id, Order oldOrder, Order newOrder) {
        log.debug("Order mutated {}, {}", id, oldOrder, newOrder);
    }

    @Override
    public void orderConsumed(OrderId id, Order order, int qtyConsumed, int qtyRemaining) {
        log.info("Order consumed {}, {}, consumed: {}, remaining: {}", id, order, qtyConsumed, qtyRemaining);

    }

    @Override
    public void orderCancelled(OrderId id, Order order) {
        log.info("Order cancelled {}, {}", id, order);
    }

    @Override
    public void orderDismissed(OrderId id, Order order) {
        log.info("Order dismissed {}, {}", id, order);
    }

    @Override
    public void executionTriggered(Execution execution) {
        log.info("Execution triggered {}", execution);
    }

    @Override
    public void marketPriceChanged(BigDecimal marketPrice) {
        log.info("Market price changed {}", marketPrice);
    }
}
