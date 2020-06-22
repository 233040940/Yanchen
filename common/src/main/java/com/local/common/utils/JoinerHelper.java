package com.local.common.utils;

import com.google.common.base.Joiner;

import java.util.Map;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO  连接字符串工具类
 * @date 2020-06-02 16:55
 */
public class JoinerHelper {


    private JoinerHelper(){

        throw new RuntimeException("JoinerHelper not support instantiated");
    }

   /**
     * @Description 通过指定的分隔符号，连接字符串数组
     * @Param [separator 分隔符 , params 字符串数组]
     * @return java.lang.String
     * @Author yc
     * @Date 2020-06-02 17:03
     */

    public static  String join(String separator,String...params){

       return Joiner.on(separator).join(params);
    }


    public static String join(String separator, String keyValueSeparator, Map map){

        return Joiner.on(separator).withKeyValueSeparator(keyValueSeparator).join(map);
    }

    public static String join(String separator,  Iterable<?> iterable){

        return Joiner.on(separator).skipNulls().join(iterable);
    }

}
