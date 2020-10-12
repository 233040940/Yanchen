package com.local.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yc
 * @project yanchen
 * @description 反射工具类
 * @date 2020-06-19 13:27
 */
public class ReflectionHelper {


    private ReflectionHelper() {

        throw new RuntimeException("ReflectionHelper is tool class,Not support instanced");
    }
    /**
     * @description: 通过注解查找目标类的所有属性
     * @create-by: yanchen @date:2020-09-02 23:52 
     * @param tClass 目标类class
     * @param annotationClass 注解class
     * @return: java.util.List<java.lang.reflect.Field>
     */
    public static <T, A extends Annotation> List<Field> findFieldsOnAnnotation(Class<T> tClass, Class<A> annotationClass) {

        Field[] declaredFields = tClass.getDeclaredFields();

        return Stream.of(declaredFields).filter((f) -> {

            A s = f.getAnnotation(annotationClass);
            return s != null;
        }).collect(Collectors.toList());
    }

    /**
     * @description: 查找目标类的所有属性
     * @create-by: yanchen @date:2020-09-02 23:54
     * @param t
     * @return: java.util.List<java.lang.reflect.Field>
     */
    public static <T> List<Field> findFields(T t) {

        return Stream.of(t.getClass().getDeclaredFields()).collect(Collectors.toList());
    }

    /**
     * @description: 获取属性注解
     * @create-by: yanchen @date:2020-09-02 23:55 
     * @param field 属性
     * @param annotationClass 注解class
     * @return: A
     */
    public static <A extends Annotation> A findAnnotationOnField(Field field, Class<A> annotationClass) {
        field.setAccessible(true);
        return field.getAnnotation(annotationClass);
    }
    
    /**
     * @description: 查询属性的所有注解
     * @create-by: yanchen @date:2020-09-02 23:56 
     * @param field
     * @return: java.lang.annotation.Annotation[]
     */
    public static Annotation[] findAnnotationsOnField(Field field) {

        return field.getDeclaredAnnotations();
    }

    /**
     * @description: 获取属性名称
     * @create-by: yanchen @date:2020-09-02 23:56
     * @param field
     * @return: java.lang.String
     */
    public static String findFieldName(Field field) {
        return field.getName();
    }

    /**
     * @description: 获取属性类型
     * @create-by: yanchen @date:2020-09-02 23:56
     * @param field
     * @return: java.lang.Class<?>
     */
    public static Class<?> findFieldType(Field field){
        return field.getType();
    }

}
