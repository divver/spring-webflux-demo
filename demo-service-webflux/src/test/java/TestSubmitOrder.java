import com.demo.domain.Order;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

public class TestSubmitOrder {
    public static void main(String[] args){
        WebClient orderClient = WebClient.create("http://127.0.0.1:8082");

        Order order1 = new Order(20L, 1L, new BigDecimal("109"));
        Order order2 = new Order(21L, 1L, new BigDecimal("109"));

        orderClient.get()
                .uri("/orders")
                .retrieve()
                .bodyToFlux(Order.class)
                .log();

//        orderClient.post()
//                .uri("/orders")
//                .contentType(MediaType.APPLICATION_STREAM_JSON)
//                .body(Flux.fromArray(new Order[]{order1, order2}), Order.class)
//                .retrieve()
//                .bodyToMono(Void.class);

    }
}
