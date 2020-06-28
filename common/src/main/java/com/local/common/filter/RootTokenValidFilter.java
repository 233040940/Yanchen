package com.local.common.filter;

import com.google.common.collect.*;
import com.local.common.entity.ResultResponse;
import com.local.common.entity.UrlMatcher;
import com.local.common.enums.ResponseCode;
import com.local.common.enums.ResponseStatus;
import com.local.common.filter.validator.AbstractTokenValidator;
import com.local.common.utils.JsonHelper;
import com.local.common.utils.SplitterHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Set;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 验证token过滤器
 * @date 2020-06-12 07:07
 */
public class RootTokenValidFilter implements Filter {

    private static final String TOKEN_HEADER_NAME = "token";      //http请求头中token名称

    private static final String STATIC_RESOURCES_SUFFIX_SEPARATOR=".";    //静态资源请求后缀分隔符

    private Set<UrlMatcher> excludeUrls;      //不拦截token的白名单

    private AbstractTokenValidator tokenValidator;     //token验证器

    private Set<String> excludeStaticResources;        //排除静态资源后缀集合

    public RootTokenValidFilter(Set<UrlMatcher> excludeUrls,Set<String> excludeStaticResources, AbstractTokenValidator tokenValidator) {

        this.excludeUrls = Optional.ofNullable(excludeUrls).orElse(Sets.newHashSetWithExpectedSize(0));
        this.tokenValidator = tokenValidator;
        this.excludeStaticResources=Optional.ofNullable(excludeStaticResources).orElse(Sets.newHashSetWithExpectedSize(0));
    }

    @Override
    public void init(FilterConfig filterConfig)   {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String url = httpServletRequest.getRequestURI();

        String method = httpServletRequest.getMethod();

        if(!excludeStaticResources.contains(SplitterHelper.splitReturnLast(STATIC_RESOURCES_SUFFIX_SEPARATOR,url).toLowerCase())){   //过滤静态文件资源请求
            try {
                if (!excludeUrls.contains(new UrlMatcher(url, RequestMethod.valueOf(method.toUpperCase())))) {         //过滤白名单

                    String token = httpServletRequest.getHeader(TOKEN_HEADER_NAME);

                    if (!(StringUtils.hasText(token)&&tokenValidator.tokenEffective(token))) {

                        print((HttpServletResponse) response,"token校验失败",ResponseStatus.ERROR,ResponseCode.UNAUTHORIZED.getCode());
                        return;
                    }
                }

            } catch (IllegalArgumentException e) {

                print((HttpServletResponse) response, "不支持的http请求类型", ResponseStatus.ERROR, ResponseCode.CLIENT_FAIL.getCode());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private void print(HttpServletResponse response, String msg, ResponseStatus status, Integer code) throws IOException {

        ResultResponse result = ResultResponse.builder()
                .code(code)
                .status(status)
                .msg(msg).build();

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter outer = response.getWriter();
        outer.print(JsonHelper.serialize(result));
        outer.flush();
        outer.close();
    }

}
