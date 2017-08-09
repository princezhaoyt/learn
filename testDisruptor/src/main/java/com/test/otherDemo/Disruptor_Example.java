package com.test.otherDemo;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zyt on 2017/6/15.
 */
public class Disruptor_Example {
    private static final int BUFFER_SIZE = 512 * 512;
    private static RingBuffer<ValueEvent> ringBuffer;
    private static Disruptor<ValueEvent> disruptor;
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    private static final int TIMES = 1000000;

    private static final int THREAD_NUMS = 4;


    private static void init(){
        disruptor = new Disruptor<ValueEvent>(
                ValueEvent.EVENT_FACTORY,
                BUFFER_SIZE,
                SERVICE,
                //ProducerType.MULTI,
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
                //new TimeoutBlockingWaitStrategy(1000, TimeUnit.MINUTES)
        );
    }

    private static void handleEventsWith(EventHandler[] eventHandlers){
        disruptor.handleEventsWith(eventHandlers);
        //disruptor.handleEventsWithWorkerPool(eventHandlers);
    }

    private static void start(){
        ringBuffer = disruptor.start();
    }

    private static void addEvent(ValueEvent valueEvent){
        if(hasCapacity()){
            System.out.println("disruptor : ringBuffer 剩余量低于 10%");
        } else {
            long seq = ringBuffer.next();
            ValueEvent currentValueEvent = ringBuffer.get(seq);
            currentValueEvent.setValue(valueEvent.getValue());
            ringBuffer.publish(seq);
        }
    }

    private static void shutdown(){
        disruptor.shutdown();
        SERVICE.shutdown();
    }

    private static boolean hasCapacity(){
        return (ringBuffer.remainingCapacity() < ringBuffer.getBufferSize() * 0.1);
    }


    public static void test(){
        try {
            concurrentPerformanceTest(THREAD_NUMS, new Runnable() {
                ValueEvent valueEvent = new ValueEvent();
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    for(int i = 0; i < TIMES; i++) {
                        valueEvent.setValue(Thread.currentThread().toString() + UUID.randomUUID().toString());
                        addEvent(valueEvent);
                    }
                    long endTime = System.currentTimeMillis();
                    System.out.println(  "spend :" + (endTime - startTime) + "ms");
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void concurrentPerformanceTest(int threadNum, Runnable runnableApi) throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            pool.execute(runnableApi);
        }
        pool.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println( " call " + TIMES + " times , spend :" + (endTime - startTime) + "ms");
    }
    public static void main(String[] args){
        String orgText = "wa.=Jm&amp;8";
        String text = StringEscapeUtils.unescapeJava(orgText);
        System.out.println("after change:" + text);
        /*int test = 37760459;

        init();
        *//*handleEventsWith(
                new EventHandler[]{
                    new DeliveryReportEventHandler(1),new DeliveryReportEventHandler(2),new DeliveryReportEventHandler(3)
                }
        );*//*
        start();
        long startTime = System.currentTimeMillis();
        test();
         *//*for(int i = 0; i < 10000000; i++){
            ValueEvent valueEvent = new ValueEvent();
            valueEvent.setValue(UUID.randomUUID().toString());
            addEvent(valueEvent);
        }*//*
        shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("spend times :" + (endTime - startTime));*/
    }
}
