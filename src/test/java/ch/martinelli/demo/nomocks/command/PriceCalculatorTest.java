package ch.martinelli.demo.nomocks.command;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceCalculatorTest {

    private final PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    void calculatePriceWithoutDiscount() {
        BigDecimal price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 10);

        assertThat(price).isEqualTo(new BigDecimal("3.44"));
    }

    @Test
    void calculatePriceWithDiscount() {
        BigDecimal price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 11);

        assertThat(price).isEqualTo(new BigDecimal("3.096"));
    }
}