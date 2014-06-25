package org.yadi.core;

import org.junit.Test;

import static org.yadi.core.DefaultContainer.create;

/**
 * Created by w1428134 on 2014/06/25.
 */
public class SamplesTest {


    @Test
    public void testSimpleString() throws Exception {

        Container container = create((builder) -> {
            builder.define(String.class)
                    .args("hello world");

            builder.define(MyObject.class)
                    .set(MyObject::setA,"hello world")
                    .initWith(MyObject::start);
        });
        MyObject myString = container.get(MyObject.class);
        System.out.println("An non-empty string: " + myString.get());

    }

    @Test
    public void testSimpleStringNamed() throws Exception {

        Container container = create((builder) -> {
            builder
                    .define(String.class)
                    .named("myString")
                    .args("hello world");
        });
        String myString = container.get("myString");
        System.out.println("An non-empty named string: " + myString);

    }

    @Test
    public void testPersonLambda() throws Exception {

        Container container = create((builder) -> {
            builder
                    .define(Person.class)
                    .set((person, value) -> person.setName(value), "John");
        });
        Person person = container.get(Person.class);
        System.out.println(person.getName());

    }

    @Test
    public void testPersonSetterMethodReference() throws Exception {

        Container container = create((builder) -> {
            builder
                    .define(Person.class)
                    .set(Person::setName, "John");
        });
        Person person = container.get(Person.class);
        System.out.println(person.getName());

    }

    @Test
    public void testInjection() throws Exception {
        Container container = create((builder) -> {
            builder
                    .define(Person.class)
                    .set(Person::setName, "Sarah")
                    .set(Person::setSurname, "Smith")
                    .initWith(Person::setup);

            builder
                    .define(Person.class)
                    .named("jSmith")
                    .set(Person::setName, "John")
                    .set(Person::setSurname, "Smith")
                    .inject(Person::setSpouse, Person.class)
                    .initWith(Person::setup);


        });
        Person person = container.get("jSmith");
        System.out.println(person.getSpouse().getFullName());

    }
}
