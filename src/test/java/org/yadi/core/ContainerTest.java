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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by julian3 on 2014/05/03.
 */
public class ContainerTest extends BasicContainer {

    public static final String OBJECT_NAME = MyObject.class.getName();

    @Before
    public void before() {
        this.init();
    }

    @After
    public void after() {
        this.close();
    }


    @Test
    public void testInit() throws Exception {
        assertNotNull(getDefinitions());
        assertEquals(1, getDefinitions().size());
        assertThat(getDefinitions().iterator().next().getName(),equalTo(OBJECT_NAME));


    }


    @Override
    public void build() {
        define(MyObject.class)
                .set(MyObject::setC, "hello world")
                .boundTo(Runnable.class)
                .destroyWith(MyObject::DESTROY);
    }



    @Test
    public void testGetByType() throws Exception {

        Optional<Runnable> objectOptional = safelyGet(Runnable.class);
        assert(objectOptional.isPresent());
        assertTrue(objectOptional.get() instanceof MyObject);
    }
}
