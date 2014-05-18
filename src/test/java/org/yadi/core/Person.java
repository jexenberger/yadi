package org.yadi.core;

import java.util.function.Supplier;

/**
 * Created by julian3 on 2014/05/04.
 */
public class Person implements Runnable, Supplier<Person> {

    String name;
    String surname;
    String fullName;
    Person spouse;
    Runnable runningPartner;


    public Person() {
    }

    public Person(Runnable runningPartner) {
        this.runningPartner = runningPartner;
    }

    public Person(Person spouse) {
        this.spouse = spouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFullName() {
        return fullName;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public void setup() {
        this.fullName = this.name + " " + this.surname;
    }

    public Runnable getRunningPartner() {
        return runningPartner;
    }

    @Override
    public void run() {
        System.out.println(getFullName());
    }

    @Override
    public Person get() {
        return this;
    }
}
