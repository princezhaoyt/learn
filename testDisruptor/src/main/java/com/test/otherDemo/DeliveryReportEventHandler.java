package com.test.otherDemo;

import com.lmax.disruptor.EventHandler;

/**
 * Created by zyt on 2017/6/15.
 */
public class DeliveryReportEventHandler implements EventHandler<ValueEvent>{
    private int id;
    public DeliveryReportEventHandler(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeliveryReportEventHandler{" +
                "id=" + id +
                '}';
    }


    @Override
    public void onEvent(ValueEvent valueEvent, long l, boolean b) throws Exception {
        Thread.sleep(20);
        System.out.println(this + "\tevent:\t" + valueEvent.getValue()
                + "\tsequence:\t" + l
                + "\tendOfBatch:\t" + b);
    }
}
