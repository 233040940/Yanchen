package com.local.common.aspect;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.RateLimiter;
import com.local.common.annotation.InterfaceRateLimit;
import com.local.common.enums.Framework;
import com.local.common.id.CustomIDGenerator;
import com.local.common.id.snowflake.SnowFlakeIDGenerator;
import com.local.common.utils.RedisOperationProvider;
import com.local.common.utils.RedisTemplateHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yc
 * @project yanchen
 * @description 接口限流切面
 * 基于自定义注解 @InterfaceRateLimit 仅做参考
 * 单体架构限流：运用guava RateLimiter
 * 分布式限流：运用redis的list数据结构 leftPush rightPop，配合定时任务实现。
 * 基于redis存在问题：强依赖定时任务实现，不能灵活的进行限流配置，不同接口需要不同的定时任务，系统开销大;且会出现某种情况比如:单位时间内
 * 生成的令牌，并没有被消费完，则会积压，下次进行调度的请求实际会大于限定个数。 解决方案：可在生成令牌时，对list进行清空，(保证原子性)
 * 这样就能保证接口请求在限定个数以内。也可基于zset实现,将score设置为请求的时间戳，判断单位时间间隔内有多少个请求，大于限定个数则返回.
 * 同样需要另外起一个定时任务，对zset进行数据清理，不然会越存越多。
 * @date 2020-06-26 21:42
 */

@Component
@Aspect
@Slf4j
public class InterfaceRateLimitAspect {

    private final RedisOperationProvider<String,Long> redisTemplateHelper;

    private static final String interfaceRateLimitKey = "interfaceRateLimit";      //基于分布式架构redis 接口限流key

    private static final int per_second_max_request = 10;     //每秒最大请求数

    private static final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>(); //基于单体架构guava RateLimiter进行限流

    private RateLimiter rateLimiter;

    public InterfaceRateLimitAspect(RedisOperationProvider<String, Long> redisTemplateHelper) {
        this.redisTemplateHelper = redisTemplateHelper;
    }

    @Pointcut("@annotation(com.local.common.annotation.InterfaceRateLimit)")
    public void pointCut() {
    }


    @Around(value = "pointCut()")
    public final Object interfaceRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method targetMethod = signature.getMethod();

        boolean annotationPresent = targetMethod.isAnnotationPresent(InterfaceRateLimit.class);

        if (annotationPresent) {
            InterfaceRateLimit interfaceRateLimit = targetMethod.getAnnotation(InterfaceRateLimit.class);
            Framework model = interfaceRateLimit.model();
            switch (model) {
                case DISTRIBUTED:
                    Long permit = redisTemplateHelper.listRightPop(interfaceRateLimitKey);
                    if (permit == null) {
                        return "系统繁忙,请稍微再试试";
                    }
                    break;
                default:
                    String methodName = targetMethod.toGenericString();

                    double permitsPerSecond = interfaceRateLimit.permitsPerSecond();

                    RateLimiter rateLimiter = rateLimiters.putIfAbsent(methodName, RateLimiter.create(permitsPerSecond));

                    if (rateLimiter == null) {
                        this.rateLimiter = rateLimiters.get(methodName);
                    } else {
                        this.rateLimiter = rateLimiter;
                    }

                    long waitTimeOut = interfaceRateLimit.waitTimeOut();
                    TimeUnit timeUnit = interfaceRateLimit.timeOutUnit();

                    if (!this.rateLimiter.tryAcquire(waitTimeOut,timeUnit)) {
                        return "系统繁忙,请稍微再试试";
                    }
                    log.info("线程:{}获取到令牌",Thread.currentThread().getName());
            }
        }
        return joinPoint.proceed();
    }

  //  @Scheduled(fixedDelay = 10_000, initialDelay = 10_000)
    public void createPermits() {

        HashSet<Long> ids = Sets.newHashSetWithExpectedSize(per_second_max_request);

        //TODO 此处会创建多个SnowFlakeIDGenerator对象，待优化，一般情况 都会提供一个服务获取分布式ID，避免反复创建对象的开销。
        CustomIDGenerator<Long> snowFlakeIDGenerator = new SnowFlakeIDGenerator(1, 1);

        for (int i = 0; i < per_second_max_request; i++) {

            Long id = snowFlakeIDGenerator.generateID();

            ids.add(id);
        }
        Long count = redisTemplateHelper.listLeftPushAll(interfaceRateLimitKey, ids);//每秒生成10个令牌

        log.info("生成{}个令牌", count);
    }

}
