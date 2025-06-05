package ch.martinelli.demo.nomocks.query;

import java.math.BigDecimal;

public record OrderItemInfo(Long id, String productName, int quantity, BigDecimal price) {
}
