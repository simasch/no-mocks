package ch.martinelli.demo.nomocks.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

class PriceCalculator {

    BigDecimal calculatePrice(BigDecimal price, int quantity, int minQuantity, int discountPercentage) {
        if (quantity >= minQuantity) {
            var discountFactor = BigDecimal.ONE.subtract(
                    new BigDecimal(discountPercentage).divide(new BigDecimal(100), 3, RoundingMode.HALF_UP));
            return price.multiply(discountFactor).setScale(3, RoundingMode.HALF_UP);
        } else {
            return price;
        }
    }
}
