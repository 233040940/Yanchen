package com.local.common.utils;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO redis操作工具类
 * @date 2020-05-28 20:37
 */

@Component
public class RedisTemplateHelper {

    private final RedisTemplate redisTemplate;

    private static final Map EMPTY_MAP=Collections.EMPTY_MAP;

    private static final Set EMPTY_SET=Collections.EMPTY_SET;

    private static final List EMPTY_LIST=Collections.EMPTY_LIST;

    public RedisTemplateHelper(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @return boolean
     * @Description 设置普通字符串, 不带过期时间
     * @Param [key 不能为null, value不能为null]
     * @Author yc
     * @Date 2020-05-28 16:43
     */


    public boolean set(String key, Object value) {

        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return boolean
     * @Description 当前不存在给定key时，设置普通字符串
     * @Param [key, value]
     * @Author yc
     * @Date 2020-05-28 17:42
     * @version 1.0
     */

    public boolean setIfAbsent(String key, Object value) {

        return redisTemplate.opsForValue().setIfAbsent(key, value);

    }

    public boolean setIfAbsent(String key, Object value, long timeOut) {

        return setIfAbsent(key, value, timeOut, TimeUnit.SECONDS);

    }

    public boolean setIfAbsent(String key, Object value, long timeOut, TimeUnit timeUnit) {

        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit);

    }

    /**
     * @return boolean
     * @Description 设置普通字符串
     * @Param [key 不能为null, value 不能为null, timeOut 过期时间，秒级]
     * @Author yc
     * @Date 2020-05-28 16:51
     */

    public boolean setExpire(String key, Object value, long timeOut) {
        return setExpire(key, value, timeOut, TimeUnit.SECONDS);

    }

    /**
     * @return boolean
     * @Description 设置普通字符串
     * @Param [key 不能为null, value 不能为null, timeOut 过期时间，timeUnit 根据时间单位]
     * @Author yc
     * @Date 2020-05-28 16:51
     */
    public boolean setExpire(String key, Object value, long timeOut, TimeUnit timeUnit) {

        try {
            redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @return boolean
     * @Description 设置key过期时间，默认秒级
     * @Param [key 不能为null, timeOut]
     * @Author yc
     */
    public boolean setKeyExpire(String key, long timeOut) {
        return setKeyExpire(key, timeOut, TimeUnit.SECONDS);
    }

    public boolean setKeyExpire(String key, long timeOut, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeOut, timeUnit);
    }

    /**
     * @return long
     * @Description 获取key过期时间，默认秒级
     * @Param [key]
     * @Author yc
     */

    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    public long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * @return boolean
     * @Description 验证是否存在key
     * @Param [key 不能为null]
     * @Author yc
     * @Date 2020-05-28 17:04
     * @version 1.0
     */

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @return boolean
     * @Description 验证是否存在给定的所有key
     * @Param [keys 不能为null]
     * @Author yc
     */
    public boolean hasKeys(Collection<String> keys) {
        return countExistingKeys(keys) == keys.size() ? true : false;
    }

    /**
     * @return long
     * @Description 计算给定的key集合，存在个数
     * @Param [keys 不能为null]
     * @Author yc
     * @Date 2020-05-28 17:08
     * @version 1.0
     */

    public long countExistingKeys(Collection<String> keys) {
        return redisTemplate.countExistingKeys(keys);
    }

    /**
     * @return boolean
     * @Description 删除指定key
     * @Param [key]
     * @Author yc
     * @Date 2020-05-28 17:28
     * @version 1.0
     */

    public boolean delKey(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * @return boolean
     * @Description 批量删除key
     * @Param [keys]
     * @Author yc
     * @Date 2020-05-28 17:30
     * @version 1.0
     */

    public boolean delKeys(String... keys) {
        return delKeys(CollectionUtils.arrayToList(keys));
    }

    public boolean delKeys(Collection<String> keys) {
        return redisTemplate.delete(keys) == keys.size() ? true : false;
    }

    /**
     * @return long
     * @Description 计算删除key成功的个数
     * @Param [keys]
     * @Author yc
     * @Date 2020-05-28 17:29
     * @version 1.0
     */

    public long countDelKeysSuccess(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * @return boolean
     * @Description 通过指定key, 和offset 替换原value中的值
     * @Param [key, value, offset ]
     * @Author yc
     * @Date 2020-05-28 17:51
     * @version 1.0
     */

    public boolean replaceValue(String key, Object value, long offset) {
        try {
            redisTemplate.opsForValue().set(key, value, offset);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return boolean
     * @Description 批量设置多个key value
     * @Param [map]
     * @Author yc
     */

    public boolean batchSet(Map<String, ?> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return boolean
     * @Description 批量设置多个key value，仅当key不存在时
     * @Param [map]
     * @Author yc
     */
    public boolean batchSetIfAbsent(Map<String, ?> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);

    }

    /**
     * @return long
     * @Description 通过key设置value递增，递增因子为1
     * @Param [key]
     * @Author yc
     */

    public long increment(String key) {
        return incrementByLong(key, 1L);
    }

    /**
     * @return long
     * @Description 通过key设置value递增，递增因子为delta
     * @Param [key ,delta 递增因子]
     * @Author yc
     */
    public long incrementByLong(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * @return double
     * @Description 通过key设置value递增，递增因子为delta
     * @Param [key, delta 递增因子]
     * @Author yc
     */

    public double incrementByDouble(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * @return java.util.List<java.lang.Object>
     * @Description 批量获取多个key对应的value
     * @Param [keys]
     * @Author yc
     * @Date 2020-05-29 18:01
     * @version 1.0
     */

    public List<Object> batchGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * @return T
     * @Description 根据key和指定类型获取value
     * @Param [key, tClass]
     * @Author yc
     * @Date 2020-05-29 18:01
     * @version 1.0
     */

    public <T> T get(String key, Class<T> tClass) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o.getClass() == tClass) {
            return (T) o;
        }
        return null;
    }

    /**
     * @return boolean
     * @Description 设置hash的value
     * @Param [key, hashKey, value]
     * @Author yc
     * @Date 2020-05-29 18:03
     * @version 1.0
     */

    public boolean hashSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @return java.util.Map<java.lang.Object       ,       java.lang.Object>
     * @Description 通过key批量获取hash的值
     * @Param [key]
     * @Author yc
     * @Date 2020-05-29 18:50
     * @version 1.0
     */

    public Map<String, Object> hashBatchGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);

    }

    /**
     * @return java.util.Map<java.lang.String               ,               T>
     * @Description 根据指定key和对象类型获取hash值
     * @Param [key, tClass]
     * @Author yc
     */
    public <T> Map<String, T> hashBatchGetAll(String key, Class<T> valueClass) {
        Map<String, Object> entries = redisTemplate.opsForHash().entries(key);
        if (CollectionUtils.isEmpty(entries)) {
            return EMPTY_MAP;
        }

        Map<String, T> result = Maps.newHashMapWithExpectedSize(entries.size());
        entries.entrySet().stream().filter((e) -> e.getValue().getClass() == valueClass).forEach((e) -> result.put(e.getKey(), (T) e.getValue()));
        return result;

    }


    public <T> List<T> hashBatchGet(String key, Collection<String> hashKeys, Class<T> valueClass) {
        List<Object> list = redisTemplate.opsForHash().multiGet(key, hashKeys);
        if (CollectionUtils.isEmpty(list)) {
            return EMPTY_LIST;
        }

        List<T> collect = list.stream().filter((item) -> item != null && item.getClass() == valueClass).map((item) -> (T) item).collect(Collectors.toList());
        return collect;
    }

    /**
     * @return T
     * @Description 根据指定key，hashkey，类型获取value
     * @Param [key, hashKey, tClass]
     * @Author yc
     * @Date 2020-05-29 18:04
     * @version 1.0
     */

    public <T> T hashGet(String key, Object hashKey, Class<T> valueClass) {
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        if (o.getClass() != valueClass) {
            return null;
        }
        return (T) o;

    }

    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * @return java.util.Set<java.lang.String>
     * @Description 获取hash 所有hashKey
     * @Param [key]
     * @Author yc
     */

    public Set<String> getHashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * @return long
     * @Description 设置hash，item 递增
     * @Param [key, hashKey, delta]
     * @Author yc
     */

    public long hashIncrementLong(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public double hashIncrementDouble(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public <T> boolean hashBatchSet(String key, Map<String, T> items) {
        try {
            redisTemplate.opsForHash().putAll(key, items);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean listSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean leftPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean leftPushIfPresent(String key,Object value){

       return redisTemplate.opsForList().leftPushIfPresent(key,value)==1?true:false;
    }

    public long leftPushAll(String key,Collection values) {

      return   redisTemplate.opsForList().leftPushAll(key, values);
    }

    public boolean rightPush(String key,Object value){
        try {
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rightPushIfPresent(String key,Object value){

      return   redisTemplate.opsForList().rightPushIfPresent(key,value)==1?true:false;
    }
    public boolean rightPushAll(String key,Collection values){
        try {
            redisTemplate.opsForList().rightPushAll(key,values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Object leftPop(String key,long timeOut,TimeUnit timeUnit){
        return redisTemplate.opsForList().leftPop(key,timeOut,timeUnit);
    }

    public Object leftPop(String key){
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object rightPop(String key){
        return redisTemplate.opsForList().rightPop(key);
    }

    public boolean listDel(String key,long start,long end){

        try {
            redisTemplate.opsForList().trim(key,start,end);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object listGet(String key,long index){

        return redisTemplate.opsForList().index(key,index);
    }

    public List listRange(String key,long start,long end){

        return redisTemplate.opsForList().range(key, start, end);
    }


    public boolean listRemove(String key,long index,Object value){
        return redisTemplate.opsForList().remove(key, index, value)==1?true:false;
    }

    public Long listSize(String key){

        return  redisTemplate.opsForList().size(key);
    }

    public Object rightPopAndLeftPush(String oldListKey,String newListKey){

        return  redisTemplate.opsForList().rightPopAndLeftPush(oldListKey,newListKey);
    }

    public Object rightPopAndLeftPush(String oldListKey,String newListKey,long blockTimeOut,TimeUnit timeUnit){

        return  redisTemplate.opsForList().rightPopAndLeftPush(oldListKey,newListKey,blockTimeOut,timeUnit);
    }

    public <T> T rightPopAndLeftPush(String oldListKey,String newListKey,Class<T> tClass){

        Object o = rightPopAndLeftPush(oldListKey, newListKey);

        if(o.getClass()!=tClass){
            return null;
        }
        return (T)o;
    }
}
