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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by julian3 on 2014/05/01.
 */
public class ObjectDefinition<T> {
    private Class<T> implementation;

    private Collection<Pair<BiConsumer<?, ?>, Object>> mutators;
    private Supplier<T> factory;
    private Consumer<T> destructor;
    private Consumer<T> initialiseWith;
    private String name;
    private Class[] bindings;
    private String scopeIdentifier = SingletonScope.SINGLETON;
    private Collection<Pair<Class, Object>> constructorArgs;
    private Construction<T> construction;
    private Arguments arguments;

    public ObjectDefinition() {
        this.construction = (arguments)-> {};
        this.arguments = new Arguments();
    }

    public ObjectDefinition<T> type(Class<T> myObjectClass) {
        this.implementation = myObjectClass;
        return this;
    }

    public T create() {
        try {

            final T instance = createNewInstance();
            getMutators().forEach((pair) -> {
                Object value = pair.getCdr();
                BiConsumer<T, Object> mutator = (BiConsumer<T, Object>) pair.getCar();
                if (value instanceof Supplier) {
                    value = ((Supplier) value).get();
                }
                mutator.accept(instance, value);
            });
            if (this.initialiseWith != null) {
                this.initialiseWith.accept(instance);
            }
            return instance;
        } catch (Exception e) {
            throw new ContainerException(e);
        }
    }

    public ObjectDefinition<T> construct(Construction<T> constructor) {
        this.construction = constructor;
        return this;
    }


    private T createNewInstance() throws InstantiationException, IllegalAccessException {
        if (this.factory != null) {
            return this.factory.get();
        }
        return construction.createWithConstructor(this.arguments, this.implementation);
    }


    public <K> ObjectDefinition<T> set(BiConsumer<T, K> setA, K s) {
        this.getMutators().add(new Pair<>(setA, s));
        return this;

    }

    public <K> ObjectDefinition<T> addConstructorArg(Object s) {
        return addConstructorArg(s, s.getClass());
    }

    public <K> ObjectDefinition<T> addConstructorArg(Object s, Class<?> targetType) {
        this.arguments.add(s, targetType);
        return this;

    }


    public <K> ObjectDefinition<T> bind(BiConsumer<T, K> setA, Supplier<K> s) {
        this.getMutators().add(new Pair<>(setA, s));
        return this;
    }

    protected Collection<Pair<BiConsumer<?, ?>, Object>> getMutators() {
        if (this.mutators == null) {
            this.mutators = new ArrayList<>();
        }
        return this.mutators;
    }

    public ObjectDefinition<T> createWith(Supplier<T> constructor) {
        this.factory = constructor;
        return this;
    }


    public ObjectDefinition<T> destroyWith(Consumer<T> destructor) {
        this.destructor = destructor;
        return this;
    }


    public ObjectDefinition<T> initWith(Consumer<T> afterPropertiesSet) {
        this.initialiseWith = afterPropertiesSet;
        return this;
    }

    void destroy(T instance) {
        if (this.destructor != null) {
            this.destructor.accept(instance);
        }
    }

    public ObjectDefinition<T> inScope(String identifier) {
        this.scopeIdentifier = identifier;
        return this;

    }

    public ObjectDefinition<T> asInstance() {
        return inScope(InstanceScope.INSTANCE);
    }

    public ObjectDefinition<T> named(String name) {
        this.name = name;
        return this;
    }

    public ObjectDefinition<T> boundTo(Class<?>... types) {
        for (Class<?> type : types) {
            assert type.isInterface();
        }
        this.bindings = types;
        return this;
    }

    public String getName() {
        if (this.name != null && !this.name.trim().isEmpty()) {
            return this.name.trim();
        }
        return this.implementation.getName();
    }

    public String getScopeIdentifier() {
        return scopeIdentifier;
    }

    public boolean hasDestructor() {
        return this.destructor != null;
    }

    public Optional<Class[]> getBindings() {
        return Optional.ofNullable(bindings);
    }

    public Class<T> getImplementation() {
        return implementation;
    }

    protected <K> void addMutator(BiConsumer<T, K> consumer, Object value) {
        getMutators().add(new Pair<>(consumer, value));
    }

    Construction<T> getConstruction() {
        return construction;
    }
}
