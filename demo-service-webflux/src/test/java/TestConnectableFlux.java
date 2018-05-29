import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class TestConnectableFlux {
    public static void main(String[] args) throws InterruptedException {
//        testPublish();

//        testReplay();

        testPublish_refCount();
    }

    public static void testPublish() throws InterruptedException {
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(100))
                .publish().autoConnect(1); // 在minSubscribers个订阅者到齐之后, 才自动触发上游开始发出数据

        System.out.println("----------s1---------");
        longFlux.subscribe(v -> System.out.println("1:"+v + "," + System.currentTimeMillis()));

        Thread.sleep(500);

        System.out.println("----------s2---------");
        longFlux.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
                this.s = s;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("2:"+aLong + "," + System.currentTimeMillis());
                if(aLong < 5){
                    this.s.request(1);
                }else if(aLong >= 5 && aLong < 10){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.s.request(1);
                }else{
                    this.s.cancel();
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        Thread.sleep(1000 * 5);
    }

    public static void testReplay() throws InterruptedException {
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(100))
                .replay().autoConnect(1);

        System.out.println("----------s1---------");
        longFlux.subscribe(v -> System.out.println("1:"+v + "," + System.currentTimeMillis()));

        Thread.sleep(500);

        System.out.println("----------s2---------");
        longFlux.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
                this.s = s;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("2:"+aLong + "," + System.currentTimeMillis());
                if(aLong < 5){
                    this.s.request(1);
                }else if(aLong >= 5 && aLong < 10){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.s.request(1);
                }else{
                    this.s.cancel();
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        Thread.sleep(1000 * 5);
    }

    public static void testPublish_refCount() throws InterruptedException {// ???
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(100))
                .publish().refCount(2);

        System.out.println("----------s1---------");
        longFlux.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
                this.s = s;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("1:"+aLong + "," + System.currentTimeMillis());
                if(aLong < 5){
                    this.s.request(1);
                }else if(aLong >= 5 && aLong < 10){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.s.request(1);
                }else{
                    this.s.cancel();
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                this.s.cancel();
            }
        });

        Thread.sleep(500);

        System.out.println("----------s2---------");
        longFlux.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
                this.s = s;
            }

            @Override
            public void onNext(Long aLong) {

                System.out.println("2:"+aLong + "," + System.currentTimeMillis());
                if(aLong < 5){
                    this.s.request(1);
                }else if(aLong >= 5 && aLong < 10){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.s.request(1);
                }else{
                    this.s.cancel();
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                this.s.cancel();
            }
        });

        Thread.sleep(1000 * 4);

        System.out.println("----------s3---------");
        longFlux.subscribe(v -> System.out.println("3:"+v + "," + System.currentTimeMillis()));

        Thread.sleep(1000 * 6);
    }
}
