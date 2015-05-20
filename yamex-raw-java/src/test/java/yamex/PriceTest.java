package yamex;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static yamex.Price.price;

public class PriceTest {

    @Test(expected = NumberFormatException.class)
    public void non_decimal_should_throw_exception() {
        price("A");
    }

    @Test
    public void type_and_null_sanity() {
        assertThat(price("12.1")).isNotEqualTo((Price) null);
        assertThat(price("12.1")).isNotEqualTo(BigDecimal.valueOf(121, 1));
    }

    @Test
    public void price_amount_should_be_equal_to_the_one_provided() {
        assertThat(price("10.1").amount()).isEqualTo(BigDecimal.valueOf(101, 1));
    }

    @Test
    public void identical_prices_should_be_equal() {
        assertThat(price("10.1")).isEqualTo(price(BigDecimal.valueOf(101, 1)));
    }
}