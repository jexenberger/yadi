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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by julian3 on 2014/05/01.
 */
public class MyObject implements Runnable, Supplier<Integer> {

    String a;
    int b;
    String c;
    boolean afterProps = false;

    public MyObject() {
    }

    public MyObject(String a, int b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public MyObject(String a) {
        this.a = a;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public static int aValue() {
        return 31;
    }

    public static MyObject CREATE() {
        return new MyObject("default");
    }

    public void DESTROY() {
        this.setA(null);
        System.out.println("DESTROYED");
    }

    public void afterPropertiesSet() {
        System.out.println("created and populated");
        this.afterProps = true;
    }

    public boolean isAfterProps() {
        return afterProps;
    }

    @Override
    public void run() {
        System.out.println("hello world");
    }

    public void start() {
        System.out.println("started");
    }

    @Override
    public Integer get() {
        run();
        return this.hashCode();
    }
}
