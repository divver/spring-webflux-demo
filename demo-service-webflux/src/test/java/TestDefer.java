import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by divver on 2018/5/19.
 */
public class TestDefer {
  public static void main(String[] args){
    testJust();
//    testDefer();
  }

  public static int index = 0;
  public static void testJust(){
    count();

    Mono.just(index)
        .delayElement(Duration.ofMillis(1000))
        .subscribe(v -> {
          System.out.println(v);
        });

  }

  public static void testDefer(){
    count();

    Mono.defer(() -> Mono.just(index))
        .delayElement(Duration.ofMillis(1000))
        .subscribe(v -> {
          System.out.println(v);
        });

  }

  public static void count(){
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.submit(() -> {
      while(index < 200){
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        index ++;
      }
      executor.shutdownNow();
    });
  }

}
