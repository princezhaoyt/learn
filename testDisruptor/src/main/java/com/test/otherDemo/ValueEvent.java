package com.test.otherDemo;

import com.lmax.disruptor.EventFactory;

/**
 * Created by zyt on 2017/6/15.
 */
public class ValueEvent {
    private String value;//模拟任务数据

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //定义生成的事件对象，注册到创建 Disruptor 对象
    public final static EventFactory<ValueEvent> EVENT_FACTORY
            = new EventFactory<ValueEvent>() {
        public ValueEvent newInstance() {
            return new ValueEvent();
        }
    };
}
