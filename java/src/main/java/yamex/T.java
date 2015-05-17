package yamex;

import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class T {

    public static T t(Object... args) {
        return new T(args);
    }

    private final Object[] args;

    public T(Object... args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        T t = (T) o;
        return Arrays.equals(args, t.args);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(args);
    }

    @Override
    public String toString() {
        return "T{" + Arrays.toString(args) + '}';
    }
}
