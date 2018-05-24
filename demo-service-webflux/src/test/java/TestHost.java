import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.Arrays;

public class TestHost {
    public static void main(String[] args){
//        testCold();

        testHost();
    }

    public static void testCold(){
        Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
//                .doOnNext(System.out::println)
                .filter(s -> s.startsWith("o"))
                .map(String::toUpperCase);

        source.subscribe(d -> System.out.println("Subscriber 1: "+d));
        source.subscribe(d -> System.out.println("Subscriber 2: "+d));
    }

    public static void testHost(){
        UnicastProcessor<String> hotSource = UnicastProcessor.create();

        Flux<String> hotFlux = hotSource.publish()
//                .autoConnect()// host
                .autoConnect(2)// >2 host, else cold
                .map(String::toUpperCase);

        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));

        hotSource.onNext("blue");
        hotSource.onNext("green");

        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));

        hotSource.onNext("orange");
        hotSource.onNext("purple");

        hotFlux.subscribe(d -> System.out.println("Subscriber 3 to Hot Source: "+d));

        hotSource.onNext("red");
        hotSource.onNext("yellow");

        hotSource.onComplete();
    }
}
