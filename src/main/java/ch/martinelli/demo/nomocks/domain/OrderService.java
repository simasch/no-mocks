package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.db.tables.records.PurchaseOrderRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public PurchaseOrder createOrder(long customerId) {
        var customer = customerRepository.findCustomer(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        PurchaseOrderRecord purchaseOrder = orderRepository.createOrder(customerId);

        return new PurchaseOrder(purchaseOrder.getId(), purchaseOrder.getOrderDate(), customer, List.of());
    }

    @Transactional
    public OrderItem addItem(long purchaseOrderId, long productId, int quantity) {
        if (!orderRepository.purchaseOrderExists(purchaseOrderId)) {
            throw new IllegalArgumentException("Purchase order does not exist");
        }

        var product = productRepository.findProduct(productId).orElseThrow(() -> new IllegalArgumentException("Product does not exist"));

        var productPriceConfiguration = productRepository.findProductPriceConfiguration(productId);
        var calculatedPrice = productPriceConfiguration
                .map(priceConfiguration ->
                        priceCalculator.calculatePrice(product.price(), quantity, priceConfiguration.getMinQuantity(),
                                priceConfiguration.getDiscountPercentage()))
                .orElse(product.price());

        var orderItem = orderRepository.addItem(purchaseOrderId, productId, quantity, calculatedPrice);
        return new OrderItem(orderItem.getId(), orderItem.getQuantity(), calculatedPrice, product);
    }

    @Transactional
    public void updateQuantity(long orderItemId, int quantity) {
        orderRepository.updateQuantity(orderItemId, quantity);
    }
}