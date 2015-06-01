package yamex.util;

import java.util.List;
import java.util.Random;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Randoms {

    public static <T> T pickRandomly(List<T> values, Random r) {
        return values.get(r.nextInt(values.size()));
    }
}
