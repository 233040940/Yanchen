package com.local.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author yc
 * @description   时间日期工具类
 * @date 2020-05-21 15:09
 */
public class DateTimeHelper {

    private DateTimeHelper() {
        throw new RuntimeException("DateTimeHelper not support instantiated");
    }

    enum DateTimePattern {
        /**
         * @Description 标准日期时间格式
         */
        STANDARD_DATETIME_FORMAT("yyyy-MM-dd HH:mm:ss");

        private String pattern;

        DateTimePattern(String pattern) {
            this.pattern = pattern;
        }
    }

    /**
     * @return long  系统时间戳
     * @Description 获取系统当前时间戳(秒)级
     * @Author yc
     */
    public static final long getSystemTimeStampToSeconds() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * @return long 系统时间戳
     * @Description 获取系统当前时间戳(毫秒 ） 级
     * @Author yc
     */
    public static final long getSystemTimeStampToMillis() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * @return long
     * @Description 日期时间字符串转时间戳(毫秒级)
     * @Param [dateTime] 日期时间字符串
     * @Author yc
     */
    public static final long stringConvertToTimeStampToMills(final String dateTime) {
        return stringConvertToTimeStampToMills(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern, dateTime);
    }

    public static final long stringConvertToTimeStampToMills(final String pattern, final String dateTime) {
        return LocalDateTime.from(LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern))).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
    }

    /**
     * @return java.lang.String
     * @Description 转换（毫秒级）时间戳到（标准时间格式）字符串
     * @Param [timeStamp] (毫秒级)时间戳
     * @Author yc
     */
    public static final String millsTimeStampConvertToStringDateTime(final long timeStamp) {
        return millsTimeStampConvertToStringDateTime(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern, timeStamp);
    }

    /**
     * @return java.lang.String
     * @Description 转换（毫秒级）时间戳到（指定时间格式）字符串
     * @Param [pattern 日期时间指定格式, timeStamp 毫秒级时间戳]
     * @Author yc
     */
    public static final String millsTimeStampConvertToStringDateTime(final String pattern, final long timeStamp) {
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.of("+8")));
    }

    /**
     * @return java.lang.String
     * @Description 转换(秒级)时间戳到(标准时间格式)字符串
     * @Param [timeStamp （秒级）时间戳]
     * @Author yc
     */
    public static final String secondsTimeStampConvertToStringDateTime(final long timeStamp) {
        return secondsTimeStampConvertToStringDateTime(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern, timeStamp);
    }

    /**
     * @return java.lang.String
     * @Description 转换（秒级）时间戳到（指定日期时间格式）字符串
     * @Param [pattern 日期时间指定格式, timeStamp 秒级时间戳]
     * @Author yc
     */
    public static final String secondsTimeStampConvertToStringDateTime(final String pattern, final long timeStamp) {
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp), ZoneOffset.of("+8")));
    }

    /**
     * @return java.lang.String
     * @Description Date转String
     * @Param [date]
     * @Author yc
     * @Date 2020-06-20 18:41
     * @version 1.0
     */
    public static final String dateConvertToString(final Date date) {
        return dateConvertToString(date, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final String dateConvertToString(final Date date, final String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.of("+8")));
    }

    public static final Date stringConvertToDate(final String date) {
        return stringConvertToDate(date, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final Date stringConvertToDate(final String date, final String pattern) {
        return Date.from(stringConvertToLocalDateTime(date, pattern).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static final LocalDateTime stringConvertToLocalDateTime(final String date) {
        return stringConvertToLocalDateTime(date, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final LocalDateTime stringConvertToLocalDateTime(final String date, final String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
