package ch.martinelli.demo.nomocks.domain;

import java.math.BigDecimal;

public record OrderItem(Long id, int quantity, BigDecimal price, Product product) {
}