package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.db.tables.records.OrderItemRecord;
import ch.martinelli.demo.nomocks.db.tables.records.PurchaseOrderRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
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

        var product = productRepository.findProduct(productId).orElseThrow(() -> new IllegalArgumentException("Product does not exist"));

        var productPriceConfiguration = productRepository.findProductPriceConfiguration(productId);
        var calculatedPrice = productPriceConfiguration
                .map(priceConfiguration ->
                        priceCalculator.calculatePrice(product.getPrice(), quantity, priceConfiguration.getMinQuantity(),
                                priceConfiguration.getDiscountPercentage()))
                .orElse(product.getPrice());

        return orderRepository.addItem(purchaseOrderId, productId, quantity, calculatedPrice);
    }

    @Transactional
    public void updateQuantity(long orderItemId, int quantity) {
        orderRepository.updateQuantity(orderItemId, quantity);
    }
}