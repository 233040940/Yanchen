package com.local.common.repeat.standalone.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.local.common.repeat.CustomCacheManager;
import com.local.common.repeat.standalone.StandAloneStrategy;
import org.springframework.util.StringUtils;
import java.util.concurrent.TimeUnit;

/**
 * @author yc
 * @project yanchen
 * @description  基于guava实现单体架构重复提交验证
 * @date 2020-06-20 20:06
 */
public class SimpleGuavaStandAloneStrategy extends StandAloneStrategy {


    private static  final Cache<String,String> GUAVA_CACHE;

    private static  final int initCapacity=16;

    private static  final long maxSize=100;

    private static  final long expire=10;

    static {

        GUAVA_CACHE= CacheBuilder.newBuilder()
                .initialCapacity(initCapacity).maximumSize(maxSize)
                .expireAfterWrite(expire, TimeUnit.SECONDS).build();
    }

    private CustomCacheManager customCacheManager;

    public SimpleGuavaStandAloneStrategy(CustomCacheManager customCacheManager){
        this.customCacheManager = customCacheManager;
    }


    private  void set(){

        GUAVA_CACHE.put(customCacheManager.keyGenerator(), customCacheManager.valueGenerator());
    }


    @Override
    protected synchronized boolean valid() {

        String value = GUAVA_CACHE.getIfPresent(customCacheManager.keyGenerator());

        if(StringUtils.isEmpty(value)){
            set();
            return false;
        }
        return true;
    }

}
