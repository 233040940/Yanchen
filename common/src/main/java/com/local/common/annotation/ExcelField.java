package com.local.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ExcelField
 * @project yanchen
 * @description 使用poi创建excel模版属性字段注解;校验策略-->创建表头必须指定title且唯一(读取可不设置);order属性必须唯一且order不小于1。
 * @date 2020-06-17 20:45
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    String title() default "";           //title
    int order()default 1;             //顺序
}
