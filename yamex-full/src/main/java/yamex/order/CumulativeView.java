package yamex.order;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface CumulativeView {
    public interface Entry {
        public static Comparator<Entry> PriceComparatorDsc = (r1, r2) -> -r1.price().compareTo(r2.price());
        public static Comparator<Entry> PriceComparatorAsc = (r1, r2) -> +r1.price().compareTo(r2.price());

        Way way();

        BigDecimal price();

        int quantity();

        int cumulativeSum();

    }

    Stream<Entry> entries();

}
