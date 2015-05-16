package yamex;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BrokerId {
    private final String id;

    public static BrokerId brokerId(String id) {
        return new BrokerId(id);
    }

    private BrokerId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BrokerId brokerId = (BrokerId) o;
        return id.equals(brokerId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
