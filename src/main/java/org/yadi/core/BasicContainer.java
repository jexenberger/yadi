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
 * Created by julian3 on 2014/05/03.
 */
public abstract class BasicContainer implements Container{

    private LinkedHashMap<Class<?>, ObjectDefinition<?>> typedDefinitions;
    private LinkedHashMap<String, ObjectDefinition<?>> namedDefinitions;
    private Collection<ObjectDefinition<?>> objectDefinitions;
    private Log log;
    private Container parent;

    protected BasicContainer() {
        typedDefinitions = new LinkedHashMap<>(20);
        objectDefinitions = new ArrayList<>();
        namedDefinitions = new LinkedHashMap<>(20);
    }

    protected BasicContainer(Container parent) {
        this();
        this.parent = parent;
    }

    @Override
    public Map<Class<?>, ObjectDefinition<?>> getTypedDefinitions() {
        return typedDefinitions;
    }

    @Override
    public Map<String, ObjectDefinition<?>> getNamedDefinitions() {
        return namedDefinitions;
    }

    @Override
    public Collection<ObjectDefinition<?>> getDefinitions() {
        return objectDefinitions;
    }

    @Override
    public Log getLog() {
        if (this.log == null) {
            if (getParent().isPresent()) {
                return getParent().get().getLog();
            } else {
                return new SystemOutLog();
            }
        }
        return log;
    }

    @Override
    public Optional<Container> getParent() {
        return Optional.ofNullable(this.parent);
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }
}
