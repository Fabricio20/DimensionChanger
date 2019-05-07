package net.notfab.dimensionchanger.util;

import net.notfab.dimensionchanger.entities.NMSException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ReflectHelper {

    public static Class<?> getClass(String className) throws NMSException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new NMSException("Failed to find class " + className);
        }
    }

    public static Class<? extends Enum> getEnum(String className) throws NMSException {
        try {
            return (Class<? extends Enum>) Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new NMSException("Failed to find class " + className);
        } catch (ClassCastException ex) {
            throw new NMSException("Found class " + className + " but is not an enum.");
        }
    }

    public static Constructor<?> findConstructor(Class<?> clazz, Class<?>... params) throws NMSException {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            throw new NMSException("Failed to find constructor on " + clazz.getName() + " with arguments " + Arrays.toString(params));
        }
    }

    public static Method findMethod(String name, Class<?> clazz, Class<?>... arguments) throws NMSException {
        try {
            return clazz.getMethod(name, arguments);
        } catch (NoSuchMethodException e) {
            throw new NMSException("Failed to find method " + name + " on " + clazz.getName() + " with arguments " + Arrays.toString(arguments));
        }
    }

    public static Object invoke(Method method, Object instance, Object... args) throws NMSException {
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new NMSException("Failed to invoke " + method.getName() + " on " + method.getDeclaringClass().getName());
        }
    }

    public static Object instantiate(Constructor constructor, Object... args) throws NMSException {
        try {
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NMSException("Failed to instantiate " + constructor.getDeclaringClass().getName());
        }
    }

    public static Field findField(String name, Class<?> clazz) throws NMSException {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            throw new NMSException("Failed to find field " + name + " on class " + clazz.getName());
        }
    }

    public static Object getField(Field field, Object instance) throws NMSException {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new NMSException("Failed to get value from field " + field.getName() + " on class " + instance.getClass().getName());
        }
    }

}