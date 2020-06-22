package com.local.common.repeat.distributed.redis;

import com.local.common.repeat.CustomCacheManager;
import com.local.common.repeat.distributed.DistributedStrategy;
import com.local.common.utils.ApplicationContextProvider;
import com.local.common.utils.RedisTemplateHelper;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 分布式架构基于redis重复提交验证策略。【根据account，accountIP，host，port，params进行验证】
 * @date 2020-06-02 20:25
 */
public class SimpleRedisDistributedStrategy extends DistributedStrategy {


    private static  final long EXPIRE=10;           //key过期时间秒级

    private CustomCacheManager customCacheManager;

    private RedisTemplateHelper redisTemplateHelper;

    public SimpleRedisDistributedStrategy(CustomCacheManager customCacheManager){

        this.customCacheManager = customCacheManager;
        this.setRedisTemplateHelper(initRedisTemplateHelper());
    }

    private RedisTemplateHelper initRedisTemplateHelper(){

    return ApplicationContextProvider.getBean("redisTemplateHelper",RedisTemplateHelper.class);
    }

    private void setRedisTemplateHelper(RedisTemplateHelper redisTemplateHelper){

        this.redisTemplateHelper=redisTemplateHelper;
    }

    @Override
    public  boolean valid() {

       return redisTemplateHelper.setIfAbsent(customCacheManager.keyGenerator(), customCacheManager.valueGenerator(),EXPIRE);
    }

}
