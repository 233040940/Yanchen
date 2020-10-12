package com.local.common.annotation;

import com.local.common.enums.Framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @project yanchen
 * @description 接口限流注解
 * @date 2020-06-22 22:32
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceRateLimit {

    String value() default "";          //标示某个接口名称，可采取[类名-方法名]组合，便于统计

    double permitsPerSecond() default Double.MAX_VALUE;        //每秒生成的令牌数

    long waitTimeOut() default 0L;            //获取令牌等待时间

    TimeUnit timeOutUnit() default TimeUnit.MILLISECONDS;   //超时时间单位

    Framework model() default Framework.STANDALONE;        //架构模型
}


