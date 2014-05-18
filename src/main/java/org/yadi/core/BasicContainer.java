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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by julian3 on 2014/05/03.
 */
public abstract class BasicContainer implements Container{

    private LinkedHashMap<Class<?>, ObjectDefinition<?>> typedDefinitions;
    private LinkedHashMap<String, ObjectDefinition<?>> namedDefinitions;
    private Collection<ObjectDefinition<?>> objectDefinitions;
    private Log log;

    protected BasicContainer() {
        typedDefinitions = new LinkedHashMap<>(20);
        namedDefinitions = new LinkedHashMap<>(20);
        objectDefinitions = new ArrayList<>();
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
            this.log = new SystemOutLog();
        }
        return log;
    }
}
