package com.local.common.utils;

import java.util.concurrent.TimeUnit;

/**
 * @Create-By: yanchen 2021/2/5 01:35
 * @Description: 分布式锁
 */
public interface DistributedLock {

    void lock();

    void lock(long timeOut,TimeUnit timeUnit);

    void release();


}
