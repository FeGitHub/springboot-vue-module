package com.company.project.utils;

import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


public abstract class CabReflections {

    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    public static final Object[] NULL_ARGS = new Object[]{null};


    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to new instance of " + clazz, e);
        }
        return (T) obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Constructor<?> constructor, Object... args) {
        Object obj;
        try {
            obj = constructor.newInstance(args);
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to new instance with " + constructor, e);
        }
        return (T) obj;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getConstructor(parameterTypes);

        } catch (Exception e) {
            String args = " ";
            if (parameterTypes != null && parameterTypes.length > 0) {
                args = Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", "));
            }
            throw new IllegalArgumentException("could not find constructor " + clazz.getSimpleName() + "(" + args + ")");
        }
    }

    public static boolean isPublicStaticFinal(int modifiers) {
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    public static boolean isAbstract(Class<?> clazz) {
        return clazz != null && Modifier.isAbstract(clazz.getModifiers());
    }

    public static ClassLoader getDefaultClassLoader() {
        try {
            // 取当前线程的 contextClassLoader 的话，小心在多线程环境下类加载的问题
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                return classLoader;
            }
        } catch (Throwable ignored) {
        }

        return CabReflections.class.getClassLoader();
    }


    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        T ann = clazz.getAnnotation(annotationClass);
        if (ann == null) {
            throw new IllegalArgumentException(annotationClass.getName() + " must be present on " + clazz);
        }
        return ann;
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Method method, Object target, Object... args) {
        try {
            return (T) method.invoke(target, args);

        } catch (Exception e) {
            throw new IllegalStateException("failed to invoke method " + method.getName() + " of target: " + target, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Field field, Object target) {
        Object obj;
        try {
            field.setAccessible(true);
            obj = field.get(target);

        } catch (Exception e) {
            throw new IllegalStateException("failed to get value of field: " + field, e);
        }
        return (T) obj;
    }


    public static void setFieldValue(Object field, Object target, Object value) {
        if (field instanceof String) {
            Field fieldObj;
            Class<?> targetClass;
            Object realTarget = null;

            if (target instanceof String) { // assume target is a 'ClassName', field is a static Field
                targetClass = resolveClassName(target.toString(), getDefaultClassLoader());

            } else if (target instanceof Class<?>) {
                targetClass = (Class<?>) target;

            } else {
                targetClass = target.getClass();
                realTarget = target;
            }
            fieldObj = findField(targetClass, field.toString());
            doSetFieldValue(fieldObj, realTarget, value);
            return;
        }
        doSetFieldValue((Field) field, target, value);
    }

    private static void doSetFieldValue(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "failed to set field [" + field + "] with value : " + value, e);
        }
    }

    public static <T> T getPropertyValue(Object target, PropertyDescriptor pd) {
        return invokeMethod(pd.getReadMethod(), target);
    }

    public static void setPropertyValue(Object target, PropertyDescriptor pd, Object propertyValue) {
        invokeMethod(pd.getWriteMethod(), target, propertyValue);
    }

    public static Class<?> resolveClassName(String className, ClassLoader classLoader) {
        return ClassUtils.resolveClassName(className, classLoader);
    }

    public static Class<?> resolveClassName(String className) {
        return ClassUtils.resolveClassName(className, getDefaultClassLoader());
    }

    public static Class<?> resolveClassName(String className, Class<?> expectedSuperClass) {
        Class<?> clazz = ClassUtils.resolveClassName(className, getDefaultClassLoader());

        if (expectedSuperClass != null && !expectedSuperClass.isAssignableFrom(clazz)) {
            String verb = expectedSuperClass.isInterface() ? "implement " : "inherit from ";
            throw new IllegalArgumentException(className + " must " + verb + expectedSuperClass.getName());
        }
        return clazz;
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        return ClassUtils.isPresent(className, classLoader);
    }

    /**
     * Check whether the specified class is a CGLIB-generated class (does the class name contain "$$" ?)
     *
     * @param clazz the class to check
     */
    public static boolean isProxyClass(Class<?> clazz) {
        String className = clazz.getSimpleName();
        return className.contains("$$") || className.contains("HibernateProxy$");
    }

    /**
     * Attempt to find a {@link Field field}(public, private...) on the supplied {@link Class} with the
     * supplied {@code name}. Searches all superclasses up to {@link Object}.
     *
     * @param clazz the class to introspect
     * @param name  the name of the field
     * @return the corresponding Field object, or {@code null} if not found
     */
    public static Field findField(Class<?> clazz, String name) {
        return ReflectionUtils.findField(clazz, name);
    }

    public static Class<?> resolveReturnCollectionArgType(Method getter) {
        Class<?> argClass = GenericTypeResolver.resolveReturnTypeArgument(getter, List.class);
        if (argClass == null) {
            argClass = GenericTypeResolver.resolveReturnTypeArgument(getter, Set.class);
        }
        return argClass;
    }


    /**
     * Check if the right-hand side type may be assigned to the left-hand side
     * type, assuming setting by reflection. Considers primitive wrapper
     * classes as assignable to the corresponding primitive types.
     *
     * @param lhsType the target type
     * @param rhsType the value type that should be assigned to the target type
     * @return if the target type is assignable from the value type
     */
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        return ClassUtils.isAssignable(lhsType, rhsType);
    }

    public static Class<?> findOriginalClass(Class<?> maybeProxyClass) {
        Class<?> cls = maybeProxyClass;
        for (; cls != null && cls != Object.class && isProxyClass(cls); cls = cls.getSuperclass()) ;
        return cls;
    }


    public static List<Method> findMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return findMethodsWithAnnotation(clazz, Object.class, annotation);
    }

    public static List<Method> findMethodsWithAnnotation(
            Class<?> clazz, Class<?> exclusiveSuperClass, Class<? extends Annotation> annotation) {
        List<Method> methodList = null;
        Set<String> methodNames = null;

        for (Class<?> loopClass = clazz; loopClass != exclusiveSuperClass; loopClass = loopClass.getSuperclass()) {
            for (Method method : loopClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    if (methodNames != null && methodNames.contains(method.getName())) {
                        continue; // 从子类开始查找，所以一旦进入这里，肯定是 父类被子类 override的方法
                    }
                    if (methodList == null) {
                        methodList = new ArrayList<>(8);
                        methodNames = new HashSet<>(8);
                    }
                    method.setAccessible(true);
                    methodList.add(method);
                    methodNames.add(method.getName());
                }
            }
        }
        return methodList != null ? methodList : Collections.emptyList();
    }

    public static boolean isSimpleType(Class<?> type) {
        return type != null && (
                type.isPrimitive()
                        || type == String.class
                        || Number.class.isAssignableFrom(type)
                        || type == Boolean.class
        );
    }


}
