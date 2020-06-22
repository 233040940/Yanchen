package com.local.common;

import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import com.local.common.utils.JwtProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})

@RestController
public class CommonApplication {

    public static void main(String[] args)  {

        SpringApplication.run(CommonApplication.class, args);


    }


    @GetMapping(value="/create")
    public String create(String name){

        String token = JwtProvider.createToken(name,30*1000,"yc");

        return token;
    }

    @GetMapping(value = "/abc")
    public ResultResponse use(){

        return  ResultResponse.builder().msg("helloworld").status(ResponseStatus.SUCCESS).code(200).build();
    }

}





