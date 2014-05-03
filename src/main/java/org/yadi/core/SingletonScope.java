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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by julian3 on 2014/05/01.
 */
public class SingletonScope implements Scope {

    public static final String SINGLETON = "singleton";


    Map<String, Pair<ObjectDefinition<?>, Object>> instances;


    @Override
    public <T> Optional<T> create(ObjectDefinition<T> objectDefinition) {
        String name = objectDefinition.getName();
        if (!instances.containsKey(name)) {
            instances.put(name, new Pair<>(objectDefinition, objectDefinition.create()));
        }
        Pair<ObjectDefinition<?>, Object> objectDefinitionObjectPair = instances.get(name);
        return Optional.ofNullable((T) objectDefinitionObjectPair.getCdr());
    }

    @Override
    public <T> Optional<T> create(ObjectDefinition<T> objectDefinition, Scope parentScope) {
        //don't care, a singleton is a singleton
        return create(objectDefinition);
    }

    @Override
    public void terminate() {

        instances.forEach((key, value) -> {
            //bone headed requirement for generics to cast for capture<?>
            ObjectDefinition definition = value.getCar();
            Pair<ObjectDefinition<?>, Object> instance = instances.get(definition.getName());
            definition.destroy(instance.getCdr());
        });

        if (instances != null) {
            instances.clear();
        }
    }

    @Override
    public void initialise() {
        this.instances = new LinkedHashMap<>(20);
    }

    @Override
    public String getIdentifier() {
        return SINGLETON;
    }


}
