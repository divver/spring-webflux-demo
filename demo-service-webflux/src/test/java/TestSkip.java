import reactor.core.publisher.Flux;

import java.time.Duration;

public class TestSkip {
    public static void main(String[] args) throws Exception {
//        testSkipUntil();

//        testSkipUntilOther();

        testSkipWhile();
    }

    public static void testSkipUntil() throws Exception {// 满足时停止跳过
        Flux.interval(Duration.ofMillis(100))
//                .skipUntil((v) -> v < 5)
//                .skipUntil((v) -> v != 5)
                .skipUntil((v) -> v == 5)
//                .skipUntil((v) -> v > 5)
                .subscribe(System.out::println);
        Thread.sleep(1000 * 3);
    }

    public static void testSkipUntilOther() throws Exception {
        Flux.interval(Duration.ofMillis(100))
                .skipUntilOther(Flux.just(1).delayElements(Duration.ofMillis(500)))
                .subscribe(System.out::println);
        Thread.sleep(1000 * 3);
    }


    public static void testSkipWhile() throws Exception {// 不满足时停止跳过
        Flux.interval(Duration.ofMillis(100))
                .skipWhile((v) -> v < 5)
//                .skipWhile((v) -> v != 5)
//                .skipWhile((v) -> v == 5)
//                .skipWhile((v) -> v > 5)
                .subscribe(System.out::println);
        Thread.sleep(1000 * 3);
    }
}
