import com.demo.domain.Order;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) throws Exception {
//        testWebClientPost();
//        testWebClientGet();
//        testFlux();
//        testFlux2();
        testFlux3();
//        testMonoSink();
    }

    static void testWebClientGet() throws Exception{
        WebClient orderClient = WebClient.create("http://127.0.0.1:8081");

        orderClient.get()
                .uri("/orders")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Order.class)
                .log()
                .subscribe(order -> System.out.println(order));


        Thread.sleep(1000);

        System.out.println("====================================================");

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

    static void testFlux2(){
       Flux.just(1, 2, 3, 4, 5)
               .log()
               .map(i -> i * i)
               .log()
               .filter(i -> (i % 2) == 0)
               .log()
//               .publishOn(Schedulers.newParallel("show"))
//               .parallel().runOn(Schedulers.newParallel("show"))
               .subscribe(value -> logger.info("value is {}", value));
    }

    static void testFlux3(){
        Flux.just(1, 2, 3, 4, 5)
                .log()
                .map(i -> i * i)
                .log()
                .filter(i -> (i % 2) == 0)
                .log()
                .subscribeOn(Schedulers.newElastic("opt"))
                .publishOn(Schedulers.newParallel("show"))
//               .parallel().runOn(Schedulers.newParallel("show"))
                .subscribe(value -> logger.info("value is {}", value));
    }

    static void testMonoSink(){
        Mono.create(sink -> {
            sink.success(1);
        }).subscribe(System.out::println);
    }
}
