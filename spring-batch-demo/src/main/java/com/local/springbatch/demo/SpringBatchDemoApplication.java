package com.local.springbatch.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class SpringBatchDemoApplication {



	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemoApplication.class, args);
	}



}
