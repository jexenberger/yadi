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

import java.lang.reflect.Proxy;
import java.util.Optional;

/**
 * Created by julian3 on 2014/05/03.
 */
public class InstanceScope implements Scope{

    public static final String INSTANCE = "instance";


    @Override
    public <T> Optional<T> create(ObjectDefinition<T> objectDefinition) {
        Class<T> implementation = objectDefinition.getImplementation();
        if (implementation.getInterfaces().length > 0) {
            InstanceScopeInterceptor prototypeScopeInterceptor = new InstanceScopeInterceptor(objectDefinition);
            T instance =  (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), implementation.getInterfaces(), prototypeScopeInterceptor);
            return Optional.of(instance);
        } else {
            throw new ContainerException(objectDefinition.getImplementation().getName()+" has no interfaces to proxy from request scope, this object cannot be injected from an object in a different scope or have a destructor");
        }

    }

    @Override
    public <T> Optional<T> create(ObjectDefinition<T> objectDefinition, Scope parentScope) {
        if ((parentScope == null || parentScope == this) && !objectDefinition.hasDestructor()) {
            return Optional.of(objectDefinition.create());
        } else {
            return create(objectDefinition);
        }
    }

    @Override
    public void terminate() {
        //nothing to do here

    }

    @Override
    public void initialise() {
        //nothing to do here
    }

    @Override
    public String getIdentifier() {
        return INSTANCE;
    }


}
