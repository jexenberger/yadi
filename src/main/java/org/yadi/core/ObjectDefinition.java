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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.yadi.core.Generics.extractTypeArgumentFromClass;
import static org.yadi.core.Generics.extractTypeArgumentFromInterface;

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
    private Construction<T> construction;
    private Arguments arguments;
    private Container container;
    private boolean proxy = false;
    private boolean explicitlyNamed = false;
    private boolean explicitlyBound = false;

    public ObjectDefinition() {
        this.construction = (arguments) -> {
        };
        this.arguments = new Arguments();
    }

    public ObjectDefinition(Container container) {
        this();
        this.container = container;
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


    public <K> ObjectDefinition<T> inject(BiConsumer<T, K> setMethod, Class<K> type) {
        return inject(setMethod, ()->  (K) getContainer().get(type));
    }

    public ObjectDefinition<T> construct(Construction<T> constructor) {
        this.construction = constructor;
        return this;
    }


    private T createNewInstance() throws InstantiationException, IllegalAccessException {
        if (this.factory != null) {
            return this.factory.get();
        }
        return (T) construction.createWithConstructor(this.arguments, this.implementation, (ref) -> {
                    ObjectDefinition definition =  container.getDefinition(ref.getType());
                    return definition.getImplementation();
                },
                (ref) -> {
                    return  container.get(ref.getType());
                }
        );
    }


    public <K> ObjectDefinition<T> set(BiConsumer<T, K> setA, K s) {
        Class<? extends Type> aClass = setA.getClass().getGenericInterfaces()[0].getClass();
        this.getMutators().add(new Pair<>(setA, s));
        return this;

    }

    public <K> ObjectDefinition<T> proxy() {
        if (this.implementation == null) {
            throw new IllegalStateException("no implementation defined on Object Defintion");
        }
        if (this.implementation.getInterfaces().length == 0) {
            throw new IllegalStateException(this.implementation.getName()+" was set to be proxied but has no interfaces to proxy");
        }
        this.proxy = true;
        return this;
    }


    public <K> ObjectDefinition<T> args(Object... args) {
        for (Object arg : args) {
            return arg(arg, arg.getClass());
        }
        return this;
    }

    public <K> ObjectDefinition<T> arg(Object s, Class<?> targetType) {
        this.arguments.add(s, targetType);
        return this;

    }


    public <K> ObjectDefinition<T> constructorRef(Class<K> type) {
        return arg(new Reference<K>(type, container.asTypeSource()), null);
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
        this.explicitlyNamed = true;
        return this;
    }

    public boolean isExplicitlyNamed() {
        return explicitlyNamed;
    }


    public ObjectDefinition<T> boundTo(Class<?>... types) {
        for (Class<?> type : types) {
            validateType(type);
        }
        this.bindings = types;
        this.explicitlyBound = true;
        return this;
    }

    public boolean isExplicitlyBound() {
        return explicitlyBound;
    }

    private void validateType(Class<?> type) {
        if (implementation != null && !type.isAssignableFrom(implementation)) {
            throw new IllegalStateException("tried to bind '"+type+"' to '"+implementation+"' but the types are not compatible");
        }
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

    public Container getContainer() {
        return container;
    }

    public boolean isProxy() {
        return proxy;
    }

    Construction<T> getConstruction() {
        return construction;
    }

    public <K> ObjectDefinition<T> inject(BiConsumer<T, K> setMethod, Supplier<K> supplier) {
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

    public  <K> ObjectDefinition<T> validate() {
        if (this.implementation == null) {
            throw new IllegalStateException("no implementation defined");
        }
        if (this.bindings != null) {
            for (Class binding : bindings) {
                validateType(binding);
            }
        }
        return this;
    }
}
