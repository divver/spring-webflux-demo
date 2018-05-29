import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TestConcatWith {
    public static void main(String[] args){
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println, System.err::println);

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(-1);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-2);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println, System.err::println);
    }
}
