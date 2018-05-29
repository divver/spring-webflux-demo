import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.function.Tuples;

import java.time.Duration;

public class TestTry {
    public static void main(String[] args) throws InterruptedException {
//        testTry();

        testTryWhen();
    }

    public static void testTry() throws InterruptedException {
        Flux.interval(Duration.ofMillis(100))
                .map(v -> {
                    if(v >30)
                        throw new RuntimeException();
                    return v;
                })
                .retry()
                .subscribe(System.out::println);

        Thread.sleep(1000 * 5);
    }

    public static void testTryWhen() throws InterruptedException {
        Flux.interval(Duration.ofMillis(100))
                .map(v -> {
                    if(v >30)
                        throw new RuntimeException();
                    return v;
                })
                .retryWhen(errorCurrentAttempt -> errorCurrentAttempt
                        .flatMap(e -> Mono.subscriberContext().map(ctx -> Tuples.of(e, ctx)))
                        .flatMap(t2 -> {
                            Throwable lastError = t2.getT1();
                            Context ctx = t2.getT2();
                            int rl = ctx.getOrDefault("retriesLeft", 3);
                            if (rl > 0) {
                                // /!\ THE ctx.put HERE IS THE ESSENTIAL PART /!\
                                return Mono.just(ctx.put("retriesLeft", rl - 1).put("lastError", lastError));
                            } else {
                                throw new IllegalStateException("retries exhausted", lastError);
                            }
                        }))
                .onErrorResume(e -> Flux.just(-1L))
                .subscribe(System.out::println);

        Thread.sleep(1000 * 30);
    }
}
