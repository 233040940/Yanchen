package com.local.common.utils;

import com.google.common.primitives.Ints;

import java.util.Collection;
import java.util.List;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO    int相关工具类
 * @date 2020-05-29 23:12
 */
public class IntHelper {


    private IntHelper(){

        throw new RuntimeException("IntHelper not support instantiated");
    }
    
 /*
  * create by yanchen datetime:2020-05-30 00:48
  * @description: 将给定字符串转化为int
  * @param var
  * @return: int
  */
    public static  int parseString(String var){
        return Ints.tryParse(var);
    }

 /*
  * create by yanchen datetime:2020-05-30 00:54
  * @description: 将给定的int 数组转为list
  * @param vars int数组
  * @return: java.util.List<java.lang.Integer>
  */
    public static List<Integer> parseList(int ...vars){
        return Ints.asList(vars);
    }

   
   /*
    * create by yanchen datetime:2020-05-30 00:59
    * @description: 判断var是否存在与arr数组中
    * @param arr
    * @param var
    * @return: boolean
    */ 
    public static boolean contains(int []arr,int var){
        return Ints.contains(arr, var);
    }
    
    /*
     * create by yanchen datetime:2020-05-30 01:00
     * @description:返回target在arr中的索引位置,不存在返回-1
     * @param arr
     * @param target
     * @return: int
     */
    public static  int indexOf(int []arr,int target){
        return Ints.indexOf(arr,target);
    }

    /*
     * create by yanchen datetime:2020-05-30 01:12
     * @description:将集合转为数组
     *
     * @param collection
     * @return: int[]
     */
    public static  int[] toArray(Collection<? extends Number> collection){
        return Ints.toArray(collection);
    }

    /*
     * create by yanchen datetime:2020-05-30 01:13
     * @description: 反转数组

     * @param arr
     * @return: int[]
     */
    public static  int[] reverse(int[] arr){

      return   reverse(arr,0,arr.length);

    }

    public static int[] reverse(int[]arr,int start,int end){

        Ints.reverse(arr,start,end);
        return  arr;
    }


    /*
     * create by yanchen datetime:2020-05-30 01:16
     * @description:获取数组中最大值

     * @param arr
     * @return: int
     */
    public static  int max(int []arr){

        return Ints.max(arr);
    }

    /*
     * create by yanchen datetime:2020-05-30 01:16
     * @description:获取数组中最小值

     * @param arr
     * @return: int
     */
    public static  int min(int [] arr){

        return Ints.min(arr);
    }
}
