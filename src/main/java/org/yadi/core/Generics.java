package org.yadi.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generics {
    private Generics() {
        // empty, in order to avoid instantiation
    }

    public static <T> Class<T> extractTypeArgumentFromClass(Class<?> parameterizedClass)
            throws IllegalArgumentException {
        return extractTypeArgumentFromClass(parameterizedClass, 0);
    }

    public static <T> Class<T> extractTypeArgumentFromClass(Class<?> parameterizedClass,
                                                            int argumentNumber) throws IllegalArgumentException {
        ParameterizedType parameterizedType = extractParameterizedTypeFromClass(parameterizedClass);
        if (parameterizedType == null) {
            throw new IllegalArgumentException(
                    "Class and any of its superclasses are not parameterized.");
        }
        return (Class<T>) getActualTypeArgument(parameterizedType, argumentNumber);
    }

    private static Type getActualTypeArgument(ParameterizedType parameterizedType,
                                              int argumentNumber) throws IllegalArgumentException {
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length == 0) {
            throw new IllegalArgumentException("Class is not parameterized.");
        }
        if (argumentNumber < 0 || typeArguments.length <= argumentNumber) {
            throw new IllegalArgumentException("Invalid argumentNumber.");
        }
        return typeArguments[argumentNumber];
    }

    public static ParameterizedType extractParameterizedTypeFromClass(Class<?> parameterizedClass) {
        Type genericSuperclass = parameterizedClass.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        }
        if (genericSuperclass != null) {
            return extractParameterizedTypeFromClass((Class<?>) genericSuperclass);
        }
        return null;
    }

    public static <T> Class<T> extractTypeArgumentFromInterface(Class<?> parameterizedClass)
            throws IllegalArgumentException {
        return extractTypeArgumentFromInterface(parameterizedClass, 0);
    }

    public static <T> Class<T> extractTypeArgumentFromInterface(Class<?> parameterizedClass,
                                                                int argumentNumber) throws IllegalArgumentException {
        ParameterizedType parameterizedType = extractParameterizedTypeFromInterface(parameterizedClass);
        if (parameterizedType == null) {
            throw new IllegalArgumentException(
                    "Class and any of its interfaces are not parameterized.");
        }
        return (Class<T>) getActualTypeArgument(parameterizedType, argumentNumber);
    }

    private static ParameterizedType extractParameterizedTypeFromInterface(
            Class<?> parameterizedClass) {
        Type[] genericInterfaces = parameterizedClass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                return (ParameterizedType) genericInterface;
            }
            ParameterizedType parameterizedType = extractParameterizedTypeFromInterface((Class<?>) genericInterface);
            if (parameterizedType != null) {
                return parameterizedType;
            }
        }
        return null;
    }
}
