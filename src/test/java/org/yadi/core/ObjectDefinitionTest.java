package org.yadi.core;
/*
Copyright 2014 Julian Exenberger

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by julian3 on 2014/05/01.
 */
public class ObjectDefinitionTest {


    public static final String HELLLOOOO = "helllloooo";

    @Test
    public void testDefine() throws Exception {

        ObjectDefinition<MyObject> objectDefinition = new ObjectDefinition<>();

        objectDefinition.type(MyObject.class)
                .named("MyObjectBean")
                .constructorVal("hello world")
                .createWith(MyObject::CREATE)
                .destroyWith(MyObject::DESTROY)
                .initWith(MyObject::afterPropertiesSet)
                .set(MyObject::setA, "hello world")
                .bind(MyObject::setC, ()-> HELLLOOOO)
                .bind(MyObject::setB, MyObject::aValue);

        MyObject result = objectDefinition.create();
        assertNotNull(result);
        assertEquals("hello world", result.getA());
        assertEquals(MyObject.aValue(), result.getB());
        assertEquals(HELLLOOOO, result.getC());
        assertTrue(result.isAfterProps());




    }

    @Test
    public void testConstruct() throws Exception {
        ObjectDefinition<MyObject> objectDefinition = new ObjectDefinition<>();

        objectDefinition
                .type(MyObject.class)
                .construct((args) -> {
                    args
                            .add("hello")
                            .add(1)
                            .add("world");
                });

        MyObject withConstructor = objectDefinition.create();
        assertNotNull(withConstructor);
        assertEquals("hello", withConstructor.getA());
        assertEquals(1, withConstructor.getB());
        assertEquals("world", withConstructor.getC());


    }
}
