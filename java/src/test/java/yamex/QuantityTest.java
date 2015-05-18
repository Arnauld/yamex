package yamex;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static yamex.Quantity.quantity;

public class QuantityTest {

    @Test
    public void type_and_null_sanity() {
        assertThat(quantity(10)).isNotEqualTo((Quantity) null);
        assertThat(quantity(10)).isNotEqualTo(10);
    }

    @Test
    public void quantity_amount_should_be_equal_to_the_one_provided() {
        assertThat(quantity(17).amount()).isEqualTo(17);
        assertThat(quantity(-1).amount()).isEqualTo(-1);
    }

    @Test
    public void quantity_decrease_should_provide_a_new_instance() {
        Quantity q27 = quantity(27);
        Quantity q15 = quantity(15);
        Quantity q12 = quantity(12);
        assertThat(q27.decreasedBy(q15))
                .isNotSameAs(q27)
                .isNotSameAs(q15)
                .isEqualTo(q12);
    }

    @Test
    public void quantity_add_should_provide_a_new_instance() {
        Quantity q27 = quantity(27);
        Quantity q15 = quantity(15);
        Quantity q12 = quantity(12);
        assertThat(q12.add(q15))
                .isNotSameAs(q12)
                .isNotSameAs(q15)
                .isEqualTo(q27);
    }


    @Test
    public void quantity_should_indicate_whether_positive_or_not() {
        assertThat(quantity(27).isPositive()).isTrue();
        assertThat(quantity(1).isPositive()).isTrue();
        assertThat(quantity(0).isPositive()).isFalse();
        assertThat(quantity(-1).isPositive()).isFalse();
    }
}