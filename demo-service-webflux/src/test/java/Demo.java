import com.demo.domain.Order;
import org.reactivestreams.Subscription;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
//        testWebClientPost();
//        testWebClientGet();
        testFlux();
    }

    static void testWebClientGet(){
        WebClient orderClient = WebClient.create("http://127.0.0.1:8081");

        List<Order> orderList = orderClient.get()
                .uri("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Order.class)
                .log()
                .collectList()
                .block();
        System.out.println(orderList);
    }

    static void testWebClientPost(){
        WebClient orderClient = WebClient.create("http://127.0.0.1:8081");

        Order order1 = new Order(20L, 1L, new BigDecimal("109"));
        Order order2 = new Order(21L, 1L, new BigDecimal("109"));

        orderClient.post()
                .uri("/orders")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(Flux.fromArray(new Order[]{order1, order2}), Order.class)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(throwable -> Mono.empty());
    }

    static void testFlux() throws Exception {
        long begin = System.currentTimeMillis();
        Flux
                .interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 30)
                        return "tick " + input;

                    try {
                        Thread.sleep(500 * 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(System.currentTimeMillis() - begin);

                    throw new RuntimeException("boom");
                })
                .onErrorReturn("Uh oh")
//                .timeout(Duration.ofMillis(300))
//                .subscribe(
//                        value -> System.out.println(value),
//                        error -> System.out.println("error:\n"+error)
//                )
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(String value) {
                        System.out.println(value);
                        //use delay to simulation work time
                        try {
                            Thread.sleep(300 * 1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        request(1);
                    }
                });

        Thread.sleep(1000 * 15);
    }
}
