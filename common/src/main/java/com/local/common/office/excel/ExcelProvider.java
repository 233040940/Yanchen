package com.local.common.office.excel;

import com.local.common.annotation.ExcelField;
import com.local.common.utils.ReflectionHelper;
import org.apache.commons.lang3.tuple.Triple;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelProvider {


    private ExcelProvider(){
        throw new RuntimeException("ExcelProvider is tool class,Not support instanced");
    }

/**
 * @description: (属性值-属性顺序-属性类型）配对
 * @create-by: yanchen @date:2020-06-29 01:10
 * @param t
 * @param field
 * @return: org.apache.commons.lang3.tuple.Triple<java.lang.Object , java.lang.Integer , ?   extends   java.lang.Class>
 */
public static <T> Triple<Object,Integer,? extends Class> fieldValueOrderTypeTriple(T t, Field field) {

        try {

            field.setAccessible(true);

            ExcelField excelField = ReflectionHelper.findAnnotationOnField(field, ExcelField.class);
            int order = excelField.order();      //属性顺序

            Object fieldValue = field.get(t);    //属性值
            Class<?> type=field.getType();       //属性类型

            return Triple.of(fieldValue,order,type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
      * @Description (属性-属性顺序-属性类型）配对
      * @Param [fields]
      * @return java.util.List<Triple<Field 属性,Integer 属性顺序,? extends java.lang.Class<?> 属性类型>>
      * @Author yc
      * @Date 2020-06-24 19:24
      * @version 1.0
      */

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
      * @Description 通过注解过滤模版属性
      * @Param [excelTemple , annotationClass ]
      * @return java.util.List<java.lang.reflect.Field>
      * @Author yc
      * @Date 2020-06-24 19:32
      */
    
    public static<A extends Annotation> List<Field> filterTemplateFields(Class<?> excelTemple, Class<A> annotationClass){
        
        return ReflectionHelper.findFieldsOnAnnotation(excelTemple,annotationClass);
    }
}
