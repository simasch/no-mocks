package ch.martinelli.demo.nomocks.command;

import java.math.BigDecimal;

public class PriceCalculator {

    BigDecimal calculatePrice(BigDecimal price, int quantity) {
        if (quantity > 10) {
            return price.multiply(new BigDecimal("0.9"));
        } else {
            return price;
        }
    }
}
