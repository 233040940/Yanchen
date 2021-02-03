package com.local.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.local.security",exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories
@EnableTransactionManagement
public class SecurityApplication {
    public static void  main(String [] args){
      SpringApplication.run(SecurityApplication.class);
    }
}

