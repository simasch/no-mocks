package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.db.tables.records.OrderItemRecord;
import ch.martinelli.demo.nomocks.db.tables.records.ProductPriceConfigurationRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NonFunctionalOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private NonFunctionalOrderService orderService;

    @Test
    void add_item_without_discount() {
        var purchaseOrderId = 1L;
        var productId = 1L;
        var quantity = 9;
        var productPrice = new BigDecimal("3.44");

        var product = new Product(productId, "", productPrice);
        var priceConfig = new ProductPriceConfigurationRecord(1L, 10, 10, productId);
        var expectedOrderItem = new OrderItemRecord(1L, quantity, productPrice, purchaseOrderId, productId);

        when(orderRepository.purchaseOrderExists(purchaseOrderId)).thenReturn(true);
        when(productRepository.findProduct(productId)).thenReturn(Optional.of(product));
        when(productRepository.findProductPriceConfiguration(productId)).thenReturn(Optional.of(priceConfig));
        when(orderRepository.addItem(eq(purchaseOrderId), eq(productId), eq(quantity), eq(productPrice))).thenReturn(expectedOrderItem);

        var orderItem = orderService.addItem(purchaseOrderId, productId, quantity);

        assertThat(orderItem).isNotNull();
        assertThat(orderItem.price()).isEqualTo(productPrice);
    }

    @Test
    void add_item_with_discount() {
        var purchaseOrderId = 1L;
        var productId = 1L;
        var quantity = 11;
        var productPrice = new BigDecimal("3.44");
        var calculatedPrice = new BigDecimal("3.096"); // 10% discount

        var product = new Product(productId, "", productPrice);
        var priceConfig = new ProductPriceConfigurationRecord(1L, 10, 10, productId);
        var expectedOrderItem = new OrderItemRecord(1L, quantity, calculatedPrice, purchaseOrderId, productId);

        when(orderRepository.purchaseOrderExists(purchaseOrderId)).thenReturn(true);
        when(productRepository.findProduct(productId)).thenReturn(Optional.of(product));
        when(productRepository.findProductPriceConfiguration(productId)).thenReturn(Optional.of(priceConfig));
        when(orderRepository.addItem(eq(purchaseOrderId), eq(productId), eq(quantity), eq(calculatedPrice))).thenReturn(expectedOrderItem);

        var orderItem = orderService.addItem(purchaseOrderId, productId, quantity);

        assertThat(orderItem).isNotNull();
        assertThat(orderItem.price()).isEqualTo(calculatedPrice);
    }
}
