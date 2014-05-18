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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by julian3 on 2014/05/03.
 */
public class ConstructionTest {

    private Construction<String> construction;

    @Before
    public void setup() {
        construction = (args) -> {
            args.add("hello", String.class);
        };
    }

    @After
    public void after() {
    }


    @Test
    public void testCreationWithConstructor() throws Exception {
        String result = construction.createWithConstructor(new Arguments(), String.class, null, null);
        assertEquals("hello", result);
    }

    @Test
    public void testCreationWithNoConstructor() throws Exception {
        Construction<String> cons = (args) -> {};
        String result = cons.createWithConstructor(new Arguments(), String.class, null, null);
        assertEquals("", result);
    }

}
