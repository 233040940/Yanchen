package com.local.common;

import com.google.common.collect.ImmutableSet;
import com.local.common.entity.UrlMatcher;
import com.local.common.filter.RootTokenValidFilter;
import com.local.common.filter.validator.IncludeSubjectTokenValidator;
import com.local.common.filter.validator.SimpleTokenValidator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-15 13:38
 */

@Configuration
public class CustomConfig  {

    @Bean
    public FilterRegistrationBean registrationBean(){


        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean();

        ImmutableSet<UrlMatcher> excludeUrls=ImmutableSet.of(new UrlMatcher("/create", RequestMethod.GET),new UrlMatcher("/abc",RequestMethod.GET));

        filterRegistrationBean.setFilter(new RootTokenValidFilter(excludeUrls,null,new IncludeSubjectTokenValidator("yc")));

        filterRegistrationBean.addUrlPatterns("/*");

        filterRegistrationBean.setName("tokenValidFilter");

        filterRegistrationBean.setOrder(1);

        return filterRegistrationBean;
    }

}
