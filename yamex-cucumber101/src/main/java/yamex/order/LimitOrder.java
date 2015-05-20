package yamex.order;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LimitOrder {
    private final String instrument;
    private final int qty;
    private final BigDecimal priceLimit;

    public LimitOrder(String instrument, int qty, BigDecimal priceLimit) {
        this.instrument = instrument;
        this.qty = qty;
        this.priceLimit = priceLimit;
    }
}
