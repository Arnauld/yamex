package yamex.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class MyCollectors {

    private MyCollectors() {
    }

    public static <E> Collector<E, ?, List<E>> maxList(Comparator<E> comp) {
        return Collector.of(
                ArrayList::new,
                (list, t) -> {
                    int c;
                    if (list.isEmpty() || (c = comp.compare(t, list.get(0))) == 0) {
                        list.add(t);
                    } else if (c > 0) {
                        list.clear();
                        list.add(t);
                    }
                },
                (list1, list2) -> {
                    int r = comp.compare((E) list1.get(0), (E) list2.get(0));
                    if (r < 0) {
                        return list2;
                    } else if (r > 0) {
                        return list1;
                    } else {
                        list1.addAll(list2);
                        return list1;
                    }
                });
    }

    public static <E> Collector<E, ?, List<E>> minList(Comparator<E> comp) {
        return Collector.of(
                ArrayList::new,
                (list, t) -> {
                    int c;
                    if (list.isEmpty() || (c = comp.compare(t, list.get(0))) == 0) {
                        list.add(t);
                    } else if (c < 0) {
                        list.clear();
                        list.add(t);
                    }
                },
                (list1, list2) -> {
                    int r = comp.compare((E) list1.get(0), (E) list2.get(0));
                    if (r < 0) {
                        return list1;
                    } else if (r > 0) {
                        return list2;
                    } else {
                        list1.addAll(list2);
                        return list1;
                    }
                });
    }
}
