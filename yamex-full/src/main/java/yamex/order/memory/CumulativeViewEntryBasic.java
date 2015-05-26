package yamex.order.memory;

import yamex.order.CumulativeView;
import yamex.order.Way;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CumulativeViewEntryBasic implements CumulativeView.Entry {
    private final Way way;
    private final BigDecimal price;
    private final int qty;
    private final int cumQty;

    public CumulativeViewEntryBasic(Way way, BigDecimal price, int qty, int cumQty) {
        this.way = way;
        this.price = price;
        this.qty = qty;
        this.cumQty = cumQty;
    }

    @Override
    public Way way() {
        return way;
    }

    @Override
    public BigDecimal price() {
        return price;
    }

    @Override
    public int quantity() {
        return qty;
    }

    @Override
    public int cumulativeSum() {
        return cumQty;
    }

    @Override
    public String toString() {
        return "CumulativeViewEntryBasic{" +
                "way=" + way +
                ", price=" + price +
                ", qty=" + qty +
                ", cumQty=" + cumQty +
                '}';
    }
}
