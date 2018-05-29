import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.UnicastProcessor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestProcessor {
    public static void main(String[] args){
//        test_direct_DirectProcessor();
        test_direct_UnicastProcessor();
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

    public static void test_direct_UnicastProcessor(){// 内置一个缓存来处理背压
        Queue queue = new LinkedBlockingQueue(5);
        UnicastProcessor unicastProcessor = UnicastProcessor.create(queue,
                (v) -> System.out.println("overflow:" + v),
                () -> System.out.println("end"));
        unicastProcessor.subscribe((v) -> {
            System.out.println(v);
//            int val = Integer.valueOf(v.toString());
//            if(val % 6 == 0){
//                try {
//                    Thread.sleep(1000 * 3);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });

        FluxSink sink = unicastProcessor.sink();
        for (int i = 0; i < 100; i++) {
            sink.next(i);
        }
        sink.complete();
    }


    // 可以用sink来推送数据, 也可以订阅到一个上游发布者来同步的生成数据
    public static void test_direct_EmmiterProcessor(){

    }

    public static void test_direct_ReplayProcessor(){

    }


    // 可以将从多个上游发布者获得的数据推送下去
    public static void test_direct_WorkQueueProcessor(){

    }

    public static void test_direct_TopicProcessor(){

    }
}
