package com.local.security.component;

import com.google.common.collect.Maps;
import com.local.common.utils.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Create-By: yanchen 2021/1/14 17:56
 * @Description: 自定义CustomUserNamePasswordAuthenticationFilter以支持登录采用json格式
 */
public class CustomUserNamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("请求方法类型不支持 " + request.getMethod());
        }
        String contentType = request.getContentType();
        Map parameters = Maps.newHashMap();
        if (StringUtils.equals(contentType, MediaType.APPLICATION_JSON_UTF8_VALUE) || StringUtils.equals(contentType, MediaType.APPLICATION_JSON_VALUE)) {
            try {
                parameters = JsonHelper.deSerializable(request.getInputStream(), HashMap.class);   //将请求参数反序列化成map
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //此处可添加其他处理数据,如验证码
            }
            String username = ((String) parameters.get(getUsernameParameter()));
            String password = ((String) parameters.get(getPasswordParameter()));
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

}
