package com.local.common.utils;

import com.google.common.collect.Lists;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yc
 * @project yanchen
 * @description （redis操作工具类，key、hashKey 都为字符串类型）
 * @date 2020-05-28 20:37
 */

public class RedisTemplateHelper<String, V> implements RedisOperationProvider<String, V> {

    private final RedisTemplate redisTemplate;

    private static final List EMPTY_LIST = Collections.EMPTY_LIST;

    public RedisTemplateHelper(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean setKeyExpire(String key, long timeOut) {
        return setKeyExpire(key, timeOut, TimeUnit.SECONDS);
    }

    @Override
    public boolean setKeyExpire(String key, long timeOut, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeOut, timeUnit);
    }

    @Override
    public Boolean setKeyExpireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    @Override
    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean hasKeys(Collection<String> keys) {
        return countExistingKeys(keys) == keys.size() ? true : false;
    }

    @Override
    public long countExistingKeys(Collection<String> keys) {
        return redisTemplate.countExistingKeys(keys);
    }

    @Override
    public boolean delKey(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public boolean delKeys(String... keys) {
        return delKeys(CollectionUtils.arrayToList(keys));
    }

    @Override
    public boolean delKeys(Collection<String> keys) {
        return redisTemplate.delete(keys) == keys.size() ? true : false;
    }

    @Override
    public boolean renameKey(String oldKey, String newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean renameKeyIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    @Override
    public boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    @Override
    public long countDelKeysSuccess(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean set(String key, V value, long timeOut) {
        return set(key, value, timeOut, TimeUnit.SECONDS);

    }

    @Override
    public boolean set(String key, V value, long timeOut, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setBit(String key, long offset, boolean value) {
        final long minOffset = 0;
        if (offset < minOffset) {
            throw new IllegalArgumentException("offset不能小于0");
        }
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    @Override
    public boolean getBit(String key, long offset) {
        final long minOffset = 0;
        if (offset < minOffset) {
            throw new IllegalArgumentException("offset不能小于0");
        }
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    @Override
    public List<Long> bitFields(String key, BitFieldSubCommands subCommands) {
        return redisTemplate.opsForValue().bitField(key, subCommands);
    }

    @Override
    public boolean setIfAbsent(String key, V value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public boolean setIfAbsent(String key, V value, long timeOut) {
        return setIfAbsent(key, value, timeOut, TimeUnit.SECONDS);
    }

    @Override
    public boolean setIfAbsent(String key, V value, long timeOut, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit);
    }

    @Override
    public boolean replaceValue(String key, V value, long offset) {
        final long minOffset = 0;
        if (offset < minOffset) {
            throw new IllegalArgumentException("offset不能小于0");
        }
        try {
            redisTemplate.opsForValue().set(key, value, offset);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean batchSet(Map<String, ?> entry) {
        try {
            redisTemplate.opsForValue().multiSet(entry);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean batchSetIfAbsent(Map<String, ?> entry) {
        return redisTemplate.opsForValue().multiSetIfAbsent(entry);
    }

    @Override
    public long increment(String key) {
        return incrementByLong(key, 1L);
    }

    @Override
    public long incrementByLong(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public double incrementByDouble(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public long decrement(String key) {
        return decrement(key, 1);
    }

    @Override
    public long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    @Override
    public List<V> batchGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public V get(String key) {
        return (V) redisTemplate.opsForValue().get(key);
    }

    @Override
    public V getAndSet(String key, V value) {
        return (V) redisTemplate.opsForValue().getAndSet(key, value);
    }

    @Override
    public void append(String key, String value) {
        redisTemplate.opsForValue().append(key, (java.lang.String) value);
    }

    @Override
    public boolean hashSet(String key, String hashKey, V value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hashBatchSet(String key, Map<String, ?> entries) {
        try {
            redisTemplate.opsForHash().putAll(key, entries);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hashSetIfAbsent(String key, String hashKey, V value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    @Override
    public V hashGet(String key, String hashKey) {
        return (V) redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<String, V> hashBatchGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);

    }

    @Override
    public List<V> hashBatchGet(String key, Collection<String> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    @Override
    public Set<String> getHashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    @Override
    public long hashIncrementLong(String key, String hashKey) {
        return hashIncrementLong(key, hashKey, 1);
    }

    @Override
    public long hashIncrementLong(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public double hashIncrementDouble(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public boolean hasHashKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public long hashDel(String key, Collection<String> hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public boolean listSet(String key, long index, V value) {
        final long minIndex = -1;    //表示该list最末端索引
        if (index < minIndex) {
           throw new IllegalArgumentException("index不能小于-1");
        }
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Cursor<Map.Entry<String, V>> hashScan(String key, ScanOptions scanOptions) {
        return redisTemplate.opsForHash().scan(key, scanOptions);
    }

    @Override
    public boolean listLeftPush(String key, V value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean listLeftPushIfPresent(String key, V value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value) == 1 ? true : false;
    }

    @Override
    public long listLeftPushAll(String key, Collection<V> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    @Override
    public boolean listRightPush(String key, V value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean listRightPushIfPresent(String key, V value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value) == 1 ? true : false;
    }

    @Override
    public long listRightPushAll(String key, Collection<V> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public V listLeftPop(String key) {
        return (V) redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public V listLeftBlockPop(String key, long timeOut, TimeUnit timeUnit) {
        return (V) redisTemplate.opsForList().leftPop(key, timeOut, timeUnit);
    }

    @Override
    public V listRightPop(String key) {
        return (V) redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public V listRightBlockPop(String key, long timeOut, TimeUnit timeUnit) {
        return (V) redisTemplate.opsForList().rightPop(key, timeOut, timeUnit);
    }

    @Override
    public V listRightPopAndLeftPush(String rightKey, String leftKey) {
        return (V) redisTemplate.opsForList().rightPopAndLeftPush(rightKey, leftKey);
    }

    @Override
    public V listRightPopAndLeftPush(String rightKey, String leftKey, long blockTimeOut, TimeUnit timeUnit) {
        return (V) redisTemplate.opsForList().rightPopAndLeftPush(rightKey, leftKey, blockTimeOut, timeUnit);
    }

    @Override
    public boolean subList(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public V listGet(String key, long index) {
        return (V) redisTemplate.opsForList().index(key, index);
    }

    @Override
    public List<V> listRange(String key, long start, long end) {
        List<V> range = redisTemplate.opsForList().range(key, start, end);
        if (CollectionUtils.isEmpty(range)) {
            return EMPTY_LIST;
        }
        return range;
    }

    @Override
    public long listRemove(String key, long count, V value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public long setAdd(String key, V... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public long setRemove(String key, V... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public V setPop(String key) {
        return (V) redisTemplate.opsForSet().pop(key);
    }

    @Override
    public List<V> setPops(String key, long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }

    @Override
    public boolean setMove(String key, V value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    @Override
    public long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public boolean setExist(String key, V value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public Set<V> setIntersect(String key, String otherKey) {
        return setIntersect(key, Lists.newArrayList(otherKey));
    }

    @Override
    public Set<V> setIntersect(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    @Override
    public Long setIntersectAndStore(String key, String otherKey, String destKey) {
        return setIntersectAndStore(key, Lists.newArrayList(otherKey), destKey);
    }

    @Override
    public Long setIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Set<V> setUnion(String key, String otherKey) {
        return setUnion(key, Lists.newArrayList(otherKey));
    }

    @Override
    public Set<V> setUnion(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    @Override
    public Long setUnionAndStore(String key, String otherKey, String destKey) {
        return setUnionAndStore(key, Lists.newArrayList(otherKey), destKey);
    }

    @Override
    public Long setUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Set<V> setDifference(String key, String otherKey) {
        return setDifference(key, Lists.newArrayList(otherKey));
    }

    @Override
    public Set<V> setDifference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    @Override
    public Long setDifferenceAndStore(String key, String otherKey, String destKey) {
        return setDifferenceAndStore(key, Lists.newArrayList(otherKey), destKey);
    }

    @Override
    public Long setDifferenceAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    @Override
    public Set<V> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public V setRandomMember(String key) {
        return (V) redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public List<V> setRandomMembers(String key, long count) {
        if (count < 0) {
            throw new IllegalArgumentException("count参数不能小于0");
        }
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    @Override
    public Set<V> setDistinctRandomMembers(String key, long count) {
        if (count < 0) {
            throw new IllegalArgumentException("count参数不能小于0");
        }
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    @Override
    public Boolean zsetAdd(String key, V value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Long zsetBatchAdd(String key, Set<ZSetOperations.TypedTuple<V>> typedTuples) {
        return redisTemplate.opsForZSet().add(key, typedTuples);
    }

    @Override
    public Long zsetRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    @Override
    public Double zsetIncrementScore(String key, V value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    @Override
    public Long zsetRank(String key, Object o) {
        return redisTemplate.opsForZSet().rank(key, o);
    }

    @Override
    public Long zsetReverseRank(String key, Object o) {
        return redisTemplate.opsForZSet().reverseRank(key, o);
    }

    @Override
    public Set<V> zsetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> zsetRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    @Override
    public Set<V> zsetRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public Set<V> zsetRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> zsetRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> zsetRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<V> zsetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> zsetReverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    @Override
    public Set<V> zsetReverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> zsetReverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<V> zsetReverseRangeByScore(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<V>> zsetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Double zsetScore(String key, Object o) {
        return redisTemplate.opsForZSet().score(key, o);
    }

    @Override
    public Long zsetRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Long zsetRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Long zsetUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey, aggregate);
    }

    @Override
    public Long zsetUnionAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public Long zsetIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey, aggregate);
    }

    @Override
    public Long zsetIntersectAndStore(String key, Collection<String> otherKeys, String destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey, aggregate, weights);
    }

    @Override
    public Set<V> zsetRangeByLex(String key, RedisZSetCommands.Range range) {
        return redisTemplate.opsForZSet().rangeByLex(key, range);
    }

    @Override
    public Set<V> zsetRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return redisTemplate.opsForZSet().rangeByLex(key, range, limit);
    }

    @Override
    public Long zsetCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public Long zsetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Cursor<ZSetOperations.TypedTuple<V>> zsetScan(String key, ScanOptions options) {
        return redisTemplate.opsForZSet().scan(key, options);
    }

    @Override
    public Cursor<V> setScan(String key, ScanOptions options) {
        return redisTemplate.opsForSet().scan(key, options);
    }

    @Override
    public void publishMessage(java.lang.String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }

    @Override
    public Long geoAdd(String key, Point point, V member) {
        return redisTemplate.opsForGeo().add(key, point, member);
    }

    @Override
    public Long geoAdd(String key, RedisGeoCommands.GeoLocation<V> location) {
        return redisTemplate.opsForGeo().add(key, location);
    }

    @Override
    public Long geoAdds(String key, Map<V, Point> memberCoordinatePairs) {
        return redisTemplate.opsForGeo().add(key, memberCoordinatePairs);
    }

    @Override
    public Long geoAdds(String key, Collection<RedisGeoCommands.GeoLocation<V>> geoLocations) {
        return redisTemplate.opsForGeo().add(key, geoLocations);
    }

    @Override
    public Distance geoDistance(String key, V member1, V member2) {
        return redisTemplate.opsForGeo().distance(key, member1, member2);
    }

    @Override
    public Distance geoDistance(String key, V member1, V member2, Metric metric) {
        return redisTemplate.opsForGeo().distance(key, member1, member2, metric);
    }

    @Override
    public List<java.lang.String> geoHash(String key, V... members) {
        return redisTemplate.opsForGeo().hash(key, members);
    }

    @Override
    public List<Point> geoPosition(String key, V... members) {
        return redisTemplate.opsForGeo().position(key, members);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadius(String key, Circle within) {
        return redisTemplate.opsForGeo().radius(key, within);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadius(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return redisTemplate.opsForGeo().radius(key, within, args);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadiusByMember(String key, V member, double radius) {
        return redisTemplate.opsForGeo().radius(key, member, radius);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadiusByMember(String key, V member, Distance distance) {
        return redisTemplate.opsForGeo().radius(key, member, distance);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<V>> geoRadiusByMember(String key, V member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        return redisTemplate.opsForGeo().radius(key, member, distance, args);
    }

    @Override
    public Long geoRemove(String key, V... members) {
        return redisTemplate.opsForGeo().remove(key, members);
    }
}

