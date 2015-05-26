package yamex;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Id {
    private final String raw;

    protected Id(String raw) {
        this.raw = raw;
    }

    public String raw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Id id = (Id) o;
        return raw.equals(id.raw);
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + raw + "]";
    }
}
