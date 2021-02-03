package com.local.security.component;

import com.local.common.utils.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Create-By: yanchen 2021/1/13 13:58
 * @Description: token过滤
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    protected static final String TOKEN_HEADER_NAME = "auth";
//    private List<UrlMatcher> excludeUrls;
//    private AbstractTokenValidator tokenValidator;
//    public JwtAuthenticationFilter(List<UrlMatcher> excludeUrls, AbstractTokenValidator tokenValidator) {
//        this.excludeUrls=excludeUrls;
//        this.tokenValidator=tokenValidator;
//    }
    public JwtAuthenticationFilter(){}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       log.warn("进入jwt filter===========");
//        if(!whiteList(request)){
//            log.warn("黑名单");
            String auth = request.getHeader(TOKEN_HEADER_NAME);
            if(StringUtils.hasText(auth)){
                String subject = JwtProvider.parseToken(auth).getBody().getSubject();
                if(StringUtils.hasText(subject) && Objects.isNull(SecurityContextHolder.getContext())){
                    UserDetails userDetails = CustomUserDetailsService.getInstance().loadUserByUsername(subject);
                    if(Objects.nonNull(userDetails)){
                        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(), userDetails.getAuthorities());
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            }
//        }
        filterChain.doFilter(request, response);
    }
//    protected boolean whiteList(HttpServletRequest request) {
//        String url = request.getRequestURI();
//        String method = request.getMethod();
//        try {
//            return excludeUrls.contains(new UrlMatcher(url, RequestMethod.valueOf(method.toUpperCase())));
//        } catch (IllegalArgumentException e) {
//            return false;
//        }
//    }
}
