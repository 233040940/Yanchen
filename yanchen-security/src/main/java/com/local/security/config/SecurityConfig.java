//package com.local.security.config;
//
//import com.local.security.component.CustomAccessDeniedHandler;
//import com.local.security.component.CustomAuthenticationFailureHandler;
//import com.local.security.component.CustomUserDetailsService;
//import com.local.security.component.CustomUserNamePasswordAuthenticationFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * @Create-By: yanchen 2021/1/15 05:40
// * @Description: TODO
// */
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Autowired
//    private CustomAccessDeniedHandler customAccessDeniedHandler;
//
//    @Autowired
//    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
//    }
//
//    @Bean
//    CustomUserNamePasswordAuthenticationFilter customUserNamePasswordAuthenticationFilter() throws Exception {
//        CustomUserNamePasswordAuthenticationFilter filter = new CustomUserNamePasswordAuthenticationFilter();
//        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
//        //      filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
//        filter.setAuthenticationManager(authenticationManagerBean());
//        filter.setFilterProcessesUrl("/auth/login");
//        return filter;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.headers().frameOptions().disable();
//        http.authorizeRequests().antMatchers("/register","/web/login.html","/web/js/**","/web/index.html").permitAll()
//                .anyRequest().authenticated()
//                .and().logout().logoutUrl("/logout").permitAll()
//                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
//                .and().addFilterAt(customUserNamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
//}
