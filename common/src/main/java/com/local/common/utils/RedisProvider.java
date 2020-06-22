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

public interface RedisProvider<K, V> {


    /**
     * @return boolean
     * @Description 设置key, value
     * @Param [key, value ,timeOut ,timeUnit]
     * @Author yc
     */

    boolean set(K key, V value);

    boolean set(K key, V value, long timeOut);

    boolean set(K key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @return boolean
     * @Description 如果key(不存在)则设置key，value
     * @Param [key, value,timeOut ,timeUnit]
     * @Author yc
     */

    boolean setIfAbsent(K key, V value);

    boolean setIfAbsent(K key, V value, long timeOut);

    boolean setIfAbsent(K key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @return boolean
     * @Description 如果key(存在)则设置key，value
     * @Param [key, value ,timeOut,timeUnit]
     * @Author yc
     */

    boolean setIfPresent(K key, V value);

    boolean setIfPresent(K key, V value, long timeOut);

    boolean setIfPresent(K key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @return boolean
     * @Description 批量设置
     * @Param [map]
     * @Author yc
     */

    boolean batchSet(Map<K, V> map);

    /**
     * @return boolean
     * @Description 批量设置，仅当给定的key不存在时
     * @Param [map]
     * @Author yc
     */

    boolean batchSetIfAbsent(Map<K, V> map);

    /**
     * @return T
     * @Description 通过指定的key和 value类型，获取value
     * @Param [key, tClass]
     * @Author yc
     */

    V get(K key);

    /**
     * @return java.util.List<T>
     * @Description 批量获取
     * @Param [keys , tClass]
     * @Author yc
     */

   List<V> batchGet(Collection<K> keys);


    /**
     * @return boolean
     * @Description 删除
     * @Param [key]
     * @Author yc
     */

    boolean del(K key);

    /**
     * @return boolean
     * @Description 批量删除
     * @Param [keys]
     * @Author yc
     */

    boolean batchDel(Collection<K> keys);

    /**
     * @return boolean
     * @Description 验证是否存在
     * @Param [key]
     * @Author yc
     * @Date 2020-05-28 21:10
     * @version 1.0
     */

    boolean exist(K key);


    /**
     * @return boolean
     * @Description 批量验证是否存在
     * @Param [keys]
     * @Author yc
     */

    boolean exist(Collection<K> keys);


    /**
     * @return boolean
     * @Description 验证给定keys 存在的个数
     * @Param [keys]
     * @Author yc
     */

    long countKeysExist(Collection<K> keys);


    /**
     * @return long
     * @Description 通过key获取value的长度
     * @Param [key]
     * @Author yc
     */

    long valueLength(K key);


    /**
     * @return long
     * @Description 通过key设置value递增
     * @Param [key, delta 递增因子]
     * @Author yc
     */

    long incrementLong(K key);

    long incrementLong(K key, long delta);

    double incrementDouble(K key, double delta);


    /**
     * @return V
     * @Description 通过key设置value，并返回old value
     * @Param [key, value]
     * @Author yc
     */

    V getAndSet(K key, V value);

    /**
     * @return int 返回新的value 长度
     * @Description 通过key，value连接old value的值
     * @Param [key, value]
     * @Author yc
     */

    int append(K key, V value);

    /**
     * @return long
     * @Description 通过key 和给定的hashkey 删除value
     * @Param [key, hashKeys]
     * @Author yc
     */

    long delete(K key, Object... hashKeys);

    /**
     * @return java.util.Map<K   ,   V>
     * @Description 通过key检索hash value
     * @Param [key]
     * @Author yc
     */

    Map<K, V> entries(K key);


    V hGet(K key, Object hashKey);

    boolean hasKey(K key, Object hashKey);

    long hIncrementLong(K key, Object hashKey, long delta);

    double hIncrementDouble(K key, Object hashKey, double delta);
}


