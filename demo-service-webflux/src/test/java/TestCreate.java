import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

public class TestCreate {
    public static void main(String[] args) throws Exception {
//        testCreate_Mono();

        testCreate_Flux();
    }

    static void testCreate_Mono() {
        Mono.create(sink -> {
                    sink.success(1);
                })
                .subscribe(System.out::println);
    }

    static void testCreate_Flux() throws InterruptedException {
        Flux.create(
                fluxSink -> {
                    fluxSink.next(1);
                    fluxSink.next(2);
                    fluxSink.next(3);
//                    fluxSink.complete();
                    fluxSink.next(4);
                }, FluxSink.OverflowStrategy.BUFFER)
//                .onBackpressureBuffer(3)
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

}
