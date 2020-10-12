package com.local.common.repeat;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 自定义缓存管理器
 * @date 2020-06-02 23:13
 */
public interface CustomCacheManager {

    String  keyGenerator();

    String  valueGenerator();
}
