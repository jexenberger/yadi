package org.yadi.core;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertNotNull;
import static org.yadi.core.DefaultContainer.create;

/**
 * Created by julian3 on 2014/05/03.
 */
public class DefaultContainerTest {


    @Test
    public void testCreate() throws Exception {

        Container container = create((builder) -> {
            builder
                .define(MyObject.class)
                    .named("aCoolFunkyObject")
                        .args("hello world")
                        .set(MyObject::setB, 200)
                        .initWith(MyObject::afterPropertiesSet);

            builder
                .define(MyObject.class)
                    .named("aCoolFunkyObject")
                        .boundTo(Runnable.class)
                        .args("hello world")
                        .set(MyObject::setB, 200)
                        .initWith(MyObject::afterPropertiesSet);

            builder
                .define(MyObject.class)
                    .named("aCoolFunkyObject")
                        .boundTo(Supplier.class)
                        .args("hello world")
                        .set(MyObject::setB, 200)
                        .initWith(MyObject::afterPropertiesSet);

            builder
                .define(AnObject.class)
                    .named("coolioObject")
                        .inject(AnObject::setOther, MyObject.class);

            builder
                .define(Person.class)
                    .named("sSmith")
                    .set(Person::setName, "Sarah")
                    .set(Person::setSurname, "Smith")
                    .initWith(Person::setup);




        });


        Runnable runnable = container.get(Runnable.class);
        runnable.run();

        Supplier<Person> supplier = container.get(Supplier.class);
        assertNotNull(supplier.get());

    }
}
