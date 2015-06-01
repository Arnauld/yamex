package yamex.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomsTest {

    @Test
    public void pickRandomly_should_pick_randomly__one_element() {
        Random r = new Random(42);
        List<String> vs = Arrays.asList("ONE");
        for (int i = 0; i < 100; i++) {
            assertThat(Randoms.pickRandomly(vs, r)).isEqualTo("ONE");
        }
    }

    @Test
    public void pickRandomly_should_pick_randomly__three_elements() {
        Random r = new Random(42);
        List<String> vs = Arrays.asList("ONE", "TWO", "THREE");

        Map<String, AtomicInteger> counts = initializeCountBy(vs);

        for (int i = 0; i < 100; i++) {
            String k = Randoms.pickRandomly(vs, r);
            counts.get(k).incrementAndGet();
        }

        for (Map.Entry<String, AtomicInteger> entry : counts.entrySet()) {
            assertThat(entry.getValue().get())
                    .describedAs("For key " + entry.getKey())
                    .isGreaterThan(0);
        }
    }

    private static Map<String, AtomicInteger> initializeCountBy(List<String> vs) {
        Map<String, AtomicInteger> counts = new HashMap<>();
        for (String v : vs)
            counts.put(v, new AtomicInteger(0));
        return counts;
    }
}