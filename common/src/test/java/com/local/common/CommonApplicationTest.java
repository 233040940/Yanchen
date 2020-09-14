package com.local.common;

import com.google.common.collect.Sets;
import com.local.common.utils.RedisOperationProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.Set;

/**
 * @program: yanchen
 * @description: TODO
 * @author: yc
 * @date: 2020-08-31 19:06
 **/

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CommonApplicationTest {

//    @Autowired
//    private RedisOperationProvider<String,User> redisOperationProvider;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void redisTest(){

        Long users = redisTemplate.opsForZSet().size("users");

        Long users1 = redisTemplate.opsForZSet().zCard("users");

        System.out.println(users+" "+users1);

    }
}

@Data
@AllArgsConstructor
class User implements Serializable {

    private String name;
    private int age;
}