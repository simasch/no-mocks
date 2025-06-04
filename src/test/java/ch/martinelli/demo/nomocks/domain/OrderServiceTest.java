package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    @Transactional
    void add_item_without_discount() {
        var purchaseOrder = orderService.createOrder(1L);
        assertThat(purchaseOrder).isNotNull();
        assertThat(purchaseOrder.getId()).isNotNull();

        // Add product 1 with quantity 1 (below the discount threshold)
        var orderItem = orderService.addItem(purchaseOrder.getId(), 1L, 1);

        assertThat(orderItem.getPrice()).isEqualTo(new BigDecimal("24.96"));
    }

    @Test
    @Transactional
    void add_item_with_discount() {
        var purchaseOrder = orderService.createOrder(1L);
        assertThat(purchaseOrder).isNotNull();
        assertThat(purchaseOrder.getId()).isNotNull();

        // Add product 1 with quantity 10 (at the discount threshold)
        var orderItem = orderService.addItem(purchaseOrder.getId(), 1L, 10);

        // Calculate expected price with 10% discount
        var originalPrice = new BigDecimal("24.96");
        var discountMultiplier = BigDecimal.ONE.subtract(new BigDecimal("0.1"));
        var expectedDiscountedPrice = originalPrice.multiply(discountMultiplier);

        assertThat(orderItem.getPrice()).isEqualTo(expectedDiscountedPrice);
    }
}
