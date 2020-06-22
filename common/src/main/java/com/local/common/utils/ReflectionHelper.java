package com.local.common.utils;

import com.local.common.annotation.ExcelField;
import com.local.common.entity.Depart;
import com.local.common.entity.Dept;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
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

    public static <T> Triple<Integer, Class<?>, Object> fieldValueTriple(T t, Field field) {

        try {

            field.setAccessible(true);

            ExcelField excelField = field.getAnnotation(ExcelField.class);

            int order=excelField.order();

            Class<?> type = field.getType();       //属性类型

            if(type != String.class && type != Object.class){

                type= ClassUtils.primitiveToWrapper(type);   //将基本数据类型进行装箱
            }

            Object fieldValue = field.get(t);   //属性值

            return Triple.of(order, type, fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static <T> Triple<Object,Integer,? extends Class> fieldValueOrderTypeTriple(T t, Field field) {

        try {

            field.setAccessible(true);

            ExcelField annotation = field.getAnnotation(ExcelField.class);
            int order = annotation.order();      //属性顺序

            Object fieldValue = field.get(t);   //属性值
            Class<?> type=field.getType();      //属性类型
            return Triple.of(fieldValue,order,type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Triple<Field, Integer, ? extends Class<?>>> templateFieldOrderTypeTriples(List<Field> fields){

        List<Triple<Field, Integer, ? extends Class<?>>> collect = fields.stream().map((f) -> {

            ExcelField excelField = ReflectionHelper.findAnnotationOnField(f, ExcelField.class);

            int order = excelField.order();    //属性顺序

            Class<?> type = f.getType();       //属性类型

            return Triple.of(f, order, type);
        }).collect(Collectors.toList());

        return collect;
    }


    /**
     * @return A
     * @Description 通过指定[属性]，获取该属性的指定[注解]
     * @Param [field 属性, annotationClass注解类型]
     * @Author yc
     */

    public static <A extends Annotation> A findAnnotationOnField(Field field, Class<A> annotationClass) {

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
