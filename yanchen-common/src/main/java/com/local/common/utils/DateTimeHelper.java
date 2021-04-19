package com.local.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author yc
 * @description 时间日期工具类
 * @date 2020-05-21 15:09
 */
public class DateTimeHelper {

    private DateTimeHelper() {
        throw new RuntimeException("DateTimeHelper not support instantiated");
    }

   public enum DateTimePattern {
        /**
         * @Description 标准日期时间格式
         */
        STANDARD_DATETIME_FORMAT("yyyy-MM-dd HH:mm:ss");

        private String pattern;

        DateTimePattern(String pattern) {
            this.pattern = pattern;
        }
    }

   public enum DatePattern {
        /**
         * @Description 标准日期格式
         */
        STANDARD_DATE_FORMAT("yyyy-MM-dd");

        private String pattern;

        DatePattern(String pattern) {
            this.pattern = pattern;
        }
    }

    enum DateTimeUnit {
        MILLISECONDS("毫秒"),
        SECONDS("秒"),
        MINUTES(" 分钟"),
        HOURS("小时"),
        DAYS("天"),
        WEEKS("星期"),
        MONTHS("月"),
        YEARS("年");

        private String desc;

        DateTimeUnit(String desc) {
            this.desc = desc;
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
     * @Description 获取系统当前时间戳(毫秒)级
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
    public static final long stringDateTimeConvertToTimestampMills(final String dateTime) {
        return stringDateTimeConvertToTimestampMills(dateTime, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final long stringDateTimeConvertToTimestampMills(final String dateTime, final String pattern) {
        return localDateTimeConvertTimestampMillis(stringConvertToLocalDateTime(dateTime, pattern));
    }

    /**
     * 日期字符串转时间戳(毫秒级)
     * @param date
     * @return
     */
    public static final long stringDateConvertToTimestampMills(final String date) {
        return stringDateConvertToTimestampMills(date, DatePattern.STANDARD_DATE_FORMAT.pattern);
    }

    public static final long stringDateConvertToTimestampMills(final String date, final String pattern) {
        return localDateConvertTimestampMillis(stringConvertToLocalDate(date, pattern));
    }

    /**
     * @return java.lang.String
     * @Description 转换（毫秒级）时间戳到（标准时间格式）字符串
     * @Param [timeStamp] (毫秒级)时间戳
     * @Author yc
     */
    public static final String millisTimeStampConvertToStringDateTime(final long timeStamp) {
        return millisTimeStampConvertToStringDateTime(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern, timeStamp);
    }

    /**
     * @return java.lang.String
     * @Description 转换（毫秒级）时间戳到（指定时间格式）字符串
     * @Param [pattern 日期时间指定格式, timeStamp 毫秒级时间戳]
     * @Author yc
     */
    public static final String millisTimeStampConvertToStringDateTime(final String pattern, final long timeStamp) {
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

    public static final Date stringConvertToDate(final String dateTime) {
        return stringConvertToDate(dateTime, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final Date stringConvertToDate(final String dateTime, final String pattern) {
        return Date.from(stringConvertToLocalDateTime(dateTime, pattern).atZone(ZoneId.systemDefault()).toInstant());
    }


    public static final LocalDateTime stringConvertToLocalDateTime(final String dateTime) {
        return stringConvertToLocalDateTime(dateTime, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final LocalDateTime stringConvertToLocalDateTime(final String dateTime, final String pattern) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    public static final LocalDate stringConvertToLocalDate(final String date) {
        return stringConvertToLocalDate(date, DatePattern.STANDARD_DATE_FORMAT.pattern);
    }

    public static final LocalDate stringConvertToLocalDate(final String date, final String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static final LocalDateTime millisTimestampConvertToLocalDateTime(final long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    public static final LocalDateTime secondsTimestampConvertToLocalDateTime(final long timestamp) {
        return Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    public static final String localDateTimeConvertToString(final LocalDateTime dateTime) {
        return localDateTimeConvertToString(dateTime, DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static final String localDateTimeConvertToString(final LocalDateTime dateTime, final String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    public static final long localDateTimeConvertTimestampMillis(final LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static final long localDateTimeConvertTimestampSeconds(final LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond();
    }

    public static final long localDateConvertTimestampMillis(final LocalDate localDate) {
        return localDateTimeConvertTimestampMillis(localDate.atStartOfDay(ZoneOffset.of("+8")).toLocalDateTime());
    }

    public static final long localDateConvertTimestampSeconds(final LocalDate localDate) {
        return localDateTimeConvertTimestampSeconds(localDate.atStartOfDay(ZoneOffset.of("+8")).toLocalDateTime());
    }

    /**
     * 时间增加
     *
     * @param timestamp 时间戳:单位(毫秒)
     * @param timeUnit  增加单位
     * @param count     增加数量
     * @return
     */
    public static final long millisTimestampPlus(final long timestamp, final DateTimeUnit timeUnit, final int count) {
        long result = 0L;
        switch (timeUnit) {
            case MILLISECONDS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plus(count, ChronoUnit.MILLIS));
                break;
            case SECONDS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusSeconds(count));
                break;
            case MINUTES:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusMinutes(count));
                break;
            case HOURS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusHours(count));
                break;
            case DAYS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusDays(count));
                break;
            case WEEKS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusWeeks(count));
                break;
            case MONTHS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusMonths(count));
                break;
            case YEARS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).plusYears(count));
                break;
        }
        return result;
    }


    /**
     * 时间减
     *
     * @param timestamp 时间戳:单位(毫秒)
     * @param timeUnit  减去单位
     * @param count     减去数量
     * @return
     */
    public static final long millisTimestampMinus(final long timestamp, final DateTimeUnit timeUnit, final int count) {
        long result = 0L;
        switch (timeUnit) {
            case MILLISECONDS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minus(count, ChronoUnit.MILLIS));
                break;
            case SECONDS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusSeconds(count));
                break;
            case MINUTES:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusMinutes(count));
                break;
            case HOURS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusHours(count));
                break;
            case DAYS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusDays(count));
                break;
            case WEEKS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusWeeks(count));
                break;
            case MONTHS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusMonths(count));
                break;
            case YEARS:
                result = localDateTimeConvertTimestampMillis(millisTimestampConvertToLocalDateTime(timestamp).minusYears(count));
                break;
        }
        return result;
    }


