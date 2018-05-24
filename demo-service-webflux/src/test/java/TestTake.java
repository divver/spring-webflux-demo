import reactor.core.publisher.Flux;

import java.time.Duration;

public class TestTake {
    public static void main(String[] args) throws Exception {
        testTakeUntil();

//        testTakeUntilOther();

//        testTakeWhile();
    }

    public static void testTakeUntil() throws Exception {// 满足时停止
        Flux.interval(Duration.ofMillis(100))
//                .takeUntil((v) -> v > 5)
//                .takeUntil((v) -> v < 5)
//                .takeUntil((v) -> v == 5)
                .takeUntil((v) -> v != 5)
                .subscribe(System.out::println);
        Thread.sleep(1000 * 3);
    }

    public static void testTakeUntilOther() throws Exception {
        Flux.interval(Duration.ofMillis(100))
                .takeUntilOther(Flux.just(1).delayElements(Duration.ofMillis(500)))
                .subscribe(System.out::println);
        Thread.sleep(1000 * 3);
    }


    public static void testTakeWhile() throws Exception {// 不满足时停止
        Flux.interval(Duration.ofMillis(100))
                .takeWhile((v) -> v != 5)
//                .takeWhile((v) -> v < 5)
//                .takeWhile((v) -> v == 5)
//                .takeWhile((v) -> v > 5)
                .subscribe(System.out::println);
        Thread.sleep(1000 * 3);
    }
}
