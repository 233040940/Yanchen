package com.local.common.utils;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public interface RedisOperationProvider<K,V> {

    /**
     * @description: 仅设置key过期时间(默认，单位 秒)。
     * @create-by: yanchen @date:2020-08-19 14:17
     * @param key
     * @param timeOut 过期时间
     * @return: boolean
     */
     boolean setKeyExpire(K key, long timeOut);

     boolean setKeyExpire(K key, long timeOut, TimeUnit timeUnit);

    /**
     * @description: 设置key在某个时间过期
     * @create-by: yanchen @date:2020-08-19 19:10
     * @param key
     * @param date
     * @return: java.lang.Boolean
     */
     Boolean setKeyExpireAt(K key, Date date);
    /**
     * @description: 获取key的过期时间(默认，单位 秒).
     * @create-by: yanchen @date:2020-08-19 14:19
     * @param key
     * @return: long
     */
     long getExpire(K key);

     long getExpire(K key, TimeUnit timeUnit);
    /**
     * @description: 检索是否存在指定key
     * @create-by: yanchen @date:2020-08-19 14:20
     * @param key
     * @return: boolean
     */
     boolean hasKey(K key);
    /**
     * @description: 检索是否包含指定集合中的所有key
     * @create-by: yanchen @date:2020-08-19 14:20
     * @param keys
     * @return: boolean
     */
     boolean hasKeys(Collection<K> keys);
    /**
     * @description: 计算给定集合keys中存在的key数量
     * @create-by: yanchen @date:2020-08-19 14:23
     * @param keys
     * @return: long
     */
     long countExistingKeys(Collection<K> keys);
    /**
     * @description: 根据key删除指定的value
     * @create-by: yanchen @date:2020-08-19 14:24
     * @param key
     * @return: boolean
     */
     boolean delKey(K key);
    /**
     * @description: 删除指定[数组、集合]keys中包含的所有value
     * @create-by: yanchen @date:2020-08-19 14:25
     * @param keys
     * @return: boolean
     */
     boolean delKeys(K... keys);
     boolean delKeys(Collection<K> keys);
    /**
     * @description: 将oldKey重命名newKey
     * @create-by: yanchen @date:2020-08-19 18:57
     * @param oldKey
     * @param newKey
     * @return: boolean
     */
     boolean renameKey(K oldKey,K newKey);
    /**
     * @description: 如果存在oldKey则替换为newKey
     * @create-by: yanchen @date:2020-08-19 19:04
     * @param oldKey
     * @param newKey
     * @return: boolean
     */
     boolean renameKeyIfAbsent(K oldKey,K newKey);
    /**
     * @description: 移除给定key的过期时间
     * @create-by: yanchen @date:2020-08-19 19:02
     * @param key
     * @return: boolean
     */
     boolean persist(K key);

    /**
     * @description: 删除给定集合keys对应的所有value，并返回删除成功个数
     * @create-by: yanchen @date:2020-08-19 14:28
     * @param keys
     * @return: long
     */
     long countDelKeysSuccess(Collection<K> keys);

    /**
     * @description: 发布消息
     * @create-by: yanchen @date:2020-09-03 22:10 
     * @param channel 频道名称
     * @param message 消息
     * @return: void
     */
    void publishMessage(String channel,Object message);
     
    //============================================String=============================================\\
    /**
     * @description: 设置普通字符串的值
     * @create-by: yanchen @date:2020-08-19 14:12
     * @param key
     * @param value
     * @return: boolean
     */
     boolean set(K key, V value);

    /**
     * @description: 设置普通字符串的值，并添加过期时间（(默认，单位 秒)）。##原子操作
     * @create-by: yanchen @date:2020-08-19 14:14
     * @param key
     * @param value
     * @param timeOut  过期时间
     * @return: boolean
     */
    boolean set(K key, V value, long timeOut);

    boolean set(K key, V value, long timeOut, TimeUnit timeUnit);
   
   /**
    * @description: 设置key 所储存的字符串指定偏移量上的位(bit)
    * @create-by: yanchen @date:2020-09-03 20:17 
    * @param key
    * @param offset
    * @param value
    * @return: boolean
    */
   boolean setBit(K key, long offset, boolean value);

   /**
    * @description: 获取key 所储存的字符串指定偏移量上的位(bit)
    * @create-by: yanchen @date:2020-09-03 20:17 
    * @param key
    * @param offset
    * @return: boolean
    */
   boolean getBit(K key, long offset);

   /**
    * @description: 获取/操作存储的可变位宽度和任意对齐偏移量的特定整数字段
    * @create-by: yanchen @date:2020-09-03 20:17 
    * @param key
    * @param subCommands
    * @return: java.util.List<java.lang.Long>
    */
   List<Long> bitFields(K key, BitFieldSubCommands subCommands);

    /**
     * @description: 仅当key不存在时，设置普通字符串的值。##原子操作
     * @create-by: yanchen @date:2020-08-19 14:13
     * @param key
     * @param value
     * @return: boolean
     */
     boolean setIfAbsent(K key, V value);

     boolean setIfAbsent(K key, V value, long timeOut);

     boolean setIfAbsent(K key, V value, long timeOut, TimeUnit timeUnit);

    /**
     * @description: 通过给定key，替换指定位置offset的value
     * @create-by: yanchen @date:2020-08-19 14:30
     * @param key
     * @param value
     * @param offset  >=0
     * @return: boolean
     */
     boolean replaceValue(K key, V value, long offset);

    /**
     * @description: 通过指定Map批量设置key-value
     * @create-by: yanchen @date:2020-08-19 14:45
     * @param entry
     * @return: boolean
     */
     boolean batchSet(Map<K, ?> entry);

    /**
     * @description: 通过指定Map，批量设置key-value，仅当key不存在 ##原子操作
     * @create-by: yanchen @date:2020-08-19 14:47
     * @param entry
     * @return: boolean
     */
     boolean batchSetIfAbsent(Map<K, ?> entry);

    /**
     * @description: 通过指定key设置value自增量delta（默认delta为1）
     * @create-by: yanchen @date:2020-08-19 14:51
     * @param key
     * @return: long
     */
     long increment(K key);

     long incrementByLong(K key, long delta);

    /**
     * @description: 通过指定key设置value自增量delta
     * @create-by: yanchen @date:2020-08-19 14:53
     * @param key
     * @param delta
     * @return: double
     */
     double incrementByDouble(K key, double delta);

    /**
     * @description: 通过指定key设置value自减量delta（默认delta为1）
     * @create-by: yanchen @date:2020-08-19 18:29
     * @param key
     * @return: long
     */
     long decrement(K key);

     long decrement(K key,long delta);

    /**
     * @description: 通过指定集合中的keys，返回与之匹配的所有value
     * @create-by: yanchen @date:2020-08-19 14:54
     * @param keys
     * @return: java.util.List<java.lang.V>
     */
     List<V> batchGet(Collection<K> keys);

    /**
     * @description: 通过指定key,获取value的值
     * @create-by: yanchen @date:2020-08-31 19:13
     * @param key
     * @return: V
     */
    V get(K key);

    /**
     * @description: 通过指定key，value设置key新值为value，并返回老的value。如果key不存在则返回null
     * @create-by: yanchen @date:2020-08-19 18:18
     * @param key
     * @param value
     * @return: java.lang.V
     */
     V getAndSet(K key,V value);

    /**
     * @description: 通过指定key，将value的值连接到key所对应值后
     * @create-by: yanchen @date:2020-08-19 18:34
     * @param key
     * @param value
     * @return: void
     */
     void append(K key,K value);

    //============================================Hash=============================================\\

    /**
     * @description: 通过指定key，hashKey，value设置Hash数据结构的值。
     * @create-by: yanchen @date:2020-08-19 14:58
     * @param key
     * @param hashKey
     * @param value
     * @return: boolean
     */
     boolean hashSet(K key, K hashKey, V value);

    /**
     * @description: 通过指定key，批量设置hash数据结构中的entries
     * @create-by: yanchen @date:2020-08-19 15:29
     * @param key
     * @param entries
     * @return: boolean
     */
      boolean hashBatchSet(K key, Map<K, ?> entries);

    /**
     * @description: 仅当存在key，hashKey时修改value的值 ##原子操作
     * @create-by: yanchen @date:2020-08-19 19:38
     * @param key
     * @param hashKey
     * @param value
     * @return: boolean
     */
     boolean hashSetIfAbsent(K key,K hashKey,V value);

    /**
     * @description: 通过指定key，hashKey获取hash数据结构中value的值
     * @create-by: yanchen @date:2020-08-19 15:07
     * @param key
     * @param hashKey
     * @return: java.lang.V
     */
     V hashGet(K key, K hashKey);

    /**
     * @description: 通过指定key获取hash结构中的所有entries[field，value].
     * @create-by: yanchen @date:2020-08-19 15:01
     * @param key
     * @return: java.util.Map<java.lang.K , java.lang.V>
     */
     Map<K, V> hashBatchGetAll(K key);

    /**
     * @description: 通过指定key、hashKey集合、和value类型获取hash结构中所匹配的entries[field，value].前提条件：value都属于同一类型
     * @create-by: yanchen @date:2020-08-19 15:13
     * @param key
     * @param hashKeys
     * @return: java.util.List<T>
     */
      List<V> hashBatchGet(K key, Collection<K> hashKeys);

    /**
     * @description: 通过指定key，获取hash结构中对应的所有fields
     * @create-by: yanchen @date:2020-08-19 15:21
     * @param key
     * @return: java.util.Set<java.lang.K>
     */
     Set<K> getHashKeys(K key);

    /**
     * @description: 通过指定key，hashKey设置hash结构中value自增量为delta(默认delta为1)
     * @create-by: yanchen @date:2020-08-19 15:22
     * @param key
     * @param hashKey
     * @return: long
     */
     long hashIncrementLong(K key,K hashKey);

     long hashIncrementLong(K key, K hashKey, long delta);

    /**
     * @description: 通过指定key，hashKey设置hash结构中value自增量为delta
     * @create-by: yanchen @date:2020-08-19 15:25
     * @param key
     * @param hashKey
     * @param delta
     * @return: double
     */
     double hashIncrementDouble(K key, K hashKey, double delta);

    /**
     * @description: 检索hash结构中是否存在指定的key，hashKey映射
     * @create-by: yanchen @date:2020-08-19 15:26
     * @param key
     * @param hashKey
     * @return: boolean
     */
     boolean hasHashKey(K key, K hashKey);

    /**
     * @description: 通过指定key，hashKeys集合删除指定的value,返回成功个数
     * @create-by: yanchen @date:2020-08-19 19:50
     * @param key
     * @param hashKeys
     * @return: void
     */
     long hashDel(K key,Collection<K> hashKeys);

    /**
     * @description: 通过指定key获取hash中元素个数
     * @create-by: yanchen @date:2020-08-19 19:53
     * @param key
     * @return: long
     */
     long hashSize(K key);

    /**
     * @description: 扫描hash数据结构
     * @create-by: yanchen @date:2020-09-03 19:42 
     * @param key
     * @param scanOptions 扫描选项
     * @return: org.springframework.data.redis.core.Cursor<java.util.Map.Entry < K , V>>
     */
    Cursor<Map.Entry<K, V>>  hashScan(K key,ScanOptions scanOptions);

    //============================================List=============================================\\

    /**
     * @description: 通过指定key，修改list数据结构中index处的value
     * @create-by: yanchen @date:2020-08-19 15:37
     * @param key
     * @param index
     * @param value
     * @return: boolean
     */
     boolean listSet(K key, long index, V value);

    /**
     * @description: 通过指定key,value向list数据结构左边添加元素
     * @create-by: yanchen @date:2020-08-19 15:44
     * @param key
     * @param value
     * @return: boolean
     */
     boolean listLeftPush(K key, V value);

    /**
     * @description: 通过指定key,value向list数据结构左边添加元素，仅当key存在
     * @create-by: yanchen @date:2020-08-19 15:44
     * @param key
     * @param value
     * @return: boolean
     */
     boolean listLeftPushIfPresent(K key, V value);

    /**
     * @description: 通过指定key，values集合向list数据结构左边批量添加元素，并返回成功个数
     * @create-by: yanchen @date:2020-08-19 15:51
     * @param key
     * @param values
     * @return: long
     */
     long listLeftPushAll(K key, Collection<V> values);

    /**
     * @description: 通过指定key,value向list数据结构右边添加元素。
     * @create-by: yanchen @date:2020-08-19 15:44
     * @param key
     * @param value
     * @return: boolean
     */
     boolean listRightPush(K key, V value);

    /**
     * @description: 通过指定key,value向list数据结构右边添加元素，仅当key存在。
     * @create-by: yanchen @date:2020-08-19 15:44
     * @param key
     * @param value
     * @return: boolean
     */
     boolean listRightPushIfPresent(K key, V value);

    /**
     * @description: 通过指定key，values集合向list数据结构右边批量添加元素，并返回成功个数
     * @create-by: yanchen @date:2020-08-19 15:51
     * @param key
     * @param values
     * @return: long
     */
     long listRightPushAll(K key, Collection<V> values);

    /**
     * @description: 通过指定key，返回并删除list数据结构中左边的第一个元素，如果不存在元素则返回null。
     * @create-by: yanchen @date:2020-08-19 15:57
     * @param key
     * @return: java.lang.V
     */
     V listLeftPop(K key);

    /**
     * @description: 通过指定key，timeOut，timeUnit返回并删除list数据结构中左边的第一个元素，如果不存在目标元素则阻塞等待timeOut。
     *               超过阻塞时间依然无法获取元素，则返回null
     * @create-by: yanchen @date:2020-08-19 16:12
     * @param key
     * @param timeOut 阻塞时间
     * @param timeUnit 时间单位
     * @return: java.lang.V
     */
     V listLeftBlockPop(K key, long timeOut, TimeUnit timeUnit);

    /**
     * @description: 通过指定key，返回并删除list数据结构中右边的第一个元素，如果不存在元素则返回null。
     * @create-by: yanchen @date:2020-08-19 15:57
     * @param key
     * @return: java.lang.V
     */
     V listRightPop(K key);

    /**
     * @description: 通过指定key，timeOut，timeUnit返回并删除list数据结构中右边的第一个元素，如果不存在目标元素则阻塞等待timeOut。
     *               超过阻塞时间依然无法获取元素，则返回null
     * @create-by: yanchen @date:2020-08-19 16:12
     * @param key
     * @param timeOut 阻塞时间
     * @param timeUnit 时间单位
     * @return: java.lang.V
     */
     V listRightBlockPop(K key,long timeOut,TimeUnit timeUnit);

    /**
     * @description: 通过rightKey返回并移除该list数据结构中最后一个元素,并将该元素push到leftKey所在list中。
     *               如果不存在rightKey所对应的目标集合，或该集合不包含元素，则返回null
     * @create-by: yanchen @date:2020-08-19 16:56
     * @param rightKey 数据来源key
     * @param leftKey  数据目的地key
     * @return: java.lang.V
     */
     V listRightPopAndLeftPush(K rightKey, K leftKey);

    /**
     * @description: 通过rightKey返回并移除该list数据结构中最后一个元素,并将该元素push到leftKey所在list中。如果rightKey所对应的list集合中不包含任何元素，则阻塞等待blockTimeOut；
     *               超过阻塞时间，依然无法获取目标元素，则返回null。
     * @create-by: yanchen @date:2020-08-19 17:10
     * @param rightKey 数据来源key
     * @param leftKey  数据目的地key
     * @param blockTimeOut 阻塞时间
     * @param timeUnit   时间单位
     * @return: java.lang.V
     */
     V listRightPopAndLeftPush(K rightKey, K leftKey, long blockTimeOut, TimeUnit timeUnit);

    /**
     * @description: 对指定key所对应的list数据结构进行截取，只保留【start-end]
     * @create-by: yanchen @date:2020-08-19 17:29
     * @param key
     * @param start
     * @param end
     * @return: boolean
     */
     boolean subList(K key, long start, long end);

    /**
     * @description: 通过指定key，index获取list数据结构中所对应index的value,如果不存在该元素则返回null
     * @create-by: yanchen @date:2020-08-19 17:40
     * @param key
     * @param index
     * @return: java.lang.V
     */
     V listGet(K key, long index);

    /**
     * @description: 通过指定key,获取对应的list数据。
     * @create-by: yanchen @date:2020-08-19 17:44
     * @param key
     * @param start
     * @param end
     * @return: java.util.List
     */
      List<V> listRange(K key, long start, long end);

    /**
     * @description: 通过指定key,count,value移除list数据结构中对应的数据；
     * @create-by: yanchen @date:2020-08-19 17:50
     * @param key
     * @param count 如果:count>0从表头开始向表尾搜索,移除与value相等的元素,数量为count;
     *              如果:count<0从表尾开始向表头搜索,移除与value相等的元素,数量为count的绝对值;
     *              如果:count=0移除表中所有与value相等的值;
     * @param value
     * @return: long 被移除元素的数量,列表不存在时返回0.
     */
     long listRemove(K key, long count, V value);

    /**
     * @description: 通过指定key获取对应list数据结构中item长度
     * @create-by: yanchen @date:2020-08-19 17:56
     * @param key
     * @return: java.lang.Long
     */
     long listSize(K key);
    
    //============================================Set=============================================\\

    /**
     * @description: 通过指定key，和集合values添加到set中,并返回添加成功的个数
     * @create-by: yanchen @date:2020-08-19 20:13
     * @param key
     * @param values
     * @return: long
     */
     long setAdd(K key,V ... values);

    /**
     * @description: 通过指定key，和集合values移除set中存在的值，并返回成功个数
     * @create-by: yanchen @date:2020-08-30 15:53
     * @param key
     * @param values
     * @return: long
     */
     long setRemove(K key,V ... values);

    /**
     * @description: 通过指定key，移除并返回set中的一个随机元素
     * @create-by: yanchen @date:2020-08-30 16:06
     * @param key
     * @return: java.lang.V
     */
     V setPop(K key);

    /**
     * @description: 通过指定key，移除并返回set中的count个随机元素
     * @create-by: yanchen @date:2020-08-30 16:06
     * @param key
     * @param count
     * @return: java.util.List
     */
     List<V> setPops(K key,long count);

    /**
     * @description: 将目标key中，value的值移动到目的地destKey集合中
     * @create-by: yanchen @date:2020-08-30 16:09
     * @param key  目标key
     * @param value 目标值
     * @param destKey 目的地key
     * @return: boolean
     */
     boolean setMove(K key, V value, K destKey);

    /**
     * @description: 获取set的成员个数
     * @create-by: yanchen @date:2020-08-30 16:12
     * @param key
     * @return: long
     */
     long setSize(K key);

   /**
    * @description: 扫描set数据结构
    * @create-by: yanchen @date:2020-09-03 19:50 
    * @param key
    * @param options
    * @return: org.springframework.data.redis.core.Cursor<V>
    */
   Cursor<V> setScan(K key, ScanOptions options);


    /**
     * @description: 验证value是否存在set中
     * @create-by: yanchen @date:2020-08-30 16:13
     * @param key
     * @param value
     * @return: boolean
     */
     boolean setExist(K key,V value);

    /**
     * @description: 通过指定key，和otherKey求交集.如果存在空的集合，则返回空集
     * @create-by: yanchen @date:2020-08-30 16:16
     * @param key
     * @param otherKey
     * @return: java.util.Set<java.lang.V>
     */
     Set<V> setIntersect(K key,K otherKey);

     Set<V> setIntersect(K key,Collection<K> otherKeys);

    /**
     * @description: 通过指定key，和otherKey求交集.并将交集储存到destKey所对应的set中
     * @create-by: yanchen @date:2020-08-31 20:35
     * @param key
     * @param otherKey
     * @param destKey
     * @return: java.lang.Long
     */
     Long setIntersectAndStore(K key, K otherKey, K destKey);

     Long setIntersectAndStore(K key, Collection<K> otherKeys, K destKey);

    /**
     * @description: 通过指定key，和otherKey求并集
     * @create-by: yanchen @date:2020-08-31 20:38
     * @param key
     * @param otherKey
     * @return: java.util.Set<V>
     */
     Set<V> setUnion(K key, K otherKey);

     Set<V> setUnion(K key, Collection<K> otherKeys);

    /**
     * @description: 通过指定key，和otherKey求并集.将并集储存到destKey所对应的set中
     * @create-by: yanchen @date:2020-08-31 20:40
     * @param key
     * @param otherKey
     * @param destKey
     * @return: java.lang.Long
     */
     Long setUnionAndStore(K key, K otherKey, K destKey);

     Long setUnionAndStore(K key, Collection<K> otherKeys, K destKey);

     /**
      * @description: 通过指定key，和otherKey求差集
      * @create-by: yanchen @date:2020-08-31 20:42
      * @param key
      * @param otherKey
      * @return: java.util.Set<V>
      */
     Set<V> setDifference(K key, K otherKey);

     Set<V> setDifference(K key, Collection<K> otherKeys);

    /**
     * @description: 通过指定key，和otherKey求差集.将差集储存到destKey所对应的set中
     * @create-by: yanchen @date:2020-08-31 20:43
     * @param key
     * @param otherKey
     * @param destKey
     * @return: java.lang.Long
     */
     Long setDifferenceAndStore(K key, K otherKey, K destKey);

     Long setDifferenceAndStore(K key, Collection<K> otherKeys, K destKey);

    /**
     * @description: 通过指定key，获取set集合中所有成员
     * @create-by: yanchen @date:2020-08-31 20:50
     * @param key
     * @return: java.util.Set<V>
     */
    Set<V> setMembers(K key);

    /**
     * @description: 通过指定key，随机获取set集合中的一个成员
     * @create-by: yanchen @date:2020-08-31 20:51
     * @param key
     * @return: V
     */
    V setRandomMember(K key);

    /**
     * @description: 通过指定key,随机获取指定count个集合中的成员
     * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。
     * 如果 count 大于等于集合基数，那么返回整个集合。
     * count不能为负数
     * @create-by: yanchen @date:2020-08-31 20:54
     * @param key
     * @param count
     * @throws IllegalArgumentException 如果count小于0
     * @return: java.util.List<V>
     */
    List<V> setRandomMembers(K key, long count) throws IllegalArgumentException;

   /**
    * @description: 通过指定key,随机获取指定count个集合中的成员并去重
    * @create-by: yanchen @date:2020-09-02 21:57
    * @param key
    * @param count
    * @return: java.util.Set<V>
    */
    Set<V> setDistinctRandomMembers(K key, long count)throws IllegalArgumentException;

    //============================================Sorted Set=============================================\\

    /**
     * @description: 向sorted set数据结构中添加元素
     * @create-by: yanchen @date:2020-08-31 21:21
     * @param key
     * @param value
     * @param score
     * @return: java.lang.Boolean
     */
    Boolean zsetAdd(K key, V value, double score);

   /**
    * @description: 向sorted set数据结构中批量添加元素
    * @create-by: yanchen @date:2020-08-31 21:25
    * @param key
    * @param tuples
    * @return: java.lang.Long
    */
   Long zsetBatchAdd(K key, Set<ZSetOperations.TypedTuple<V>> tuples);
   
   /**
    * @description: 移除有序集中的一个或多个成员，不存在的成员将被忽略。并返回移除成功的个数
    * @create-by: yanchen @date:2020-09-02 20:14
    * @param key
    * @param values
    * @return: java.lang.Long
    */
   Long zsetRemove(K key, Object... values);

  /**
   * @description: 通过指定key和value将score自增delta。
   * 如果:delta小于0，则将score自减delta。
   * @create-by: yanchen @date:2020-09-02 20:15
   * @param key
   * @param value
   * @param delta
   * @return: java.lang.Double
   */
   Double zsetIncrementScore(K key, V value, double delta);
  
  /**
   * @description: 通过指定key和value获取对应的排名(从小到大升序，排名第一为0)
   * @create-by: yanchen @date:2020-09-02 20:31 
   * @param key
   * @param value
   * @return: java.lang.Long
   */
   Long zsetRank(K key, Object value);

  /**
   * @description: 通过指定key和value获取对应的排名(从大到小降序，排名第一为0)
   * @create-by: yanchen @date:2020-09-02 20:47
   * @param key
   * @param value
   * @return: java.lang.Long
   */
   Long zsetReverseRank(K key, Object value);

  /**
   * @description: 返回有序集中，指定区间内的成员。
   * 其中成员的位置按分数值递增(从小到大)来排序。
   * 具有相同分数值的成员按字典顺序来排列。
   * 下标参数 start 和 end 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
   * 也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
   * 如果end超过该有序集合长度则返回整个有序集合。
   * 如果需要成员按值递减(从大到小)来排列，请使用zsetReverseRange方法
   * @create-by: yanchen @date:2020-09-02 20:52
   * @param key
   * @param start
   * @param end
   * @return: java.util.Set<V>
   */
  Set<V> zsetRange(K key, long start, long end);

  Set<V> zsetReverseRange(K key, long start, long end);

  /**
   * @description: 返回有序集中，指定区间内的成员和score
   * @create-by: yanchen @date:2020-09-02 21:01
   * @param key
   * @param start
   * @param end
   * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
   */
  Set<ZSetOperations.TypedTuple<V>> zsetRangeWithScores(K key, long start, long end);

  /**
   * @description: 通过指定score范围,返回有序集中，指定区间内的成员(从小到大排列)
   * @create-by: yanchen @date:2020-09-02 21:02
   * @param key
   * @param min
   * @param max
   * @return: java.util.Set<V>
   */
  Set<V> zsetRangeByScore(K key, double min, double max);

  /**
   * @description: 通过指定socre范围,返回有序集中，指定区间内从offset开始的count个成员（从小到大排列）
   * @create-by: yanchen @date:2020-09-02 21:06
   * @param key
   * @param min
   * @param max
   * @param offset 区间内偏移量
   * @param count  返回成员个数
   * @return: java.util.Set<V>
   */
  Set<V> zsetRangeByScore(K key, double min, double max, long offset, long count);

  /**
   * @description: 通过指定score范围，返回有序集合中，指定区间内的成员和score
   * @create-by: yanchen @date:2020-09-02 21:08
   * @param key
   * @param min
   * @param max
   * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
   */
  Set<ZSetOperations.TypedTuple<V>> zsetRangeByScoreWithScores(K key, double min, double max);

  /**
   * @description: 通过指定score范围，返回有序集合中，指定区间内从offset开始的count成员
   * @create-by: yanchen @date:2020-09-02 21:09
   * @param key
   * @param min
   * @param max
   * @param offset 区间内偏移量
   * @param count  返回成员个数
   * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
   */
  Set<ZSetOperations.TypedTuple<V>> zsetRangeByScoreWithScores(K key, double min, double max, long offset, long count);
  
  /**
   * @description: 返回有序集中，指定区间内的成员和socre
   * 其中成员的位置按分数值递减(从大到小)来排列。
   * 具有相同分数值的成员按字典逆序排列。
   * @create-by: yanchen @date:2020-09-02 21:13 
   * @param key
   * @param start
   * @param end
   * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
   */
  Set<ZSetOperations.TypedTuple<V>> zsetReverseRangeWithScores(K key, long start, long end);

  /**
   * @description: 通过指定score范围，返回有序集合中的成员（从大到小排列）
   * @create-by: yanchen @date:2020-09-02 21:18
   * @param key
   * @param min
   * @param max
   * @return: java.util.Set<V>
   */
  Set<V> zsetReverseRangeByScore(K key, double min, double max);

  /**
   * @description: 通过指定score范围，返回有序集合中的成员和socre（从大到小排列）
   * @create-by: yanchen @date:2020-09-02 21:21 
   * @param key
   * @param min
   * @param max
   * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
   */
  Set<ZSetOperations.TypedTuple<V>> zsetReverseRangeByScoreWithScores(K key, double min, double max);

  /**
   * @description: 通过指定socre范围,返回有序集中，指定区间内从offset开始的count个成员（从大到小排列）
   * 如果分数相同，则按照字典逆序排列
   * @create-by: yanchen @date:2020-09-02 21:22 
   * @param key
   * @param min
   * @param max
   * @param offset 区间偏移量
   * @param count  成员个数
   * @return: java.util.Set<V>
   */
  Set<V> zsetReverseRangeByScore(K key, double min, double max, long offset, long count);

   /**
    * @description: 通过指定socre范围,返回有序集中，指定区间内从offset开始的count个成员和分数（从大到小排列）
    * 如果分数相同，则按照字典逆序排列
    * @create-by: yanchen @date:2020-09-02 21:27
    * @param key
    * @param min
    * @param max
    * @param offset
    * @param count
    * @return: java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
    */
   Set<ZSetOperations.TypedTuple<V>> zsetReverseRangeByScoreWithScores(K key, double min, double max, long offset, long count);
  
   /**
    * @description: 通过指定score范围，返回有序集合中，指定区间内成员个数
    * @create-by: yanchen @date:2020-09-02 21:28 
    * @param key
    * @param min
    * @param max
    * @return: java.lang.Long
    */
   Long zsetCount(K key, double min, double max);

  /**
   * @description: 返回有序集合中所有成员个数
   * @create-by: yanchen @date:2020-09-02 21:30
   * @param key
   * @return: java.lang.Long
   */
   Long zsetSize(K key);

   /**
    * @description: 通过指定key和value返回相对应的分数
    * @create-by: yanchen @date:2020-09-02 21:31
    * @param key
    * @param o
    * @return: java.lang.Double
    */
   Double zsetScore(K key, Object o);

  /**
   * @description: 移除有序集合中，排名区间[start,end]内的成员
   * @create-by: yanchen @date:2020-09-02 21:35
   * @param key
   * @param start
   * @param end
   * @return: java.lang.Long
   */
   Long zsetRemoveRange(K key, long start, long end);

   /**
    * @description: 通过指定score范围，移除有序集合区间内的成员
    * @create-by: yanchen @date:2020-09-02 21:37
    * @param key
    * @param min
    * @param max
    * @return: java.lang.Long
    */
   Long zsetRemoveRangeByScore(K key, double min, double max);

  /**
   * @description: 将指定key对应的有序集合，与otherKey对应的有序集合，的并集，储存到destKey对应的集合中
   * 默认情况下，聚合类型(aggregate)：SUM，结果集中某个成员的分数值是所有给定集下该成员分数值之和
   * 乘法系数(weights):1
   * @create-by: yanchen @date:2020-09-02 21:56
   * @param key
   * @param otherKey
   * @param destKey
   * @return: java.lang.Long
   */
   Long zsetUnionAndStore(K key, K otherKey, K destKey);

   /**
    * @description: 将指定key对应的有序集合，与集合otherKeys对应的所有有序集合，的并集，储存到destKey对应的集合中
    * @create-by: yanchen @date:2020-09-02 22:00
    * @param key
    * @param otherKeys
    * @param destKey
    * @return: java.lang.Long
    */
   Long zsetUnionAndStore(K key, Collection<K> otherKeys, K destKey);

   /**
    * @description: 将指定key对应的有序集合，与集合otherKeys对应的所有有序集合，的并集，通过指定的聚合类型储存到destKey对应的集合中
    * @create-by: yanchen @date:2020-09-02 22:02 
    * @param key
    * @param otherKeys
    * @param destKey
    * @param aggregate 聚合函数类型:可选SUM，MIN，MAX
    * @return: java.lang.Long
    */
   Long zsetUnionAndStore(K key, Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate);
   
  /**
   * @description: 将指定key对应的有序集合，与集合otherKeys对应的所有有序集合，的并集，通过指定的聚合类型和乘法系数储存到destKey对应的集合中
   * @create-by: yanchen @date:2020-09-02 22:12 
   * @param key
   * @param otherKeys
   * @param destKey
   * @param aggregate 聚合函数类型:可选SUM，MIN，MAX
   * @param weights   乘法系数
   * @return: java.lang.Long
   */
   Long zsetUnionAndStore(K key, Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights);

    /**
     * @description: 将指定key对应的有序集合，与otherKey对应的有序集合，的交集，储存到destKey对应的集合中
     * 默认情况下，聚合类型(aggregate)：SUM，结果集中某个成员的分数值是所有给定集下该成员分数值之和
     * 乘法系数(weights):1
     * @create-by: yanchen @date:2020-09-02 21:56
     * @param key
     * @param otherKey
     * @param destKey
     * @return: java.lang.Long
     */
   Long zsetIntersectAndStore(K key, K otherKey, K destKey);

    /**
     * @description: 将指定key对应的有序集合，与集合otherKeys对应的所有有序集合，的交集，储存到destKey对应的集合中
     * @create-by: yanchen @date:2020-09-02 22:00
     * @param key
     * @param otherKeys
     * @param destKey
     * @return: java.lang.Long
     */
   Long zsetIntersectAndStore(K key, Collection<K> otherKeys, K destKey);

    /**
     * @description: 将指定key对应的有序集合，与集合otherKeys对应的所有有序集合，的交集，通过指定的聚合类型储存到destKey对应的集合中
     * @create-by: yanchen @date:2020-09-02 22:02
     * @param key
     * @param otherKeys
     * @param destKey
     * @param aggregate 聚合函数类型:可选SUM，MIN，MAX
     * @return: java.lang.Long
     */
   Long zsetIntersectAndStore(K key, Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate);

    /**
     * @description: 将指定key对应的有序集合，与集合otherKeys对应的所有有序集合，的交集，通过指定的聚合类型和乘法系数储存到destKey对应的集合中
     * @create-by: yanchen @date:2020-09-02 22:12
     * @param key
     * @param otherKeys
     * @param destKey
     * @param aggregate 聚合函数类型:可选SUM，MIN，MAX
     * @param weights   乘法系数
     * @return: java.lang.Long
     */
   Long zsetIntersectAndStore(K key, Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights);
  
   /**
    * @description: 返回有序集合中基于字典range内的所有成员
    * @create-by: yanchen @date:2020-09-02 22:41 
    * @param key
    * @param range 字典顺序范围
    * @return: java.util.Set<V>
    */
   Set<V> zsetRangeByLex(K key, RedisZSetCommands.Range range);
   
  /**
   * @description: 返回有序集合中基于字典range内的指定limit个数成员
   * @create-by: yanchen @date:2020-09-02 22:43 
   * @param key
   * @param range 字典顺序范围
   * @param limit 成员个数
   * @return: java.util.Set<V>
   */
  Set<V> zsetRangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);

  /**
   * @description: 扫描zset数据结构
   * @create-by: yanchen @date:2020-09-02 22:24 
   * @param key
   * @param options 扫描选项
   * @return: org.springframework.data.redis.core.Cursor<org.springframework.data.redis.core.ZSetOperations.TypedTuple < V>>
   */
  Cursor<ZSetOperations.TypedTuple<V>> zsetScan(K key, ScanOptions options);

 //============================================Geo=============================================\\
  
  /**
   * @description: 根据坐标添加单个地理信息
   * @create-by: yanchen @date:2020-09-03 21:24 
   * @param key
   * @param point
   * @param member
   * @return: java.lang.Long
   */
  Long geoAdd(K key, Point point, V member);

  Long geoAdd(K key, RedisGeoCommands.GeoLocation<V> location);

 /**
  * @description: 根据坐标添加多个地理信息
  * @create-by: yanchen @date:2020-09-03 21:44 
  * @param key
  * @param memberCoordinatePairs
  * @return: java.lang.Long
  */
  Long geoAdds(K key, Map<V, Point> memberCoordinatePairs);

  Long geoAdds(K key, Collection<RedisGeoCommands.GeoLocation<V>> locations);
  
 /**
  * @description: 获取两个地理位置的距离
  * @create-by: yanchen @date:2020-09-03 21:46 
  * @param key
  * @param member1
  * @param member2
  * @return: org.springframework.data.geo.Distance
  */
  Distance geoDistance(K key, V member1, V member2);
 
  /**
   * @description: 通过指定的度量衡，获取两个地理位置的距离
   * @create-by: yanchen @date:2020-09-03 21:48 
   * @param key
   * @param member1
   * @param member2
   * @param metric 度量衡，参见DistanceUnit
   * @return: org.springframework.data.geo.Distance
   */
  Distance geoDistance(K key, V member1, V member2, Metric metric);

  /**
   * @description: 获取多个成员的地理位置信息，以Geohash表示
   * @create-by: yanchen @date:2020-09-03 21:51 
   * @param key
   * @param members
   * @return: java.util.List<java.lang.String>
   */
  List<String> geoHash(K key, V... members);

 /**
  * @description: 获取多个成员的地理位置信息，以Point表示
  * @create-by: yanchen @date:2020-09-03 21:54 
  * @param key
  * @param members
  * @return: java.util.List<org.springframework.data.geo.Point>
  */
  List<Point> geoPosition(K key, V... members);

 /**
  * @description: 根据指定的circle范围，获取此范围内的所有地理位置集合
  * @create-by: yanchen @date:2020-09-03 21:55 
  * @param key
  * @param within 圆范围
  * @return: org.springframework.data.geo.GeoResults<org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation < V>>
  */
  GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadius(K key, Circle within);

  GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadius(K key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args);

 /**
  * @description: 根据指定成员member地理坐标位置，获取在半径radius范围内的所有地理位置集合
  * @create-by: yanchen @date:2020-09-03 22:01 
  * @param key
  * @param member
  * @param radius 半径
  * @return: org.springframework.data.geo.GeoResults<org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation < V>>
  */
  GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadiusByMember(K key, V member, double radius);

 /**
  * @description: 根据指定成员member地理坐标位置，获取在距离distance范围内的所有地理位置集合
  * @create-by: yanchen @date:2020-09-03 22:04
  * @param key
  * @param member
  * @param distance 距离
  * @return: org.springframework.data.geo.GeoResults<org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation < V>>
  */
  GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadiusByMember(K key, V member, Distance distance);

  GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadiusByMember(K key, V member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args);

 /**
  * @description: 移除指定多个成员members的地理坐标位置
  * @create-by: yanchen @date:2020-09-03 22:07 
  * @param key
  * @param members
  * @return: java.lang.Long
  */
  Long geoRemove(K key, V... members);

}


