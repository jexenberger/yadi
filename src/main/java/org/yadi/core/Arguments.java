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
import java.util.stream.Stream;

/**
 * Created by julian3 on 2014/05/03.
 */
public class Arguments {

    Collection<Pair<Class, Object>> arguments = new ArrayList<>();

    public Arguments() {
        this.arguments = new ArrayList<>(5);
    }

    public Arguments add(Object value, Class<?> targetType) {
        arguments.add(new Pair<>(targetType, value));
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
