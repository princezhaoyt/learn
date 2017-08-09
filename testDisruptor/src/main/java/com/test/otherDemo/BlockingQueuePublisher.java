package com.test.otherDemo;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zyt on 2017/7/7.
 */
public class BlockingQueuePublisher{
    private static final int MAX_SIZE = 50000000;
    private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(MAX_SIZE);

    private static final int THREAD_NUMS = 4;
    private static final int TIMES = 1000000;

    private static void produceParal(){
        try {
            concurrentPerformanceTest(THREAD_NUMS, new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    for(int i = 0; i < TIMES; i++){
                        String putString = "test" + UUID.randomUUID().toString();
                        queue.add(putString);
                       // System.out.println("put String success:" + putString + "==current size :" + queue.size());
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

    private static void produceLinear(int num){
        for(int i = 0; i < num; i++){
            String putString = "test" + UUID.randomUUID().toString();
            queue.add(putString);
           // System.out.println("put String success:" + putString + "==current size :" + queue.size());
        }
    }

    private static void handleParal(){
        try{
            concurrentPerformanceTest(THREAD_NUMS, new Runnable() {
                @Override
                public void run() {
                    try{
                        while(!queue.isEmpty()) {
                            String curStr = queue.take();
                            System.out.println("take out String:" + curStr + "==current size :" + queue.size());
                        }
                    } catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        long startTime = System.currentTimeMillis();
        produceParal();
        //produceLinear(10000000);
        long endTime = System.currentTimeMillis();
        System.out.println("spend times :" + (endTime - startTime));
        //Thread.sleep(5000);
        //handleParal();
    }

    private static void concurrentPerformanceTest(int threadNum, Runnable runnableApi) throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            pool.execute(runnableApi);
        }
        pool.shutdown();
        long endTime = System.currentTimeMillis();
       System.out.println(  "  spend :" + (endTime - startTime) + "ms");
    }
}
