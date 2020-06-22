package com.local.common.utils;

import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author yc
 * @description TODO  时间日期工具类
 * @date 2020-05-21 15:09
 */
public class DateTimeHelper {

    private DateTimeHelper(){

        throw new RuntimeException("DateTimeHelper not support instantiated");
    }

    enum DateTimePattern{

        /**
          * @Description  标准日期时间格式
          */

        STANDARD_DATETIME_FORMAT("yyyy-MM-dd HH:mm:ss");

        private String pattern;

        DateTimePattern(String pattern){

            this.pattern=pattern;
        }
    }

    /**
      * @Description 获取系统当前时间戳(秒)级
      * @return long  系统时间戳
      * @Author yc
      */

    public static final long getSystemTimeStampToSecnnds(){

        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
      * @Description 获取系统当前时间戳(毫秒）级
      * @return long 系统时间戳
      * @Author yc
      */

    public static final long getSystemTimeStampToMillis(){

        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }


    /**
      * @Description 日期时间字符串转时间戳(毫秒级)
      * @Param [dateTime] 日期时间字符串
      * @return long
      * @Author yc
      */

    public static final long stringConvertTimeStampToMills(final String dateTime){

        return stringConvertTimeStampToMills(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern,dateTime);
    }

    public static final long stringConvertTimeStampToMills(final String pattern,final String dateTime){

      return   LocalDateTime.from(LocalDateTime.parse(dateTime,DateTimeFormatter.ofPattern(pattern))).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
    }

    /**
      * @Description 转换（毫秒级）时间戳到（标准时间格式）字符串
      * @Param [timeStamp] (毫秒级)时间戳
      * @return java.lang.String
      * @Author yc
      */

    public static  final String millsTimeStampConvertStringDateTime(final long timeStamp){

        return millsTimeStampConvertStringDateTime(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern,timeStamp);
    }

    /**
      * @Description  转换（毫秒级）时间戳到（指定时间格式）字符串
      * @Param [pattern 日期时间指定格式, timeStamp 毫秒级时间戳]
      * @return java.lang.String
      * @Author yc
      */

    public static final String millsTimeStampConvertStringDateTime(final String pattern,final long timeStamp){

        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),ZoneOffset.of("+8")));
    }


    /**
      * @Description 转换(秒级)时间戳到(标准时间格式)字符串
      * @Param [timeStamp （秒级）时间戳]
      * @return java.lang.String
      * @Author yc
      */

    public static final String secondsTimeStampConvertStringDateTime(final long timeStamp){

        return secondsTimeStampConvertStringDateTime(DateTimePattern.STANDARD_DATETIME_FORMAT.pattern,timeStamp);
    }

    /**
      * @Description 转换（秒级）时间戳到（指定日期时间格式）字符串
      * @Param [pattern 日期时间指定格式, timeStamp 秒级时间戳]
      * @return java.lang.String
      * @Author yc
      */

    public static  final String secondsTimeStampConvertStringDateTime(final String pattern,final long timeStamp){


        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(Instant.ofEpochSecond(timeStamp),ZoneOffset.of("+8")));
    }

/**
  * @Description Date转String
  * @Param [date]
  * @return java.lang.String
  * @Author yc
  * @Date 2020-06-20 18:41
  * @version 1.0
  */

    public static  final String dateConvertString(final Date date){

        return dateConvertString(date,DateTimePattern.STANDARD_DATETIME_FORMAT.pattern);
    }

    public static  final String dateConvertString(final Date date,final  String pattern){

        return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.ofInstant(date.toInstant(),ZoneOffset.of("+8")));
    }
}
