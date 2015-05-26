package yamex.order;

import yamex.BrokerId;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LimitOrder implements Order {
    private final BrokerId brokerId;
    private final String instrument;
    private final Way way;
    private final int qty;
    private final BigDecimal priceLimit;

    public LimitOrder(BrokerId brokerId,
                      String instrument,
                      Way way,
                      int qty,
                      BigDecimal priceLimit) {
        this.brokerId = brokerId;
        this.instrument = instrument;
        this.way = way;
        this.qty = qty;
        this.priceLimit = priceLimit;
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
        return OrderType.LimitOrder;
    }

    @Override
    public int quantity() {
        return qty;
    }

    @Override
    public Way way() {
        return way;
    }

    public BigDecimal priceLimit() {
        return priceLimit;
    }
}
