package org.yadi.core;

import java.util.function.Function;
import java.util.function.Supplier;


public class Reference<T> implements Supplier<T> {

    String name;
    Class type;
    Function<String, T> namedSource;
    Function<Class<T>,T> typedSource;

    public Reference(String name, Function<String, T> namedSource) {
        this.name = name;
        this.namedSource = namedSource;
    }

    public Reference(Class<T> type, Function<Class<T>,T> typedSource) {
        this.type = type;
        this.typedSource = typedSource;
    }

    public String getName() {
        return name;
    }

    public boolean isNamed() {
        return this.name != null;
    }

    public Class getType() {
        return type;
    }

    public T get() {
        if (isNamed()) {
            return (T) namedSource.apply(this.name);
        }else {
            return (T) typedSource.apply(this.type);
        }
    }


}
