package yamex.order;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class LimitOrder {
    private final String instrument;
    private final Way way;
    private final int qty;
    private final BigDecimal priceLimit;

    public LimitOrder(String instrument, Way way, int qty, BigDecimal priceLimit) {
        this.instrument = instrument;
        this.way = way;
        this.qty = qty;
        this.priceLimit = priceLimit;
    }

    public BigDecimal getPriceLimit() {
        return priceLimit;
    }

    public Way way() {
        return way;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LimitOrder that = (LimitOrder) o;
        return way == that.way
                && qty == that.qty
                && instrument.equals(that.instrument)
                && priceLimit.equals(that.priceLimit);
    }

    @Override
    public int hashCode() {
        int result = instrument.hashCode();
        result = 31 * result + qty;
        result = 31 * result + way.hashCode();
        result = 31 * result + priceLimit.hashCode();
        return result;
    }

}
