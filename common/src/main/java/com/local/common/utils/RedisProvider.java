package com.local.common.utils;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO  redis操作工具类
 */

public interface RedisProvider<String, V> {


    /**
     * @return boolean
     * @Description 设置key, value
     * @Param [key, value ,timeOut ,timeUnit]
     * @Author yc
     */

    boolean set(String key, V value);

    boolean set(String key, V value, long timeOut);

    boolean set(String key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @return boolean
     * @Description 如果key(不存在)则设置key，value
     * @Param [key, value,timeOut ,timeUnit]
     * @Author yc
     */

    boolean setIfAbsent(String key, V value);

    boolean setIfAbsent(String key, V value, long timeOut);

    boolean setIfAbsent(String key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @return boolean
     * @Description 如果key(存在)则设置key，value
     * @Param [key, value ,timeOut,timeUnit]
     * @Author yc
     */

    boolean setIfPresent(String key, V value);

    boolean setIfPresent(String key, V value, long timeOut);

    boolean setIfPresent(String key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @return boolean
     * @Description 批量设置
     * @Param [map]
     * @Author yc
     */

    boolean batchSet(Map<String, V> map);

    /**
     * @return boolean
     * @Description 批量设置，仅当给定的key不存在时
     * @Param [map]
     * @Author yc
     */

    boolean batchSetIfAbsent(Map<String, V> map);

    /**
     * @return T
     * @Description 通过指定的key和 value类型，获取value
     * @Param [key, tClass]
     * @Author yc
     */

    V get(String key);

    /**
     * @return java.util.List<T>
     * @Description 批量获取
     * @Param [keys , tClass]
     * @Author yc
     */

   List<V> batchGet(Collection<String> keys);


    /**
     * @return boolean
     * @Description 删除
     * @Param [key]
     * @Author yc
     */

    boolean del(String key);

    /**
     * @return boolean
     * @Description 批量删除
     * @Param [keys]
     * @Author yc
     */

    boolean batchDel(Collection<String> keys);

    /**
     * @return boolean
     * @Description 验证是否存在
     * @Param [key]
     * @Author yc
     * @Date 2020-05-28 21:10
     * @version 1.0
     */

    boolean exist(String key);


    /**
     * @return boolean
     * @Description 批量验证是否存在
     * @Param [keys]
     * @Author yc
     */

    boolean exist(Collection<String> keys);


    /**
     * @return boolean
     * @Description 验证给定keys 存在的个数
     * @Param [keys]
     * @Author yc
     */

    long countStringeysExist(Collection<String> keys);


    /**
     * @return long
     * @Description 通过key获取value的长度
     * @Param [key]
     * @Author yc
     */

    long valueLength(String key);


    /**
     * @return long
     * @Description 通过key设置value递增
     * @Param [key, delta 递增因子]
     * @Author yc
     */

    long incrementLong(String key);

    long incrementLong(String key, long delta);

    double incrementDouble(String key, double delta);


    /**
     * @return V
     * @Description 通过key设置value，并返回old value
     * @Param [key, value]
     * @Author yc
     */

    V getAndSet(String key, V value);

    /**
     * @return int 返回新的value 长度
     * @Description 通过key，value连接old value的值
     * @Param [key, value]
     * @Author yc
     */

    int append(String key, V value);

    /**
     * @return long
     * @Description 通过key 和给定的hashkey 删除value
     * @Param [key, hashStringeys]
     * @Author yc
     */

    long delete(String key, String... hashKeys);

    /**
     * @return java.util.Map<String   ,   V>
     * @Description 通过key检索hash value
     * @Param [key]
     * @Author yc
     */

    Map<String, V> entries(String key);


    V hGet(String key, String hashKey);

    boolean hasHashKey(String key, String hashKey);

    long hashIncreLong(String key, String hashKey, long delta);

    double hashIncreDouble(String key, String hashKey, double delta);
}


