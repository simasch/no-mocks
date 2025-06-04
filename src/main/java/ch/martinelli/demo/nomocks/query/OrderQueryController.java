package ch.martinelli.demo.nomocks.query;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    List<PurchaseOrder> getCustomersWithOrders(@RequestParam(required = false) String firstName,
                                               @RequestParam(required = false) String lastName,
                                               @RequestParam int offset, @RequestParam int limit) {
        return orderQueryRepository.findOrders(firstName, lastName, offset, limit);
    }
}
