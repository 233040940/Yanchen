package com.local.common;

import com.local.common.annotation.InterfaceRateLimit;
import com.local.common.entity.ResultResponse;
import com.local.common.enums.Framework;
import com.local.common.enums.ResponseStatus;
import com.local.common.repository.BaseJpaRepository;
import com.local.common.utils.JwtProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})

@RestController
@EnableAspectJAutoProxy
@EnableScheduling
@EnableJpaRepositories(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {BaseJpaRepository.class})})
public class CommonApplication {

    public static void main(String[] args)  {

        SpringApplication.run(CommonApplication.class, args);


    }


    @GetMapping(value="/create")
    @InterfaceRateLimit(permitsPerSecond = 0.1)
    public String create(String name){

        String token = JwtProvider.createToken(name,30*1000,"yc");

        return token;
    }

    @GetMapping(value = "/abc")
    @InterfaceRateLimit(permitsPerSecond = 10)
    public ResultResponse use(){

        return  ResultResponse.builder().code(200).status(ResponseStatus.SUCCESS).build();
    }

}





