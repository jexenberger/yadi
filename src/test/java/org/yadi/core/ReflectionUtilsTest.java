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

import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertNotNull;

/**
 * Created by julian3 on 2014/05/03.
 */
public class ReflectionUtilsTest {


    @Test
    public void testResolveConstructor() throws Exception {

        Constructor constructor = ReflectionUtils.resolveConstructor(MyObject.class, new Class[]{String.class, Integer.class, String.class});
        assertNotNull(constructor);
        constructor.newInstance("hello", new Integer(1), "world");

    }
}
