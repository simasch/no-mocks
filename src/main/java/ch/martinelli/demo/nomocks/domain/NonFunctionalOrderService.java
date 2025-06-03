package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.db.tables.records.OrderItemRecord;
import ch.martinelli.demo.nomocks.db.tables.records.PurchaseOrderRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NonFunctionalOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    NonFunctionalOrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public PurchaseOrderRecord createOrder(long customerId) {
        return orderRepository.createOrder(customerId);
    }

    @Transactional
    public OrderItemRecord addItem(long purchaseOrderId, long productId, int quantity) {
        if (!orderRepository.purchaseOrderExists(purchaseOrderId)) {
            throw new IllegalArgumentException("Purchase order does not exist");
        }

        var product = productRepository.findProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product does not exist"));

        var productPriceConfiguration = productRepository.findProductPriceConfiguration(productId);
        var calculatedPrice = productPriceConfiguration
                .map(priceConfiguration -> {
                    if (quantity >= priceConfiguration.getMinQuantity()
                            && priceConfiguration.getDiscountPercentage() != null) {
                        var discountFactor = BigDecimal.ONE
                                .subtract(new BigDecimal(priceConfiguration.getDiscountPercentage())
                                        .divide(new BigDecimal(100), 3, RoundingMode.HALF_UP));
                        return product.getPrice().multiply(discountFactor).setScale(3, RoundingMode.HALF_UP);
                    } else {
                        return product.getPrice();
                    }
                })
                .orElse(product.getPrice());

        var orderItem = new OrderItemRecord(null, quantity, calculatedPrice, purchaseOrderId, productId);

        var savedOrderItem = orderRepository.addItem(orderItem);
        // This is a hack, otherwise the test would only test the mock objects
        orderItem.setId(savedOrderItem.getId());
        return orderItem;
    }

    @Transactional
    public void updateQuantity(long orderItemId, int quantity) {
        orderRepository.updateQuantity(orderItemId, quantity);
    }
}