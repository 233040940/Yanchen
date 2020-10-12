package com.local.common.utils;

import com.local.common.exception.CustomException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 验证null工具类
 * @date 2020-06-17 23:55
 */
public class CustomValidator {

    /**
     * @return void
     * @Description 验证String不为null，且包含非空格字符
     * @Param [param,exceptionTip 异常提示]
     * @Author yc
     * @Date 2020-06-18 15:57
     * @version 1.0
     */

    public static void checkStringNotEmpty(String param, String exceptionTip) {
        if (!checkStringNotEmpty(param)) {
            throw new CustomException(exceptionTip);
        }
    }


    public static boolean checkStringNotEmpty(String param) {
        return StringUtils.hasText(param);
    }

    public static void checkStringNotNull(String param,String exceptionTip){
        if(!checkStringNotNull(param)){
            throw new CustomException(exceptionTip);
        }
    }

    public static boolean checkStringNotNull(String param){
       return null!=param;
    }

    public static void checkObjectNotNull(Object param, String exceptionTip) {
        if (!checkObjectNotNull(param)) {
            throw new CustomException(exceptionTip);
        }
    }

    public static boolean checkObjectNotNull(Object param) {
        return param != null;
    }

    /**
     * @return void
     * @Description 验证Array不为null, 且包含元素
     * @Param [params,exceptionTip 异常提示]
     * @Author yc
     * @Date 2020-06-18 15:58
     * @version 1.0
     */

    public static <T> void checkArrayNotEmpty(T[] params, String exceptionTip) {
        if (!checkArrayNotEmpty(params)) {
            throw new CustomException(exceptionTip);
        }
    }

    public static <T> boolean checkArrayNotEmpty(T[] params) {
        return ArrayUtils.isNotEmpty(params);
    }

    public static <T> void checkArrayNotNull(T[] params, String exceptionTip) {
        if (!checkArrayNotNull(params)) {
            throw new CustomException(exceptionTip);
        }
    }

    public static <T> boolean checkArrayNotNull(T[] params) {
        return null!=params;
    }

    /**
     * @return void
     * @Description 验证Collection不为null, 且包含元素
     * @Param [collection，exceptionTip异常提示]
     * @Author yc
     * @Date 2020-06-18 15:59
     * @version 1.0
     */

    public static void checkCollectionNotEmpty(Collection<?> collection, String exceptionTip) {
        if (!checkCollectionNotEmpty(collection)) {
            throw new CustomException(exceptionTip);
        }
    }
    public static boolean checkCollectionNotEmpty(Collection<?> collection) {
        return !CollectionUtils.isEmpty(collection);
    }
    public static boolean checkCollectionNotNull(Collection<?> collection) {
        return null!=collection;
    }

    public static void checkCollectionNotNull(Collection<?> collection, String exceptionTip) {
        if (!checkCollectionNotNull(collection)) {
            throw new CustomException(exceptionTip);
        }
    }
    /**
     * @return void
     * @Description 验证Map不为null, 且包含元素
     * @Param [map,exceptionTip异常提示]
     * @Author yc
     * @Date 2020-06-18 16:00
     * @version 1.0
     */

    public static void checkMapNotEmpty(Map<?, ?> map, String exceptionTip) {

        if (!checkMapNotEmpty(map)) {
            throw new CustomException(exceptionTip);
        }
    }

    public static boolean checkMapNotEmpty(Map<?, ?> map) {

        return !(null == map||map.isEmpty() );

    }

    public static void checkMapNotNull(Map<?, ?> map, String exceptionTip) {

        if (!checkMapNotNull(map)) {
            throw new CustomException(exceptionTip);
        }
    }

    public static boolean checkMapNotNull(Map<?, ?> map) {

        return null!=map;

    }

    /**
     * @return void
     * @Description 验证方法所有参数不为empty
     * @Param [exceptionTip异常提示,params]
     * @Author yc
     * @Date 2020-06-18 16:00
     */

    public static void checkMethodParametersAllNotEmpty(String exceptionTip, Object... params) {

        final long fail = 0L;        // 验证失败标识

        final long success = 1L;     //验证成功标识

        List<Long> collect = Stream.of(params).map((param) -> {

            if (param instanceof String) {
                return checkStringNotEmpty((String) param) ? success : fail;

            } else if (param instanceof Collection) {
                return checkCollectionNotEmpty((Collection) param) ? success : fail;

            } else if (param instanceof Map) {
                return checkMapNotEmpty((Map) param) ? success : fail;

            } else if (param instanceof Object[]) {
                return checkArrayNotEmpty((Object[]) param) ? success : fail;

            } else {
                return checkObjectNotNull(param) ? success : fail;

            }
        }).collect(Collectors.toList());

        if (collect.contains(fail)) {

            throw new CustomException(exceptionTip);
        }
    }

    /**
     * @return void
     * @Description 验证方法所有参数不为null
     * @Param [exceptionTip异常提示,params]
     * @Author yc
     */
    public static void checkMethodParametersAllNotNull(String exceptionTip,Object ...params){

        List<Object> collect = Stream.of(params).filter((p) -> null != p).collect(Collectors.toList());

        if(params.length!=collect.size()){
            throw new CustomException(exceptionTip);
        }
    }
}
