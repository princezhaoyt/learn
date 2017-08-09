package com.test;

import com.test.domain.Person;
import com.test.helper.PersonHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zyt on 2017/6/14.
 */
public class Application {

    private static final int TIMES = 1000;

    private static final int THREAD_NUMS = 4;

    public static void test(){
        PersonHelper.start();
        try {
            concurrentPerformanceTest(THREAD_NUMS, new Runnable() {
                @Override
                public void run() {
                    Person person = new Person(Thread.currentThread().toString(), 30 , "男", "1234566");
                    PersonHelper.produce(person);
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
        //test();
        PersonHelper.start();
        for(int i = 0; i < 2000; i++){
            Person person = new Person("zhaoyating"+i, i , "男", "1234566"+i);
            PersonHelper.produce(person);
        }
    }
}
