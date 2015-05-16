package yamex;

import java.math.BigDecimal;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Price {

    public static Price price(double amount) {
        return price(BigDecimal.valueOf(amount));
    }

    public static Price price(String amount) {
        return price(new BigDecimal(amount));
    }

    public static Price price(BigDecimal amount) {
        return new Price(amount);
    }

    private final BigDecimal amount;

    public Price(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Price price = (Price) o;
        return amount.equals(price.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }
}
