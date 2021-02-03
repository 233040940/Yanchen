package com.local.security.component;

import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseCode;
import com.local.common.enums.ResponseStatus;
import com.local.common.utils.JsonHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Create-By: yanchen 2021/1/12 11:07
 * @Description: 未授权处理器
 */
//@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        ResultResponse  resultResponse = ResultResponse.builder()
                                         .code(ResponseCode.UNAUTHORIZED.getCode())
                                         .status(ResponseStatus.ERROR)
                                         .msg("请求资源未授权,请联系超级管理员").build();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(JsonHelper.serialize(resultResponse));
        writer.flush();
        writer.close();

    }
}
