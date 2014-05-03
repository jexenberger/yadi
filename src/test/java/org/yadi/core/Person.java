package org.yadi.core;

/**
 * Created by julian3 on 2014/05/04.
 */
public class Person {

    String name;
    String surname;
    String fullName;
    Person spouse;


    public Person() {
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
}
