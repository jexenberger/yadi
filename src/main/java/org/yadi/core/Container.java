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

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by julian3 on 2014/05/01.
 */
public interface Container {


    default void init() {

        build();
        if (getDefinitions().size() > 0) {
            getDefinitions().forEach((objectDefinition) -> {
                Optional<Class[]> bindings = objectDefinition.getBindings();
                bindings.ifPresent((classes) -> {
                    for (Class aClass : classes) {
                        if (getTypedDefinitions().containsKey(aClass)) {
                            ObjectDefinition<?> existing = getTypedDefinitions().get(aClass);
                            throw new IllegalStateException("tried to bind "+objectDefinition.getImplementation().getName()+" to "+aClass.getName()+" but "+existing.getImplementation().getName()+" is already bound to this");
                        }
                        getTypedDefinitions().put(aClass, objectDefinition);
                    }
                });
                if (!bindings.isPresent()) {
                    getTypedDefinitions().put(objectDefinition.getImplementation(), objectDefinition);
                }

            });

        }
        Scopes.startup();
    }

    Map<Class<?>, ObjectDefinition<?>> getTypedDefinitions();


    default void close() {
        Scopes.shutdown();
    }

    default <T> ObjectDefinition<T> define(Class<T> type) {
        return this.<T>define().type(type);
    }

    default <T> ObjectDefinition<T> define() {
        ObjectDefinition<T> definition = new ObjectDefinition<>(this);
        getDefinitions().add(definition);
        return definition;
    }

    Collection<ObjectDefinition<?>> getDefinitions();

    default <T> Optional<T> safelyGet(Class<T> type) {
        final ObjectDefinition<T> objectDefinition = getDefinition(type);
        return getFromScope(objectDefinition);
    }

    default <T> ObjectDefinition<T> getDefinition(Class<T> type) {
        return (ObjectDefinition<T>) getTypedDefinitions().get(type);
    }

    default <T> T get(Class<T> type) {
        final ObjectDefinition<T> objectDefinition = (ObjectDefinition<T>) getTypedDefinitions().get(type);
        if (objectDefinition == null) {
            throw new IllegalArgumentException(type.getName()+" is not bound to any object");
        }
        Optional<T> instanceFromScope = getFromScope(objectDefinition);
        return instanceFromScope.get();
    }





    default <T> Optional<T> getFromScope(ObjectDefinition<T> objectDefinition) {
        if (objectDefinition != null) {
            Optional<Scope> scope = Scopes.get(objectDefinition.getScopeIdentifier());
            if (scope.isPresent()) {
                return scope.get().create(objectDefinition, null);
            } else {
                throw new ContainerException(objectDefinition.getScopeIdentifier()+" is not a recognised scope");
            }
        }
        return Optional.empty();

    }

    default <K> Function<Class<K>, K> asTypeSource() {
        return (type)-> {return this.get(type);};
    }


    Log getLog();


    void build();


}
