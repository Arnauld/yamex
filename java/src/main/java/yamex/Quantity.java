package yamex;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Quantity {

    public static Quantity quantity(int amount) {
        return new Quantity(amount);
    }

    private final int amount;

    public Quantity(int amount) {
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }

    public Quantity decreasedBy(Quantity other) {
        return quantity(amount - other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Quantity quantity = (Quantity) o;
        return amount == quantity.amount;

    }

    @Override
    public int hashCode() {
        return amount;
    }
}
