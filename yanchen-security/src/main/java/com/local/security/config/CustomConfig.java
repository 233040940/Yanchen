package com.local.security.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local.common.id.CustomIDGenerator;
import com.local.common.id.snowflake.SnowFlakeIDGenerator;
import com.local.common.utils.ApplicationContextProvider;
import com.local.common.utils.RedisOperationProvider;
import com.local.common.utils.RedisTemplateHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class CustomConfig {

    @Value("${snowflake.dataCenterID}")
    private long dataCenterID;

    @Value("${snowflake.workerID}")
    private long workerID;

    @Bean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        setSerializer(redisTemplate);
        return redisTemplate;
    }

    // 设置value的序列化规则和 key的序列化规则
    private void setSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.setEnableDefaultSerializer(true);
        template.afterPropertiesSet();
    }

    //Spring-data-redis操作redis封装对象
    @Bean
    public RedisOperationProvider redisOperationProvider(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateHelper(redisTemplate(redisConnectionFactory));
    }

    //基于twitter雪花算法生成分布式ID
    @Bean(name = "snowFlakeIDGenerator")
    public CustomIDGenerator<Long> snowFlakeIDGenerator() {
        return new SnowFlakeIDGenerator<>(workerID, dataCenterID);
    }

    //设置jackson序列化规则
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//         通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
//         Include.Include.ALWAYS 默认
//         Include.NON_DEFAULT 属性为默认值不序列化
//         Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
//         Include.NON_NULL 属性为NULL 不序列化
//        字段保留，将null值转为""
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()
//        {
//            @Override
//            public void serialize(Object o, JsonGenerator jsonGenerator,
//                                  SerializerProvider serializerProvider)
//                    throws IOException, JsonProcessingException
//            {
//                jsonGenerator.writeString("");
//            }
//        });
        return objectMapper;
    }

//    @Bean
//    public FilterRegistrationBean filterRegistrationBean(){
//         FilterRegistrationBean bean=new FilterRegistrationBean();
//        ImmutableSet excludeUrls=ImmutableSet.of(new UrlMatcher("/web.*", RequestMethod.GET),
//                                                new UrlMatcher("/web.*", RequestMethod.POST),
//                                                new UrlMatcher("/auth/login",RequestMethod.POST));
//         bean.setFilter(new PermissionFilter(excludeUrls));
//         bean.setOrder(1);
//         bean.addUrlPatterns("/*");
//         return bean;
//    }
//
//    public static void main(String[] args) {
//        ImmutableSet excludeUrls=ImmutableSet.of(new UrlMatcher("/web.*", RequestMethod.GET),
//                new UrlMatcher("/web.*", RequestMethod.POST),
//                new UrlMatcher("/auth/login",RequestMethod.POST));
//        boolean contains = excludeUrls.contains(new UrlMatcher("/web/login.html", RequestMethod.GET));
//        System.out.println(contains);
//
//        String str="/web/login.html";
//        String str2="/web.*";
//        Pattern pattern= Pattern.compile(str2);
//        Matcher matcher = pattern.matcher(str);
//        boolean matches = matcher.matches();
//        System.out.println(matches);
//    }
}