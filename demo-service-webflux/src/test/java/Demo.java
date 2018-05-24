import com.demo.domain.Order;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) throws Exception {
//        testWebClientPost();
//        testWebClientGet();
//        testFlux();
//        testFlux2();
//        testFlux3();
//        testMonoSink();
//        testGenerate();
        testDefer();
    }

    static void testDefer(){
        final AtomicInteger intVal = new AtomicInteger(1);

        Mono test1 = Mono.just(intVal.get());
        intVal.addAndGet(1);
        test1.subscribe(val -> System.out.println(val));

        Mono test = Mono.defer(() -> Mono.just(intVal.get()));
        intVal.addAndGet(1);
        test.subscribe(val -> System.out.println(val));

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
                .interval(Duration.ofMillis(800))
                .map(input -> {
                    if (input < 3000000)
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
                            Thread.sleep(100 * 1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        request(1);
                    }
                });

        Thread.sleep(1000 * 25);
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
                .subscribeOn(Schedulers.newElastic("opt"))
                .log()
                .filter(i -> (i % 2) == 0)
                .subscribeOn(Schedulers.newElastic("opt2"))
                .log()
//                .publishOn(Schedulers.newParallel("show"))
               .parallel().runOn(Schedulers.newParallel("show"))
                .subscribe(value -> logger.info("value is {}", value));
    }

    static void testMonoSink() throws InterruptedException {
        Mono.create(sink -> {
            sink.success(1);
        }).subscribe(System.out::println);

        Flux.create(fluxSink -> {
                    fluxSink.next(1);
                    fluxSink.next(2);
                    fluxSink.next(3);
                    fluxSink.next(4);
                },
                FluxSink.OverflowStrategy.BUFFER)
                .onBackpressureBuffer(3) // ???
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    static void testGenerate(){
        Flux<String> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3*i);
                    if (i == 10) sink.complete();
                    return state;
                });
//        flux.log().blockLast();

        Mono<Integer> count = Flux.just(1, 2, 3, 4, 5)
                .parallel().runOn(Schedulers.newParallel("opt"))
//                .reduce(AtomicLong::new, new BiFunction<AtomicLong, Integer, AtomicLong>() {
//                    @Override
//                    public AtomicLong apply(AtomicLong atomicLong, Integer integer) {
//                        logger.info(atomicLong.addAndGet(1) + ", " +integer);
//                        return atomicLong;
//                    }
//                })
                .map(i -> 1)
                .log()
                .reduce((i, j) -> i+j)
                .log();
//                .subscribeOn(Schedulers.elastic())
//                .count();

//                count.subscribe(System.out::println);

        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(
                        s -> Mono.subscriberContext()
                                .map( ctx -> s + " " + ctx.get(key))
                )
                .subscriberContext(ctx -> ctx.put(key, "World"));
//        r.subscribe(System.out::println);

        Flux.range(1, 10)
                .window(5, 3) //overlapping windows
                .concatMap(g -> g.defaultIfEmpty(-1)) //show empty windows as -1
        .subscribe(System.out::println);

    }
}
