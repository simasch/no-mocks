package ch.martinelli.demo.nomocks.command;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class OrderCommandHandler {

    private final OrderService orderService;

    OrderCommandHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    Optional<?> handle(OrderCommand orderCommand) {
        switch (orderCommand) {
            case OrderCommand.CreateOrderCommand(long customerId) -> {
                var purchaseOrder = orderService.createOrder(customerId);
                return Optional.of(purchaseOrder.getId());
            }
            case OrderCommand.AddOrderItemCommand(long orderId, long productId, int quantity) -> {
                var orderItemRecord = orderService.addItem(orderId, productId, quantity);
                return Optional.of(orderItemRecord.getId());
            }
            case OrderCommand.UpdateQuantityCommand(long orderItemId, int quantity) -> {
                orderService.updateQuantity(orderItemId, quantity);
                return Optional.empty();
            }
        }
    }
}
