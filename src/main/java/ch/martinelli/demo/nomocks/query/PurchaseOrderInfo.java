package ch.martinelli.demo.nomocks.query;

import java.time.LocalDateTime;
import java.util.List;

record PurchaseOrderInfo(Long id, LocalDateTime orderDate, String firstName, String lastName,
                         String street, String postalCode, String city, List<OrderItemInfo> items) {
}
