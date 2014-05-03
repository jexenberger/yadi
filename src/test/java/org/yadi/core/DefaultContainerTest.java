package org.yadi.core;

import org.junit.Test;

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
                        .addConstructorArg("hello world")
                        .set(MyObject::setB, 200)
                        .initWith(MyObject::afterPropertiesSet);

            builder
                .define(AnObject.class)
                    .named("coolioObject")
                        .inject(AnObject::setOther, "aCoolFunkyObject");

            builder
                .define(Person.class)
                    .named("sSmith")
                    .set(Person::setName, "Sarah")
                    .set(Person::setSurname, "Smith")
                    .initWith(Person::setup);


            builder
                .define(Person.class)
                    .named("jSmith")
                    .set(Person::setName, "John")
                    .set(Person::setSurname, "Smith")
                    .inject(Person::setSpouse, "sSmith")
                    .initWith(Person::setup);

        });

        AnObject result = container.get("coolioObject");
        assertNotNull(result);
        assertNotNull(result.getOther());

        Person bPitt = container.get("jSmith");
        assertNotNull(bPitt);
        assertNotNull(bPitt.getSpouse());
        System.out.println(bPitt.getSpouse().getFullName());

    }
}
