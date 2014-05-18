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
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by julian3 on 2014/05/03.
 */
public class Arguments {

    Collection<Pair<Class, Object>> arguments = new ArrayList<>();

    Function<String, Object> namedSource;
    Function<Class<?>,Object> typedSource;

    public Arguments() {
        this.arguments = new ArrayList<>(5);
    }

    public Arguments(Function<String, Object> namedSource, Function<Class<?>, Object> typedSource) {
        this();
        this.namedSource = namedSource;
        this.typedSource = typedSource;
    }

    public Arguments add(Object value, Class<?> targetType) {
        arguments.add(new Pair<>(targetType, value));
        return this;
    }

    public Arguments ref(String name, Class<?> targetType) {
        add(new Reference(name, namedSource), targetType);
        return this;
    }

    public Arguments ref(String name) {
        add(new Reference(name, namedSource), null);
        return this;
    }

    public Arguments ref(Class type, Class<?> targetType) {
        add(new Reference(type, typedSource), targetType);
        return this;
    }

    public Arguments ref(Class type) {
        add(new Reference(type, typedSource), type);
        return this;
    }

    public Arguments add(Object value) {
        arguments.add(new Pair<>(value.getClass(), value));
        return this;
    }

    public int size() {
        return arguments.size();
    }

    public Stream<Pair<Class, Object>> stream() {
        return arguments.stream();
    }


}
