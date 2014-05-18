package org.yadi.core;
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

import java.lang.reflect.Constructor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.yadi.core.ReflectionUtils.resolveConstructor;

/**
 * Created by julian3 on 2014/05/03.
 */
public interface Construction<T> extends Consumer<Arguments> {


    default T createWithConstructor(Arguments arguments, Class<T> implementation, Function<Reference, Object> typeResolver, Function<Reference, Object> objectResolver) {
        accept(arguments);
        try {
            if (arguments.size() == 0) {
                return implementation.newInstance();
            }

            Object[] args = arguments.stream().map((pair) -> {
                if (pair.getCdr() instanceof Reference && objectResolver != null) {
                    Reference ref = (Reference) pair.getCdr();
                    return objectResolver.apply(ref);
                } else {
                    return pair.getCdr();
                }

            }).toArray();
            Class[] types = arguments.stream()
                    .map((pair) -> {
                        if (pair.getCdr() instanceof Reference && typeResolver != null && pair.getCar() == null) {
                            Reference ref = (Reference) pair.getCdr();
                            return typeResolver.apply(ref);
                        } else {
                            return pair.getCar();
                        }
                    })
                    .collect(Collectors.toList())
                    .toArray(new Class[]{});

            Constructor<T> theConstructor = resolveConstructor(implementation, types);
            return theConstructor.newInstance(args);
        } catch (Exception e) {
            throw new ContainerException(e);
        }
    }


}
