package yamex.order;

import yamex.BrokerId;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MarketOrder implements Order {
    private final BrokerId brokerId;
    private final String instrument;
    private final Way way;
    private final int qty;

    public MarketOrder(BrokerId brokerId,
                       String instrument,
                       Way way,
                       int qty) {
        this.brokerId = brokerId;
        this.instrument = instrument;
        this.way = way;
        this.qty = qty;
    }

    @Override
    public BrokerId brokerId() {
        return brokerId;
    }

    @Override
    public String instrument() {
        return instrument;
    }

    @Override
    public OrderType type() {
        return OrderType.MarketOrder;
    }

    @Override
    public int quantity() {
        return qty;
    }

    @Override
    public Way way() {
        return way;
    }

}
