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

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by julian3 on 2014/05/03.
 */
public class InstanceScopeInterceptorTest {


    final AtomicBoolean called = new AtomicBoolean(false);
    final AtomicInteger firstInstance = new AtomicInteger(0);
    final AtomicInteger secondInstance = new AtomicInteger(0);


    @Test
    public void testInvocation() throws Exception {
        ObjectDefinition<MyObject> definition = new ObjectDefinition<MyObject>()
                .type(MyObject.class)
                .destroyWith(MyObject::DESTROY);
        InstanceScopeInterceptor prototypeScopeInterceptor = new InstanceScopeInterceptor(definition);
        Supplier<Integer> instance = (Supplier<Integer>) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), MyObject.class.getInterfaces(), prototypeScopeInterceptor);
        Assert.assertNotEquals(instance.get(), instance.get());
    }


}
