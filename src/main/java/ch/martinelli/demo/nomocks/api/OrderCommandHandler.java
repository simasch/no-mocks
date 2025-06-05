package ch.martinelli.demo.nomocks.api;

import ch.martinelli.demo.nomocks.domain.OrderService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("commands/order")
@RestController
class OrderCommandHandler {

    private final OrderService orderService;

    OrderCommandHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    Optional<?> handle(@RequestBody @Nonnull @Valid OrderCommand orderCommand) {
        switch (orderCommand) {
            case OrderCommand.CreateOrderCommand(long customerId) -> {
                var purchaseOrder = orderService.createOrder(customerId);
                return Optional.of(purchaseOrder.id());
            }
            case OrderCommand.AddOrderItemCommand(long orderId, long productId, int quantity) -> {
                var orderItemRecord = orderService.addItem(orderId, productId, quantity);
                return Optional.of(orderItemRecord.id());
            }
            case OrderCommand.UpdateQuantityCommand(long orderItemId, int quantity) -> {
                orderService.updateQuantity(orderItemId, quantity);
                return Optional.empty();
            }
        }
    }
}
