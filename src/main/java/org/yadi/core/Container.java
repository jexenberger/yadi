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

/**
 * Created by julian3 on 2014/05/01.
 */
public interface Container {


    default void init() {

        build();
        if (getDefinitions().size() > 0) {
            getDefinitions().forEach((objectDefinition) -> {
                String name = objectDefinition.getName();
                getNamedDefinitions().put(name, objectDefinition);
                String scopeIdentifier = objectDefinition.getScopeIdentifier();
                Optional<Scope> scopeOptional = Scopes.get(scopeIdentifier);
                scopeOptional.ifPresent((scope) -> scope.addObjectDefinition(objectDefinition));
            });
            getDefinitions().forEach((objectDefinition) -> {
                String name = objectDefinition.getName();
                Optional<Class[]> bindings = objectDefinition.getBindings();
                bindings.ifPresent((classes) -> {
                    for (Class aClass : classes) {
                        getTypedDefinitions().put(aClass, getNamedDefinitions().get(name));
                    }
                });
            });

        }
        Scopes.startup();
    }

    Map<Class<?>, ObjectDefinition<?>> getTypedDefinitions();

    Map<String, ObjectDefinition<?>> getNamedDefinitions();

    default void close() {
        Scopes.shutdown();
    }

    default <T> InjectableObjectDefinition<T> define(Class<T> type) {
        return this.<T>define().type(type);
    }

    default <T> InjectableObjectDefinition<T> define() {
        InjectableObjectDefinition<T> definition = new InjectableObjectDefinition<>(this);
        getDefinitions().add(definition);
        return definition;
    }

    Collection<ObjectDefinition<?>> getDefinitions();

    default <T> Optional<T> safelyGet(Class<T> type) {
        final ObjectDefinition<T> objectDefinition = (ObjectDefinition<T>) getTypedDefinitions().get(type);
        return getFromScope(objectDefinition);
    }

    default <T> T get(Class<T> type) {
        final ObjectDefinition<T> objectDefinition = (ObjectDefinition<T>) getTypedDefinitions().get(type);
        return getFromScope(objectDefinition).get();
    }

    default <T> Optional<T> safelyGet(String name) {
        final ObjectDefinition<T> objectDefinition = (ObjectDefinition<T>) getNamedDefinitions().get(name);
        return getFromScope(objectDefinition);
    }

    default <T> T get(String name) {
        final ObjectDefinition<T> objectDefinition = (ObjectDefinition<T>) getNamedDefinitions().get(name);
        return getFromScope(objectDefinition).get();
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

    void build();


}
