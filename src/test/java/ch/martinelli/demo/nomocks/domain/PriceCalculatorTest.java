package ch.martinelli.demo.nomocks.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceCalculatorTest {

    private final PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    void calculate_price_without_discount() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 9, 10, 10);

        assertThat(price).isEqualTo(new BigDecimal("3.44"));
    }

    @Test
    void calculate_price_with_discount() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 11, 10, 10);

        assertThat(price).isEqualTo(new BigDecimal("3.096"));
    }

}