    /**
     * 时间增加
     *
     * @param timestamp 时间戳:单位(秒)
     * @param timeUnit  增加单位
     * @param count     增加数量
     * @return
     */
    public static final long secondsTimestampPlus(final long timestamp, final DateTimeUnit timeUnit, final int count) {
        long result = 0L;
        switch (timeUnit) {
            case MILLISECONDS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plus(count, ChronoUnit.MILLIS));
                break;
            case SECONDS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusSeconds(count));
                break;
            case MINUTES:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusMinutes(count));
                break;
            case HOURS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusHours(count));
                break;
            case DAYS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusDays(count));
                break;
            case WEEKS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusWeeks(count));
                break;
            case MONTHS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusMonths(count));
                break;
            case YEARS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).plusYears(count));
                break;
        }
        return result;
    }

    /**
     * 时间减
     *
     * @param timestamp 时间戳:单位(秒)
     * @param timeUnit  减去单位
     * @param count     减去数量
     * @return
     */
    public static final long secondsTimestampMinus(final long timestamp, final DateTimeUnit timeUnit, final int count) {
        long result = 0L;
        switch (timeUnit) {
            case MILLISECONDS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minus(count, ChronoUnit.MILLIS));
                break;
            case SECONDS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusSeconds(count));
                break;
            case MINUTES:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusMinutes(count));
                break;
            case HOURS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusHours(count));
                break;
            case DAYS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusDays(count));
                break;
            case WEEKS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusWeeks(count));
                break;
            case MONTHS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusMonths(count));
                break;
            case YEARS:
                result = localDateTimeConvertTimestampSeconds(secondsTimestampConvertToLocalDateTime(timestamp).minusYears(count));
                break;
        }
        return result;
    }

    /**
     * 计算两个时间相差多少unit
     *
     * @param starTimestamp 第一个时间戳：单位毫秒级
     * @param endTimestamp  第二个时间戳：单位毫秒级
     * @param unit          相差多少单位
     * @return
     */
    public static final long calculateDateDistance(final long starTimestamp, final long endTimestamp, final DateTimeUnit unit) {
        long result = 0L;
        switch (unit) {
            case MILLISECONDS:
                result = ChronoUnit.MILLIS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case SECONDS:
                result = ChronoUnit.SECONDS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case MINUTES:
                result = ChronoUnit.MINUTES.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case HOURS:
                result = ChronoUnit.HOURS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case DAYS:
                result = ChronoUnit.DAYS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case WEEKS:
                result = ChronoUnit.WEEKS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case MONTHS:
                result = ChronoUnit.MONTHS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
            case YEARS:
                result = ChronoUnit.YEARS.between(millisTimestampConvertToLocalDateTime(starTimestamp), millisTimestampConvertToLocalDateTime(endTimestamp));
                break;
        }
        return result;
    }

}
