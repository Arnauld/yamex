package yamex.feature.order;

import yamex.order.CumulativeView;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CumulativeViewRowProto {
    public Integer sumOfBids;
    public Integer bidQty;
    public BigDecimal bidPrice;
    public BigDecimal askPrice;
    public Integer askQty;
    public Integer sumOfAsks;

    public static CumulativeViewRowProto from(CumulativeView.Entry bidEntry, CumulativeView.Entry askEntry) {
        CumulativeViewRowProto proto = new CumulativeViewRowProto();
        if (bidEntry != null) {
            proto.sumOfBids = bidEntry.cumulativeSum();
            proto.bidQty = bidEntry.quantity();
            proto.bidPrice = bidEntry.price();
        }
        if (askEntry != null) {
            proto.askPrice = askEntry.price();
            proto.askQty = askEntry.quantity();
            proto.sumOfAsks = askEntry.cumulativeSum();
        }
        return proto;
    }
}
