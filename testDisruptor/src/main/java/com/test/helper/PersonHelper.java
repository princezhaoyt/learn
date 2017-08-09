package com.test.helper;

import com.lmax.disruptor.*;
import com.test.domain.Person;
import com.test.event.PersonEvent;
import com.test.handler.PersonEventHandler;

/**
 * Created by zyt on 2017/6/14.
 */
public class PersonHelper {
    public static PersonHelper personHelperInstance;

    private static boolean inited = false;

    private static final int BUFFER_SIZE = 256;

    private RingBuffer<PersonEvent> ringBuffer;

    private SequenceBarrier sequenceBarrier;

    private PersonEventHandler personEventHandler;

    private BatchEventProcessor<PersonEvent> batchEventProcessor;

    public PersonHelper(){
       /* ringBuffer = new RingBuffer<PersonEvent>(PersonEvent.EVENT_FACTORY,
                new SingleThreadedClaimStrategy(BUFFER_SIZE), new YieldingWaitStrategy());
        sequenceBarrier = ringBuffer.newBarrier();
        personEventHandler = new PersonEventHandler();
        batchEventProcessor = new BatchEventProcessor<PersonEvent>(ringBuffer, sequenceBarrier, personEventHandler);
        ringBuffer.setGatingSequences(batchEventProcessor.getSequence());*/
    }

    public static void start(){
        personHelperInstance = new PersonHelper();
        Thread thread = new Thread(personHelperInstance.batchEventProcessor);
        thread.start();
        inited = true;
    }

    public static void shutdown(){
        if(!inited){
            throw new RuntimeException("PersonHelper has not bean initialized!");
        } else {
            personHelperInstance.doHalt();
        }
    }

    private void doHalt(){
        batchEventProcessor.halt();
    }

    private void doProduce(Person person){
        long sequence = ringBuffer.next();
        ringBuffer.get(sequence).setPerson(person);
        ringBuffer.publish(sequence);
    }

    public static void produce(Person person){
        if(!inited){
            throw new RuntimeException("PersonHelper has not bean initialized!");
        }else{
            personHelperInstance.doProduce(person);
        }
    }
}
