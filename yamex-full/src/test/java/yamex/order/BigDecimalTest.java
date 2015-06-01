package yamex.order;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class BigDecimalTest {

    @Test
    public void ctor() {
        assertThat(new BigDecimal("12.1234")).isEqualTo(new BigDecimal(BigInteger.valueOf(121234), 4));
    }
}
