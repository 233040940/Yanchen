package com.local.common.repeat;

import com.local.common.entity.BaseParameterEntity;
import com.local.common.utils.JoinerHelper;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description
 * @date 2020-06-02 23:15
 */
public class SimpleCustomCacheManager implements CustomCacheManager {

    private static  final String SEPARATOR="::";      //缓存key分隔符
    private BaseParameterEntity parameterMetaEntity;

    public SimpleCustomCacheManager(BaseParameterEntity parameterMetaEntity){
        this.parameterMetaEntity=parameterMetaEntity;
    }
    @Override
    public String keyGenerator() {
        return JoinerHelper.join(SEPARATOR,parameterMetaEntity.getAccount(), parameterMetaEntity.getAccountIP(), parameterMetaEntity.getHost(), parameterMetaEntity.getPort(),parameterMetaEntity.getInterfaceName(),parameterMetaEntity.getParams());
    }

    @Override
    public String valueGenerator() {
        return parameterMetaEntity.getParams();
    }
}
