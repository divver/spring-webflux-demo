import reactor.core.publisher.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestProcessor {
    public static void main(String[] args) throws InterruptedException {
//        test_direct_DirectProcessor();
//        test_direct_UnicastProcessor();

//        test_sync_EmitterProcessor();
//        test_sync_ReplayProcessor();

//        test_async_TopicProcessor();
        test_async_WorkQueueProcessor();
    }

    // 只能用sink来推送数据
    public static void test_direct_DirectProcessor(){
        DirectProcessor directProcessor = DirectProcessor.create();
        directProcessor.subscribe(System.out::println);

        FluxSink sink = directProcessor.sink();
        sink.next(1);
        sink.next(2);
        sink.next(3);
        sink.complete();
    }

    public static void test_direct_UnicastProcessor() throws InterruptedException {// 内置一个缓存来处理背压
        Queue queue = new LinkedBlockingQueue(5);
        UnicastProcessor unicastProcessor = UnicastProcessor.create(queue,
                (v) -> {
                    System.out.println("overflow:" + v);
                },
                () -> System.out.println("end"));

        FluxSink sink = unicastProcessor.sink();
        for (int i = 0; i < 5; i++) { // test 6
            sink.next(i);
        }

        unicastProcessor.subscribe(System.out::println);
//        unicastProcessor.subscribe(System.out::println);// only enable one subscriber

        for (int i = 0; i < 10; i++) {
            sink.next(i);
        }
        sink.complete();

        Thread.sleep(1000 * 3);
    }


    // 可以用sink来推送数据, 也可以订阅到一个上游发布者来同步的生成数据; 允许多个订阅者
    public static void test_sync_EmitterProcessor() throws InterruptedException {
        DirectProcessor directProcessor = DirectProcessor.create();
        FluxSink sink = directProcessor.sink();

        EmitterProcessor emitterProcessor = EmitterProcessor.create(5, true);
        directProcessor.subscribe(emitterProcessor);

        emitterProcessor.subscribe(System.out::println);
        sink.next(1);
        sink.next(2);
        sink.next(3);

        emitterProcessor.subscribe(System.out::println);//后来的订阅者只能收到其订阅后发出的元素
        sink.next(4);
        sink.next(5);
        sink.next(6);

        Thread.sleep(1000 * 1);
    }

    public static void test_sync_ReplayProcessor() throws InterruptedException {
        ReplayProcessor replayProcessor = ReplayProcessor.create(5, true);

        DirectProcessor directProcessor = DirectProcessor.create();
        FluxSink sink = directProcessor.sink();
        directProcessor.subscribe(replayProcessor);

        DirectProcessor directProcessor1 = DirectProcessor.create();
        FluxSink sink1 = directProcessor.sink();
        directProcessor1.subscribe(replayProcessor);

        replayProcessor.subscribe(System.out::println);

        sink.next(1);
        sink.next(2);
        sink.next(3);

        replayProcessor.subscribe(System.out::println);// 后来的订阅者也会收到重发的元素

        sink.next(4);
        sink.next(5);
        sink.next(6);

        sink1.next(11);
        sink1.next(12);
        sink1.next(13);

        Thread.sleep(1000 * 1);
    }


    // 可以将从多个上游发布者获得的数据推送下去; 异步
    public static void test_async_TopicProcessor() throws InterruptedException {
        DirectProcessor directProcessor = DirectProcessor.create();
        FluxSink sink = directProcessor.sink();

        TopicProcessor topicProcessor = TopicProcessor.builder().share(true).build();
        directProcessor.subscribe(topicProcessor);

        topicProcessor.subscribe(System.out::println);
        sink.next(1);
        sink.next(2);
        sink.next(3);

        topicProcessor.subscribe(System.out::println);//后来的订阅者只能收到其订阅后发出的元素
        sink.next(4);
        sink.next(5);
        sink.next(6);

        topicProcessor.onNext(21);
        topicProcessor.onNext(22);
        topicProcessor.onNext(23);

        Thread.sleep(1000 * 1);
    }

    public static void test_async_WorkQueueProcessor() throws InterruptedException {
        DirectProcessor directProcessor = DirectProcessor.create();
        FluxSink sink = directProcessor.sink();

        WorkQueueProcessor workQueueProcessor = WorkQueueProcessor.builder().share(true).build();// for multi-threaded publisher
        directProcessor.subscribe(workQueueProcessor);

        // 对多个订阅者round-ribbon发送元素
        workQueueProcessor.subscribe(v -> System.out.println("1:" + v));
        workQueueProcessor.subscribe(v -> System.out.println("2:" + v));
        workQueueProcessor.subscribe(v -> System.out.println("3:" + v));

        Thread.sleep(100);

        sink.next(1);
        sink.next(2);
        sink.next(3);
        sink.next(4);
        sink.next(5);
        sink.next(6);

        workQueueProcessor.onNext(21);
        workQueueProcessor.onNext(22);
        workQueueProcessor.onNext(23);

        Thread.sleep(1000 * 1);

    }

}
