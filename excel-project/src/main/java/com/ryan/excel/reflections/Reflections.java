/*
 * Personal Learning Use
 */
package com.ryan.excel.reflections;

import com.ryan.excel.constants.ExcelConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/23 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class Reflections {

    public static final String SETTER_PREFIX = "set";

    public static final String GETTER_PREFIX = "get";

    /**
     * 调用对象的getter方法
     */
    public static Object invokeGetter(Object obj, String propertyName){
        Object object = obj;
        String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
        object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
        return object;
    }

    /**
     * 调用setter方法
     */
    public static void invokeSetter(Object obj, String propertyName, Object val){
        String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
        invokeMethod(obj, setterMethodName,  new Object[]{val});
    }

    /**
     * 无论private/protected修饰符如何，直接调用对象方法。
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] paramTypes, final Object[] args){
        Method method = getAccessibleMethod(obj, methodName, paramTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 无论private/protected修饰符如何，直接调用对象方法。
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Object[] args){
        Method method = getAccessibleMethod(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 向上循环，获取对象的DeclaredMethod，并强制使其可访问。
     * 如果找不到到 Object 的过渡，则返回 null。
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... paramTypes) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, paramTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // 方法不在当前类定义中，继续向上过渡
                // 新添加
            }
        }
        return null;
    }

    /**
     * 向上循环，获取对象的DeclaredMethod，并强制使其可访问。
     * 如果找不到到 Object 的过渡，则返回 null。
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if(method.getName().equals(methodName)){
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }


    /**
     * 将private/protected方法改为public，尽量不调用实际更改的语句，避免JDK的SecurityManager异常。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 将反射的已检查异常转换为未检查异常。
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

}
