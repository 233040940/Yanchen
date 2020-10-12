package com.local.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

/**
 * @author yc
 * @description   Jackson工具类
 * @date 2020-05-23 14:06
 */
public class JsonHelper {


    private static final ObjectMapper MAPPER;

    static {

        MAPPER = new ObjectMapper();
        //设置序列化规则，不包含null值
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //设置反序列化，忽略找不到属性异常
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        //设置反序列化，允许单个值为数组或集合
        MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    }


    private JsonHelper() {

        throw new RuntimeException("JsonHelper is tool class, Not support instantiated");
    }


    enum JSONValueBaseType {

        STRING() {
            @Override
            Object getJsonNodeValue(final String json, final String nodeName) throws IOException {

                JsonNode jsonNode = MAPPER.readTree(json);

                return jsonNode.findValue(nodeName).asText();
            }
        },

        LONG() {
            @Override
            Object getJsonNodeValue(final String json, final String nodeName) throws IOException {

                JsonNode jsonNode = MAPPER.readTree(json);

                return jsonNode.findValue(nodeName).asLong();
            }
        },


        INTEGER() {
            @Override
            Object getJsonNodeValue(final String json, final String nodeName) throws IOException {

                JsonNode jsonNode = MAPPER.readTree(json);

                return jsonNode.findValue(nodeName).asInt();
            }
        },


        BOOLEAN() {
            @Override
            Object getJsonNodeValue(final String json, final String nodeName) throws IOException {

                JsonNode jsonNode = MAPPER.readTree(json);

                return jsonNode.findValue(nodeName).asBoolean();
            }
        },


        DOUBLE() {
            @Override
            Object getJsonNodeValue(final String json, final String nodeName) throws IOException {
                JsonNode jsonNode = MAPPER.readTree(json);

                return jsonNode.findValue(nodeName).asDouble();
            }
        },

        LIST() {
            @Override
            Object getJsonNodeValue(final String json, final String nodeName) throws IOException {

                return MAPPER.readTree(json).findPath(nodeName).toString();
            }
        };

        abstract Object getJsonNodeValue(final String json, final String nodeName) throws IOException;
    }


    /**
     * @return T
     * @Description 通过json字符串反序列化成java对象
     * @Param [jsonFormat json字符串, targetClass 反序列化类型]
     * @Author yc
     */

    public static <T> T deSerialize(final String json, final Class<T> targetClass) throws IOException {

        return MAPPER.readValue(json, targetClass);
    }

    /**
     * @return java.lang.String
     * @Description 序列化目标对象到json字符串
     * @Param [target 目标对象]
     * @Author yc
     * @Date 2020-05-23 17:32
     * @version 1.0
     */

    public static String serialize(final Object target) throws JsonProcessingException {

        return MAPPER.writeValueAsString(target);
    }


    /**
     * @return java.lang.Object
     * @Description 根据json字符串，获取指定属性的值.[备注:只查找一级json，不包含嵌套]
     * @Param [json 字符串, nodeName 目标node key, valueType 值类型]
     * @Author yc
     */

    public static Object getJsonNodeValue(final String json, final String nodeName, final JSONValueBaseType valueType) throws IOException {

        return valueType.getJsonNodeValue(json, nodeName);

    }

    /**
     * @return java.lang.String
     * @Description 根据json字符串，获取指定属性的值.【支持多级json嵌套，但是json字符串中不能包含重名的key】
     * @Param [json, nodeName 目标node key]
     * @Author yc
     * @Date 2020-05-24 17:50
     * @version 1.0
     */

    public static String getJsonNodeValue(final String json, final String nodeName) throws IOException {

        return MAPPER.readTree(json).findValue(nodeName).toString();
    }

    /**
     * @return java.lang.String
     * @Description 根据json字符串，获取指定属性的值.【支持多级json嵌套，json字符串中可以包含重名的key，由parentNode区分】
     * @Param [json, parentNode 上级node key , nodeName 目标node key]
     * @Author yc
     * @Date 2020-05-24 17:54
     * @version 1.0
     */


    public static String getJsonNodeValue(final String json, final String parentNode, final String nodeName) throws IOException {

        JsonNode jsonNode = MAPPER.readTree(json).findPath(parentNode);

        return getJsonNodeValue(jsonNode.toString(), nodeName);
    }


    /**
     * @return java.util.List<T>
     * @Description 根据json字符串，反序列化到指定类型集合
     * @Param [json, tClass 目标类型]
     * @Author yc
     * @version 1.0
     */

    public static <T> List<T> deSerializeToList(final String json, Class<T> tClass) throws IOException {

        return MAPPER.readValue(json, new TypeReference<List<T>>() {
        });
    }

    /**
     * @Description 根据json字符串，反序列化到map
     * @Param [json]
     * @Author yc
     * @Date 2020-05-24 15:05
     * @version 1.0
     */

    public static <T> Map<String, T> deSerializeToMap(final String json, Class<T> tClass) throws IOException {

        return MAPPER.readValue(json, new TypeReference<Map<String, T>>() {
        });
    }

    /**
     * @return T
     * @Description 根据json字符串，和key将目标node 反序列为对象
     * @Param [json, nodeName key, tClass 类型]
     * @Author yc
     */

    public static <T> T jsonNodeConvertObject(final String json, final String nodeName, Class<T> tClass) throws IOException {

        JsonNode jsonNode = MAPPER.readTree(json).findPath(nodeName);

        return deSerialize(jsonNode.toString(), tClass);
    }

    /**
     * @return T
     * @Description 根据json字符串，和key将目标node 反序列为对象集合
     * @Param [json, nodeName key, tClass 类型]
     * @Author yc
     */

    public static <T> List<T> jsonNodeConvertList(final String json, final String nodeName, Class<T> tClass) throws IOException {

        JsonNode jsonNode = MAPPER.readTree(json).findPath(nodeName);

        return deSerializeToList(jsonNode.toString(), tClass);
    }

}