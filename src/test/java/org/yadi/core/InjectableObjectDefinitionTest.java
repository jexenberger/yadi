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

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by julian3 on 2014/05/03.
 */
public class InjectableObjectDefinitionTest extends BasicContainer{


    @Before
    public void before() {
        this.init();
    }

    @After
    public void after() {
        this.close();
    }


    @Test
    public void testInjectByTypeAndName() throws Exception {

        Optional<AnObject> myObject = safelyGet(AnObject.class);
        assertTrue(myObject.isPresent());
        assertNotNull(myObject.get().getWired());
        assertNotNull(myObject.get().getOther());
    }

    @Override
    public void build() {
        define(MyObject.class)
                .asInstance()
                .boundTo(Runnable.class)
                .set(MyObject::setC, "hello world")
                .destroyWith(MyObject::DESTROY);

        define(AnObject.class)
                .boundTo(AnObject.class)
                .inject(AnObject::setWired, Runnable.class)
                .inject(AnObject::setOther, Runnable.class);

    }
}
