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

/**
 * Created by julian3 on 2014/05/01.
 */
public class Pair<T,K> {

    T car;
    K cdr;

    public Pair(T car, K cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public T getCar() {
        return car;
    }

    public K getCdr() {
        return cdr;
    }
}
