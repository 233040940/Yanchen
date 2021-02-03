package com.local.common.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.CollectionUtils;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yc
 * @project yanchen
 * @description 随机数工具类
 * params 【min(最小值),max(最大值),count(生成数量),exclude(排除的随机数集合)】
 * 命名规则:one表示只生成一个随机数,any表示生成多个
 * 备注:生成的随机数范围区间为（左闭右开）比如：生成0-10的某个随机数，那么min=0,max=9
 * @date 2020-06-09 03:33
 */
public class RandomHelper {

    private RandomHelper() {
        throw new RuntimeException("RandomHelper is tool class,Not support instantiated");
    }

    public static int oneInt() {
        return oneInt(0, Integer.MAX_VALUE);
    }
    public static int oneInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
    public static int[] anyInts(int count) {
        return anyInts(0, Integer.MAX_VALUE, count);
    }
    public static int[] anyInts(int min, int max, int count) {
        return anyInts(min, max, count, null);
    }
    public static int[] anyInts(int min, int max, int count, Set<Integer> excludes) {
        if (CollectionUtils.isEmpty(excludes)) {
            return ThreadLocalRandom.current().ints(min, max).limit(count).toArray();
        }
        return ThreadLocalRandom.current().ints(min, max).filter((e) -> !excludes.contains(e)).limit(count).toArray();
    }
    public static long oneLong() {
        return oneLong(0L, Long.MAX_VALUE);
    }
    public static long oneLong(int min){return oneLong(min,Long.MAX_VALUE);}
    public static long oneLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }
    public static long[] anyLongs(int count) {
        return anyLongs(0L, Long.MAX_VALUE, count);
    }
    public static long[] anyLongs(long min, long max, int count) {
        return anyLongs(min, max, count, null);
    }

    public static long[] anyLongs(long min, long max, int count, Set<Long> excludes) {
        if (CollectionUtils.isEmpty(excludes)) {
            return ThreadLocalRandom.current().longs(min, max).limit(count).toArray();
        }
        return ThreadLocalRandom.current().longs(min, max).filter((e) -> !excludes.contains(e)).limit(count).toArray();
    }
    public static double oneDouble() {
        return oneDouble(0, Double.MAX_VALUE);
    }
    public static double oneDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static double[] anyDoubles(int count) {
        return anyDoubles(0, Double.MAX_VALUE, count);
    }

    public static double[] anyDoubles(double min, double max, int count) {
        return anyDoubles(min, max, count, null);
    }

    public static double[] anyDoubles(double min, double max, int count, Set<Double> excludes) {
        if (CollectionUtils.isEmpty(excludes)) {
            return ThreadLocalRandom.current().doubles(min, max).limit(count).toArray();
        }
        return ThreadLocalRandom.current().doubles(min, max).filter((e) -> !excludes.contains(e)).limit(count).toArray();
    }

    /**
      * @Description 生成指定长度，是否包含字母，数字的随机字符组合
      * @Param [length 随机字符长度, letter 允许出现字母, number允许出现数字]
      * @return java.lang.String
      * @Author yc
      */
    public static String oneString(int length,boolean letter,boolean number) {
        return RandomStringUtils.random(length,letter,number);
    }

    /**
      * @Description 生成指定长度,指定字符集中的随机字符组合
      * @Param [length 随机字符长度, randomChars 指定随机字符]
      * @return java.lang.String
      * @Author yc
      * @Date 2020-06-09 06:59
      */
    public static String oneString(int length,char...randomChars){
        return RandomStringUtils.random(length, randomChars);
    }

}

