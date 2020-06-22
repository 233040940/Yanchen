package com.local.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.Map;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description  spring 上下文工具类
 * @date 2020-05-26 17:35
 */

public class ApplicationContextProvider implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ApplicationContextProvider.applicationContext = applicationContext;

    }

    /**
     * @return T
     * @Description 通过bean类型获取单个bean，如果存在多个会抛异常
     * @Param [beanType  bean类型]
     * @Author yc
     */

    public static final <T> T getBean(Class<T> beanType) throws BeansException {

        return applicationContext.getBean(beanType);
    }


    /**
     * @return java.lang.Object
     * @Description 通过bean名称获取bean
     * @Param [beanName bean名称]
     * @Author yc
     */

    public static final Object getBean(String beanName) throws BeansException {

        return applicationContext.getBean(beanName);
    }


    public  static  final <T> T getBean(String beanName,Class<T> beanType)throws BeansException{

        return applicationContext.getBean(beanName,beanType);
    }
    /**
     * @return java.util.Map <java.lang.String beanName , T bean对象>
     * @Description 通过bean类型获取所有bean
     * @Param [beanType bean类型]
     * @Author yc
     * @Date 2020-05-26 17:53
     * @version 1.0
     */

    public static final <T> Map<String,T> getBeans(Class<T> beanType) throws BeansException{

        return applicationContext.getBeansOfType(beanType);
    }

    }
