package com.local.security.component;

import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Create-By: yanchen 2021/1/13 13:04
 * @Description: TODO
 */
//@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        ResultResponse result = ResultResponse.builder()
                .msg("登录成功").status(ResponseStatus.SUCCESS)
                .code(0).build();

        ResultResponse.outPut(httpServletResponse, result);

    }
}
