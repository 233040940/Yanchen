package com.local.security.component;

import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Create-By: yanchen 2021/1/12 19:46
 * @Description: TODO
 */
//@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        ResultResponse  result = ResultResponse.builder()
                                 .msg(e.getMessage()).status(ResponseStatus.ERROR)
                                 .code(400).build();
      ResultResponse.outPut(httpServletResponse,result);
    }
}
