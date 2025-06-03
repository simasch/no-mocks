package ch.martinelli.demo.nomocks.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceCalculatorTest {

    private final PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    void calculatePriceWithoutDiscount_QuantityLessThanMinQuantity() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 9, 10, 10);

        assertThat(price).isEqualTo(new BigDecimal("3.44"));
    }

    @Test
    void calculatePriceWithDiscount() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 11, 10, 10);

        assertThat(price).isEqualTo(new BigDecimal("3.096"));
    }

    @Test
    void calculatePriceWithoutDiscount_DiscountPercentageIsZero() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 11, 10, 0);

        assertThat(price).isEqualTo(new BigDecimal("3.44"));
    }

    @Test
    void calculatePriceWithDiscount_ExactlyMinQuantity() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 10, 10, 10);

        assertThat(price).isEqualTo(new BigDecimal("3.096"));
    }

    @Test
    void calculatePriceWithDiscount_FullDiscount() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.44"), 11, 10, 100);

        assertThat(price).isEqualTo(new BigDecimal("0.000"));
    }

    @Test
    void calculatePriceWithDiscount_RoundingBehavior() {
        var price = priceCalculator.calculatePrice(new BigDecimal("3.333"), 11, 10, 33);

        // 3.333 * (1 - 33/100) = 3.333 * 0.67 = 2.23311, rounded to 3 decimal places = 2.233
        assertThat(price).isEqualTo(new BigDecimal("2.233"));
    }

    @Test
    void calculatePrice_NullPrice() {
        assertThatThrownBy(() -> priceCalculator.calculatePrice(null, 10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price cannot be null");
    }

    @Test
    void calculatePrice_NegativeQuantity() {
        assertThatThrownBy(() -> priceCalculator.calculatePrice(new BigDecimal("3.44"), -1, 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity cannot be negative");
    }

    @Test
    void calculatePrice_NegativeMinQuantity() {
        assertThatThrownBy(() -> priceCalculator.calculatePrice(new BigDecimal("3.44"), 10, -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Minimum quantity cannot be negative");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void calculatePrice_InvalidDiscountPercentage(int invalidDiscount) {
        assertThatThrownBy(() -> priceCalculator.calculatePrice(new BigDecimal("3.44"), 10, 10, invalidDiscount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount percentage must be between 0 and 100");
    }
}
