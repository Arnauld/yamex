package yamex.util;

import org.junit.AssumptionViolatedException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JUnits {
    public static void skip(String reason) {
        throw new AssumptionViolatedException(reason);
    }
}
