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
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by julian3 on 2014/05/01.
 */
public interface Scope {


    Collection<ObjectDefinition<?>> objectDefinitions = new ArrayList<>();

    <T> java.util.Optional<T> create(ObjectDefinition<T> objectDefinition);

    void terminate();
    void initialise();
    String getIdentifier();

    default void forEachDefintion(Consumer<ObjectDefinition<?>> handler) {
        objectDefinitions.forEach(handler);
    }

    default void addObjectDefinition(ObjectDefinition<?> objectDefinition) {
        objectDefinitions.add(objectDefinition);
    }


}
