import reactor.core.publisher.Flux;

import java.time.Duration;

public class TestConcatMap {
    public static void main(String[] args){
        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);

        System.out.println("-------------------");

        Flux.just(5, 10)
                .flatMapSequential(x -> Flux.interval(Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);

        System.out.println("-------------------");

        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }
}
