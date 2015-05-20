package yamex.orderbook;

import yamex.Execution;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface OrderBookListener {

    void onOrderPlaced(Record record);

    void onOrderConsumed(Record records);

    void onExecution(Execution execution);

    static OrderBookListener NULL_LISTENER = new OrderBookListener() {
        @Override
        public void onOrderPlaced(Record record) {
        }

        @Override
        public void onOrderConsumed(Record records) {
        }

        @Override
        public void onExecution(Execution execution) {
        }
    };
}
