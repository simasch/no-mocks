package ch.martinelli.demo.nomocks.query;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("orders")
@RestController
class OrderQueryController {

    private final OrderQueryRepository orderQueryRepository;

    OrderQueryController(OrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }

    @GetMapping
    List<PurchaseOrder> getCustomersWithOrders(FindOrders query) {
        return orderQueryRepository.findOrders(query.firstName(), query.lastName(), query.offset(), query.limit());
    }
}
