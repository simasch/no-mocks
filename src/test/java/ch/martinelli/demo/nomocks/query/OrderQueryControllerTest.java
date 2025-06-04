package ch.martinelli.demo.nomocks.query;

import ch.martinelli.demo.nomocks.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderQueryControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderQueryControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void get_orders() {
        var stopWatch = new StopWatch();
        stopWatch.start();

        var response = restTemplate.getForEntity("/orders?offset=0&limit=500", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        stopWatch.stop();
        LOGGER.info("Test took {} ms", stopWatch.getTotalTimeMillis());
    }
}
