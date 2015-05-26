package yamex.order;

import yamex.OrderId;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface OrderBookListener {


    default void resolvingOrder(OrderId id, Order order, int qtyRemaining) {
    }

    default void orderPlaced(OrderId id, Order order) {
    }

    default void orderMutated(OrderId id, Order oldOrder, Order newOrder) {
    }

    default void orderConsumed(OrderId id, Order order, int qtyConsumed, int qtyRemaining) {
    }

    default void orderCancelled(OrderId id, Order order) {
    }

    default void orderDismissed(OrderId id, Order order) {
    }

    default void executionTriggered(Execution execution) {
    }

    default void marketPriceChanged(BigDecimal marketPrice) {
    }

    public static OrderBookListener SOUT = new OrderBookListener() {

        @Override
        public void resolvingOrder(OrderId id, Order order, int qtyRemaining) {
            System.out.println("OrderBookListener.resolvingOrder::id = [" + id + "], order = [" + order + "], qtyRemaining = [" + qtyRemaining + "]");
        }

        @Override
        public void orderPlaced(OrderId id, Order order) {
            System.out.println("OrderBookListener.orderPlaced::id = [" + id + "], order = [" + order + "]");
        }

        @Override
        public void orderMutated(OrderId id, Order oldOrder, Order newOrder) {
            System.out.println("OrderBookListener.orderMutated::id = [" + id + "], oldOrder = [" + oldOrder + "], newOrder = [" + newOrder + "]");
        }

        @Override
        public void orderConsumed(OrderId id, Order order, int qtyConsumed, int qtyRemaining) {
            System.out.println("OrderBookListener.orderConsumed::id = [" + id + "], order = [" + order + "], qtyConsumed = [" + qtyConsumed + "], qtyRemaining = [" + qtyRemaining + "]");
        }

        @Override
        public void orderCancelled(OrderId id, Order order) {
            System.out.println("OrderBookListener.orderCancelled::id = [" + id + "], order = [" + order + "]");
        }

        @Override
        public void orderDismissed(OrderId id, Order order) {
            System.out.println("OrderBookListener.orderDismissed::id = [" + id + "], order = [" + order + "]");
        }

        @Override
        public void executionTriggered(Execution execution) {
            System.out.println("OrderBookListener.executionTriggered::execution = [" + execution + "]");
        }

        @Override
        public void marketPriceChanged(BigDecimal marketPrice) {
            System.out.println("OrderBookListener.marketPriceChanged::marketPrice = [" + marketPrice + "]");
        }
    };

}
