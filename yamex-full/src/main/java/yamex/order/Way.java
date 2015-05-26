package yamex.order;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum Way {
    Sell, Buy;

    public Way other() {
        switch (this) {
            case Sell:
                return Buy;
            case Buy:
                return Sell;
            default:
                throw new UnsupportedOperationException("Way::other not supported for " + this);
        }
    }
}
