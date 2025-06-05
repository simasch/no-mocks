package ch.martinelli.demo.nomocks.domain;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrder(Long id, LocalDateTime orderDate,
                     Customer customer, List<OrderItem> items) {
}