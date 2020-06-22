package com.local.common.utils;

import com.google.common.base.Splitter;
import java.util.List;
import java.util.Map;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description   分割字符工具类
 * @date 2020-06-02 16:54
 */
public class SplitterHelper {


    private SplitterHelper() {

        throw new RuntimeException("SplitterHelper is tool class,Not support instantiated");
    }

    public static String splitReturnFirst(String separator,String target){

        return splitToList(separator, target).stream().findFirst().get();
    }

    public static String splitReturnLast(String separator,String target){

        List<String> list = splitToList(separator, target);

        return list.get(list.size()-1);
    }
    public static List<String> splitToListByFixedLength(int length,String target){

        return Splitter.fixedLength(length).splitToList(target);
    }

    public static List<String> splitToList(String separator, String target) {

        return Splitter.on(separator).trimResults().splitToList(target);
    }

    public static Map<String,String> splitToMap(String separator, String keyValueSeparator, String target) {

        return Splitter.on(separator).withKeyValueSeparator(keyValueSeparator).split(target);
    }

}
