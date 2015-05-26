package yamex;

import org.junit.Test;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class IdTest {

    @Test
    public void equals_should_accept_same_instance() {
        Consumer<Id> eq = (id) -> assertThat(id).isEqualTo(id);

        eq.accept(new BrokerId("aef34"));
        eq.accept(new OrderId("aef34"));
    }

    @Test
    public void hashcode_is_not_a_constant() {
        assertThat(new BrokerId("aef34").hashCode())
                .isNotEqualTo(new BrokerId("fea43").hashCode());
    }

    @Test
    public void equals_should_accept_same_class() {
        assertThat(new BrokerId("aef34")).isEqualTo(new BrokerId("aef34"));
        assertThat(new BrokerId("aef34")).isNotEqualTo(new BrokerId("AE"));
        assertThat(new OrderId("aef34")).isEqualTo(new OrderId("aef34"));
        assertThat(new OrderId("aef34")).isNotEqualTo(new OrderId("AE"));
    }

    @Test
    public void equals_should_not_accept_super_class() {
        assertThat(new BrokerId("aef34")).isNotEqualTo(new Id("aef34"));
    }

    @Test
    public void equals_should_not_accept_sister_class() {
        assertThat(new BrokerId("aef34")).isNotEqualTo(new OrderId("aef34"));
    }
}