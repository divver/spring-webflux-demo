import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class TestCombineLatest {
    public static void main(String[] args){
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.ofMillis(100)),
                Flux.interval(Duration.ofMillis(100))
        ).toStream().forEach(System.out::println);
    }
}
