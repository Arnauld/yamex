package yamex.order;

import yamex.BrokerId;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public interface Order {

    String instrument();

    OrderType type();

    Way way();

    int quantity();

    BrokerId brokerId();
}
