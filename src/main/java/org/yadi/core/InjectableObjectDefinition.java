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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by julian3 on 2014/05/03.
 */
public class InjectableObjectDefinition<T>  extends ObjectDefinition<T>{


    Container context;

    public InjectableObjectDefinition(Container context) {
        this.context = context;
    }

    @Override
    public InjectableObjectDefinition<T> type(Class<T> myObjectClass) {
        super.type(myObjectClass);
        return this;
    }


    @Override
    public <K> InjectableObjectDefinition<T> set(BiConsumer<T, K> setA, K s) {
        super.set(setA, s);
        return this;
    }

    @Override
    public <K> InjectableObjectDefinition<T> bind(BiConsumer<T, K> setA, Supplier<K> s) {
        super.bind(setA, s);
        return this;
    }

    @Override
    public InjectableObjectDefinition<T> createWith(Supplier<T> constructor) {
        super.createWith(constructor);
        return this;
    }

    @Override
    public InjectableObjectDefinition<T> destroyWith(Consumer<T> destructor) {
        super.destroyWith(destructor);
        return this;
    }

    @Override
    public InjectableObjectDefinition<T> initWith(Consumer<T> afterPropertiesSet) {
        super.initWith(afterPropertiesSet);
        return this;
    }


    @Override
    public InjectableObjectDefinition<T> inScope(String identifier) {
        super.inScope(identifier);
        return this;
    }

    @Override
    public ObjectDefinition<T> asInstance() {
        super.asInstance();
        return this;
    }

    @Override
    public InjectableObjectDefinition<T> named(String name) {
        super.named(name);
        return this;
    }

    @Override
    public InjectableObjectDefinition<T> boundTo(Class<?>... types) {
        super.boundTo(types);
        return this;
    }


    @Override
    public <K> InjectableObjectDefinition<T> addConstructorArg(Object s) {
        super.addConstructorArg(s);
        return this;
    }

    @Override
    public <K> InjectableObjectDefinition<T> addConstructorArg(Object s, Class<?> targetType) {
        super.addConstructorArg(s, targetType);
        return this;
    }

    public <K> InjectableObjectDefinition<T> inject(BiConsumer<T, K> setMethod, String name) {

        return inject(setMethod, ()-> context.get(name));
    }

    public <K> InjectableObjectDefinition<T> inject(BiConsumer<T, K> setMethod, Class type) {
        return inject(setMethod, ()->  (K) context.get(type));
    }

    @Override
    public InjectableObjectDefinition<T> construct(Construction<T> constructor) {
        super.construct(constructor);
        return this;
    }

    public <K> InjectableObjectDefinition<T> inject(BiConsumer<T, K> setMethod, Supplier<K> supplier) {
        BiConsumer<T, K> consumer = (instance, value) -> {
            K result = supplier.get();
            if (result == null) {
                throw new ContainerException(instance.getClass().getName()+" tried to wire named instance which does not exist");
            }
            setMethod.accept(instance, result);
        };
        addMutator(consumer, null);
        return this;
    }


}
