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
package org.yadi.core;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Created by julian3 on 2014/05/03.
 */
public class SingletonScopeTest {

    private SingletonScope singletonScope;
    private ObjectDefinition<MyObject> objectDefinition;

    @Before
    public void setup() throws Exception {
        singletonScope = new SingletonScope();
        objectDefinition = new ObjectDefinition<MyObject>().type(MyObject.class).set(MyObject::setC, "hello").boundTo(Runnable.class);
        singletonScope.addObjectDefinition(objectDefinition);
        singletonScope.initialise();
    }

    public void after() {
        singletonScope.terminate();
    }

    @Test
    public void testResolveByName() throws Exception {
        Optional<MyObject> result = singletonScope.create(objectDefinition);
        assertNotNull(result);
        assertNotNull(result.get());
        assertEquals("hello", result.get().getC());

        //test singletonness
        Optional<MyObject> other = singletonScope.create(objectDefinition);
        assertSame(result.get(), other.get());

    }


    @Test
    public void testGetIdentifier() throws Exception {

        assertEquals(SingletonScope.SINGLETON, singletonScope.getIdentifier());
    }
}
