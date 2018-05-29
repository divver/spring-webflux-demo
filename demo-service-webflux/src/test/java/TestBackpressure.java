import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class TestBackpressure {
    public static void main(String[] args) throws InterruptedException {
//        testOnBackpressureBuffer();

//        testOnBackpressureBuffer_onOverflow();

//        testOnBackpressureBuffer_overflow_strategy();

//        testOnBackpressureDrop();

        testOnBackpressureLatest();

//        testOnBackpressureError();
    }

    public static void testOnBackpressureBuffer() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10))
                .onBackpressureBuffer(100)
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 60 * 30);
    }

    public static void testOnBackpressureBuffer_onOverflow() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10))
//                .onBackpressureBuffer(100, (e) -> System.out.println("overflow"+e) )
                .onBackpressureBuffer(Duration.ofMillis(1), 100, (e) -> System.out.println("overflow"+e) ) // ???
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 60 * 30);
    }


    public static void testOnBackpressureBuffer_overflow_strategy() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10))
//                .onBackpressureBuffer(100, BufferOverflowStrategy.DROP_OLDEST)
//                .onBackpressureBuffer(100, (e) -> System.out.println("overflow"+e), BufferOverflowStrategy.DROP_OLDEST)
//                .onBackpressureBuffer(100, BufferOverflowStrategy.DROP_LATEST)
                .onBackpressureBuffer(100, (e) -> System.out.println("overflow"+e), BufferOverflowStrategy.DROP_LATEST)
//                .onBackpressureBuffer(100, BufferOverflowStrategy.ERROR)
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 60 * 30);
    }

    public static void testOnBackpressureDrop() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10))
//                .onBackpressureDrop()
                .onBackpressureDrop((e) -> System.out.println("overflow"+e))
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 60 * 30);
    }

    public static void testOnBackpressureLatest() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10))
                .onBackpressureLatest()
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 60 * 30);
    }

    public static void testOnBackpressureError() throws InterruptedException {
        Flux.interval(Duration.ofMillis(10))
                .onBackpressureError()
                .delayElements(Duration.ofMillis(20))
                .timestamp()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 60 * 30);
    }
}
