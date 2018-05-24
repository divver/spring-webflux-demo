import reactor.core.publisher.Flux;

import java.time.Duration;

public class TestDelay {
    public static void main(String[] args) throws Exception {
//        testDelayElements();

//        testDelaySequence();

        testDelayElements_product_faster();

//        testDelaySequence_product_faster();
    }

    public static void testDelayElements() throws Exception {
        Flux.interval(Duration.ofMillis(10))
                .delayElements(Duration.ofMillis(5))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(100 * 2);
    }

    public static void testDelaySequence() throws Exception {
        Flux.interval(Duration.ofMillis(10))
                .delaySequence(Duration.ofMillis(5))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(100 * 2);
    }

    public static void testDelayElements_product_faster() throws Exception {
        Flux.interval(Duration.ofMillis(5))
                .cache() // must use cache
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(100 * 2);
    }

    public static void testDelaySequence_product_faster() throws Exception {
        Flux.interval(Duration.ofMillis(5))
//                .cache()
                .delaySequence(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(100 * 2);
    }
}
