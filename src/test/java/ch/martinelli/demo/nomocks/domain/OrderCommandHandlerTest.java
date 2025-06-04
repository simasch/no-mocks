package ch.martinelli.demo.nomocks.domain;

import ch.martinelli.demo.nomocks.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderCommandHandlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCommandHandlerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_purchase_order_with_one_item() throws Exception {
        var stopWatch = new StopWatch();
        stopWatch.start();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var createOrderJson = """
                {
                    "@type": "CreateOrderCommand",
                    "customerId":  1
                }""";

        var createOrderRequest = new HttpEntity<>(createOrderJson, headers);
        var createOrderResponse = restTemplate.postForEntity(
                "/commands/order", 
                createOrderRequest, 
                String.class);

        // Verify response status is OK (200)
        assert createOrderResponse.getStatusCode().is2xxSuccessful();
        var orderId = createOrderResponse.getBody();

        stopWatch.stop();
        LOGGER.info("Create order took {} ms", stopWatch.getTotalTimeMillis());

        stopWatch.start();

        var addOrderItemJson = """
                {
                    "@type": "AddOrderItemCommand",
                    "orderId": %s,
                    "productId": 1,
                    "quantity": 1
                }""".formatted(orderId);

        var addOrderItemRequest = new HttpEntity<>(addOrderItemJson, headers);
        var addOrderItemResponse = restTemplate.postForEntity(
                "/commands/order", 
                addOrderItemRequest, 
                String.class);

        // Verify response status is OK (200)
        assert addOrderItemResponse.getStatusCode().is2xxSuccessful();
        var orderItemId = addOrderItemResponse.getBody();

        var updateQuantityJson = """
                {
                    "@type": "UpdateQuantityCommand",
                    "orderItemId": %s,
                    "quantity": 1
                }""".formatted(orderItemId);

        var updateQuantityRequest = new HttpEntity<>(updateQuantityJson, headers);
        var updateQuantityResponse = restTemplate.postForEntity(
                "/commands/order", 
                updateQuantityRequest, 
                String.class);

        // Verify response status is OK (200)
        assert updateQuantityResponse.getStatusCode().is2xxSuccessful();

        stopWatch.stop();
        LOGGER.info("Add order item took {} ms", stopWatch.getTotalTimeMillis());
    }
}
