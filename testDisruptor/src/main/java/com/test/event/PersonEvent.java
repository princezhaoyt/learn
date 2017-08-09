package com.test.event;

import com.lmax.disruptor.EventFactory;
import com.test.domain.Person;

/**
 * Created by zyt on 2017/6/14.
 */
public class PersonEvent {
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public static final EventFactory<PersonEvent> EVENT_FACTORY = new EventFactory<PersonEvent>() {
        public PersonEvent newInstance() {
            return new PersonEvent();
        }
    };
}
