package ch.martinelli.demo.nomocks.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

class PriceCalculator {

    BigDecimal calculatePrice(BigDecimal price, int quantity, int minQuantity, int discountPercentage) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (minQuantity < 0) {
            throw new IllegalArgumentException("Minimum quantity cannot be negative");
        }
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        // No discount if quantity is less than the minimum quantity or discount is 0
        if (quantity < minQuantity || discountPercentage == 0) {
            return price;
        }

        // Calculate discount factor (e.g., 10% discount = 0.9 factor)
        var discountFactor = BigDecimal.ONE.subtract(
                BigDecimal.valueOf(discountPercentage)
                        .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));

        // Apply discount and round to 3 decimal places
        return price.multiply(discountFactor).setScale(3, RoundingMode.HALF_UP);
    }
}
