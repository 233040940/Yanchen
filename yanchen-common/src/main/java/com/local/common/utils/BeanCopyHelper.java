package com.local.common.utils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yc
 * @description 复制bean工具类
 * @date 2020-06-10 11:13
 */
public class BeanCopyHelper {

    private BeanCopyHelper(){

        throw new RuntimeException("BeanCopyHelper is tool class,Not support instanced");
    }

    /**
      * @Description 复制来源对象(属性)到目标对象
      * @Param [source 来源对象, target 目标对象]
      * @return T
      * @Author yc
      */

    public static <S,T> T copy(S source, T target) {

        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <S,T> T copy(S source, Class<T> tClass) {

        try {
            T t = tClass.newInstance();
            return copy(source, t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <S,T> List<T> copyAny(Collection<S> sources, Class<T>tClass){

        if(CollectionUtils.isEmpty(sources)){
            return Collections.EMPTY_LIST;
        }

     return   sources.stream().filter((e)->e!=null).map((e)-> copy(e,tClass)).collect(Collectors.toList());
    }


    public static <S,T> List<T> copyAnyExclude(Collection<S> sources,Class<T> tClass,String ...excludeProperties){

        if (CollectionUtils.isEmpty(sources)){
            return Collections.EMPTY_LIST;
        }

        return sources.stream().filter((e)->e!=null).map((e)->copyExclude(e,tClass,excludeProperties)).collect(Collectors.toList());
    }

    /**
      * @Description 复制来源对象(属性)到目标对象,并排除给定的属性
      * @Param [source, target, excludeProperties 指定排除哪些属性]
      * @return T
      * @Author yc
      */



    public static <S,T> T copyExclude(S source, T target,String ...excludeProperties) {

        BeanUtils.copyProperties(source,target,excludeProperties);
        return target;
    }

    public static <S,T> T copyExclude(S source, Class<T> tClass,String ...excludeProperties) {

        try {

            T t = tClass.newInstance();
            return copyExclude(source, t,excludeProperties);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

