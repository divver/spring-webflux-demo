import org.assertj.core.util.Lists;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncDemo {
    public static void main(String[] args){
        testFuture();
//        testReactive();
    }

    private static CompletableFuture<List<String>> ids(){
        CompletableFuture<List<String>> ids = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Lists.newArrayList("1", "2", "3");
        });
        return ids;
    }

    private static CompletableFuture<String> nameTask(String id){
        CompletableFuture<String> nameTask = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "name" + id;
        });
        return nameTask;
    }

    private static CompletableFuture<String> statTask(String id){
        CompletableFuture<String> statTask = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "stat" + id;
        });
        return statTask;
    }

    public static void testFuture(){
        CompletableFuture<List<String>> ids = ids();
        CompletableFuture<List<String>> result = ids.thenComposeAsync(l -> {
            Stream<CompletableFuture<String>> zip =
                    l.stream().map(id -> {
                        CompletableFuture<String> nameTask = nameTask(id);
                        CompletableFuture<String> statTask = statTask(id);
                        return nameTask.thenCombineAsync(statTask, (name, stat) -> name + " has " + stat);
                    });
            List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList());
            CompletableFuture<String>[] combinationArray = combinationList.toArray(new CompletableFuture[combinationList.size()]);

            CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray);
            return allDone.thenApply(
                    v -> combinationList.stream().map(CompletableFuture::join).collect(Collectors.toList())
            );
        });

        List<String> results = result.join();
        System.out.println(results);
    }

    public static void testReactive(){
        Flux<String> ids = Flux.just("1", "2", "3").delayElements(Duration.ofMillis(50));
        Flux<String> combinations =
                ids.flatMap(id -> {
                    Mono<String> nameTask = Mono.fromFuture(nameTask(id));
                    Mono<String> statTask = Mono.fromFuture(statTask(id));
                    return nameTask.zipWith(statTask, (name, stat) -> name + " has " + stat);
                });
        Mono<List<String>> result = combinations.collectList();
        List<String> results = result.block();
        System.out.println(results);

//        combinations.subscribe(System.out::println);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
