package com.local.common.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Create-By: yanchen 2021/2/5 01:41
 * @Description: 基于zookeeper 临时有序节点实现分布式锁
 */
public class ZookeeperDistributedLock  implements DistributedLock{

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDistributedLock.class);
    private final int defaultBaseSleepMs = 1000;   //每次重试间隔时间
    private final int defaultMaxSleepMs = 10 * 1000;   //每次重试最大时间
    private final int defaultMaxRetryCount = 3;     //最多重试次数
    private final RetryPolicy defaultRetryPolicy = new ExponentialBackoffRetry(defaultBaseSleepMs, defaultMaxRetryCount, defaultMaxSleepMs);  //重试策略
    private final String defaultServers = "127.0.0.1:2181";      //zk服务器地址
    private final String defaultNodePath = "/maple/check_character_name_lock";      //分布式锁节点名称
    private String servers;
    private String nodePath;
    private RetryPolicy retryPolicy;
    private CuratorFramework client;       //zk客户端
    private InterProcessMutex mutex;     //分布式锁

    public ZookeeperDistributedLock() {
        init(defaultServers, defaultNodePath, defaultRetryPolicy);
    }

    public ZookeeperDistributedLock(String servers, String nodePath, RetryPolicy retryPolicy) {
        init(servers, nodePath, retryPolicy);
    }

    public static ZookeeperDistributedLock getInstance() {
        return ApplicationContextProvider.getBean(ZookeeperDistributedLock.class);
    }

    /**
     * @create-by: yanchen 2021/2/4 04:46
     * @description: 创建zkClient连接zookeeper服务器
     * @param servers     服务器地址:支持负载均衡 --》ip:port,ip:port...
     * @param retryPolicy 重试策略
     * @return: void
     */
    private void linkZkServer(String servers, RetryPolicy retryPolicy) {
        this.client = CuratorFrameworkFactory.newClient(servers, retryPolicy);
        client.start();
    }

    //初始化分布式锁
    private void init(String servers, String nodePath, RetryPolicy retryPolicy) {
        try {
            linkZkServer(servers, retryPolicy);
        } catch (Exception e) {
            throw new RuntimeException(String.format("连接zookeeper服务器失败,地址[%s]",servers));
        }
        this.mutex = new InterProcessMutex(client, nodePath);
    }

    //获取锁
    @Override
    public void lock() {
        try {
            mutex.acquire();
        } catch (Exception e) {
            LOGGER.warn("获取zk分布式锁失败,节点{}", nodePath, e);
        }
    }

    //释放锁
    @Override
    public void release() {
        try {
            mutex.release();
        } catch (Exception e) {
            LOGGER.warn("释放zk分布式锁失败,节点{}", nodePath, e);
        }
    }


    @Override
    public void lock(long timeOut, TimeUnit timeUnit) {

    }
}
