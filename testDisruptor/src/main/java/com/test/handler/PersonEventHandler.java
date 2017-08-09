package com.test.handler;

import com.lmax.disruptor.EventHandler;
import com.test.domain.Person;
import com.test.event.PersonEvent;

/**
 * Created by zyt on 2017/6/14.
 */
public class PersonEventHandler implements EventHandler<PersonEvent> {
    public void onEvent(PersonEvent personEvent, long l, boolean b) throws Exception {
        Person person = personEvent.getPerson();
        System.out.println("Thread:" + Thread.currentThread() +
                ",name = " + person.getName() + " , age = " + person.getAge() +
                ", gender = " + person.getGender() +" , mobile = "+person.getMobile());
    }
}
