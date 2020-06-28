package com.local.common.utils;

import com.local.common.annotation.ExcelField;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 反射工具类
 * @date 2020-06-19 13:27
 */
public class ReflectionHelper {


    private ReflectionHelper() {

        throw new RuntimeException("ReflectionHelper is tool class,Not support instanced");
    }


    /**
     * @return java.util.List<java.lang.reflect.Field>
     * @Description 查找[目标类]包含指定[注解]的属性
     * @Param [tClass 目标类, annotationClass 注解类]
     * @Author yc
     */

    public static <T, A extends Annotation> List<Field> findFieldsOnAnnotation(Class<T> tClass, Class<A> annotationClass) {

        Field[] declaredFields = tClass.getDeclaredFields();

        return Stream.of(declaredFields).filter((f) -> {

            A s = f.getAnnotation(annotationClass);
            return s != null;
        }).collect(Collectors.toList());
    }


    public static <T> List<Field> findFields(T t) {

        return Stream.of(t.getClass().getDeclaredFields()).collect(Collectors.toList());
    }



    /**
     * @return A
     * @Description 通过指定[属性]，获取该属性的指定[注解]
     * @Param [field 属性, annotationClass注解类型]
     * @Author yc
     */

    public static <A extends Annotation> A findAnnotationOnField(Field field, Class<A> annotationClass) {

        field.setAccessible(true);
        return field.getAnnotation(annotationClass);
    }

    /**
     * @return java.lang.annotation.Annotation[]
     * @Description 获取属性的所有注解
     * @Param [field]
     * @Author yc
     * @Date 2020-06-20 17:00
     */

    public static Annotation[] findAnnotationsOnField(Field field) {

        return field.getDeclaredAnnotations();
    }

    public static String findFieldName(Field field) {

        return field.getName();
    }

    public static Class<?> findFieldType(Field field){

        return field.getType();
    }

}
