package com.baloot.presentation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Container {
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static void register(Class<?> type) {
        Constructor<?>[] constructors = type.getConstructors();
        if (constructors.length != 1) {
            throw new IllegalArgumentException("Type must have exactly one constructor");
        }
        Constructor<?> constructor = constructors[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            dependencies[i] = resolve(parameterTypes[i]);
        }
        try {
            instances.put(type, constructor.newInstance(dependencies));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T resolve(Class<T> type) {
        Object instance = instances.get(type);
        if (instance == null) {
            throw new IllegalArgumentException("Type not registered: " + type.getName());
        }
        return type.cast(instance);
    }
}
