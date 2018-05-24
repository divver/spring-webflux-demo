import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Created by divver on 2018/5/24.
 */
public class TestUsing {

  public static void main(String[] args) throws Exception {
        testUsing();
  }

  static int resouce = 1;
  static void testUsing(){
    Mono.using(()-> {return resouce++;}, (d) -> {return Mono.just(resouce);}, (d) -> {resouce = 0;})
        .subscribe(System.out::println);

    Mono.never();

    Mono.error(new RuntimeException())
        .onErrorReturn(1)
        .subscribe(System.out::println);

    Mono.justOrEmpty(Optional.of(3))
        .subscribe(System.out::println);

    Mono.justOrEmpty(Optional.ofNullable(null))
        .defaultIfEmpty(4)
        .subscribe(System.out::println);
  }
}
