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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by julian3 on 2014/05/01.
 */
public class Scopes {

    private static Map<String, Scope> SCOPES;

    static {
        init();
    }

    public static Optional<Scope> get(String identifier) {
        return Optional.ofNullable(SCOPES.get(identifier));
    }

    private static void init() {
        if (SCOPES == null) {
            SCOPES = new HashMap<String, Scope>();
        }
        register(new SingletonScope());
        register(new InstanceScope());
    }

    public static void register(Scope scope) {
        SCOPES.put(scope.getIdentifier(), scope);
    }

    public static void startup() {
        SCOPES.forEach((key, value)-> {
            value.initialise();
        });
    }

    public static void shutdown() {
        SCOPES.forEach((key, value)-> {
            value.terminate();
        });
    }


}
