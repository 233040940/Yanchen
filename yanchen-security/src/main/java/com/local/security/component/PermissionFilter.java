package com.local.security.component;

import com.google.common.collect.Sets;
import com.local.common.entity.ResultResponse;
import com.local.common.entity.UrlMatcher;
import com.local.common.enums.ResponseCode;
import com.local.common.enums.ResponseStatus;
import com.local.common.utils.JwtProvider;
import com.local.security.entity.Account;
import com.local.security.entity.Menu;
import com.local.security.entity.Role;
import com.local.security.entity.SysPermission;
import com.local.security.entity.SysUserRole;
import com.local.security.service.impl.AccountServiceImpl;
import com.local.security.service.impl.MenuServiceImpl;
import com.local.security.service.impl.SysPermissionServiceImpl;
import com.local.security.service.impl.SysUserRoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Create-By: yanchen 2021/1/15 20:28
 * @Description: TODO
 */
@Slf4j
public class PermissionFilter implements Filter {

    private static final String TOKEN_REQUEST_HEADER = "auth";
    private static final String SUPER_ADMIN_CODE = "SUPER_ADMIN";

    private Set<UrlMatcher> excludeUrls;      //不拦截token的白名单

    public PermissionFilter(Set<UrlMatcher> excludeUrls) {
        this.excludeUrls = Optional.ofNullable(excludeUrls).orElse(Sets.newHashSetWithExpectedSize(0));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        boolean contains = excludeUrls.contains(new UrlMatcher(url, RequestMethod.valueOf(method.toUpperCase())));
        if (!contains) {
            String token = httpServletRequest.getHeader(TOKEN_REQUEST_HEADER);
            if (StringUtils.hasText(token)) {
                String username = JwtProvider.parseToken(token).getBody().getSubject();
                if (StringUtils.hasText(username)) {
                    boolean permission = checkPermissionHandle(username, httpServletRequest.getRequestURI());
                    if (!permission) {
                        chain.doFilter(request, response);
                        return;
                    }
                }
            }
            ResultResponse<Object> result = ResultResponse.builder().code(ResponseCode.UNAUTHORIZED.getCode())
                    .status(ResponseStatus.ERROR).msg("请求资源未授权，请联系超级管理员")
                    .build();
            ResultResponse.outPut(((HttpServletResponse) response), result);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * @create-by: yanchen 2021/1/17 02:19
     * @description: 权限校验处理
     * @param username 账号
     * @param requestUri 请求uri
     * @return: boolean
     */
    private boolean checkPermissionHandle(String username,String requestUri){
        return checkAccount(username, requestUri);
    }

   /**
    * @create-by: yanchen 2021/1/17 02:16
    * @description: 校验账号
    * @param username 账号
    * @param requestUri 请求uri
    * @return: boolean
    */
    private boolean checkAccount(String username, String requestUri) {
        Account account = AccountServiceImpl.getInstance().get(username);
        return Objects.isNull(account)?true:checkUserRole(account, requestUri);
    }

    /**
     * @create-by: yanchen 2021/1/17 02:17
     * @description: 校验该账号是否分配角色
     * @param account 账号信息
     * @param requestUri  请求uri
     * @return: boolean
     */
    private boolean checkUserRole(Account account,String requestUri){
        SysUserRole userRole = SysUserRoleServiceImpl.getInstance().findByAccount(account);
        if(Objects.isNull(userRole)){
            return true;
        }
        Role role = userRole.getRole();
        if(Objects.isNull(role)){
            return true;
        }
        //如果该账号角色为超级管理员则直接放行，否则校验请求是否被权限
        return org.apache.commons.lang3.StringUtils.equals(role.getCode(),SUPER_ADMIN_CODE)?false:checkPermission(role,requestUri);
    }

    /**
     * @create-by: yanchen 2021/1/17 02:18
     * @description: 校验该角色是否被被权限
     * @param role 角色信息
     * @param requestUri  请求uri
     * @return: boolean
     */
    private boolean checkPermission(Role role,String requestUri){
        SysPermission permission = SysPermissionServiceImpl.getInstance().findPermission(role);
        if(Objects.nonNull(permission)){
            Set<Menu> permissionMenus = permission.getMenu();   //被权限的菜单
            if(!CollectionUtils.isEmpty(permissionMenus)){
                Set<String> permissionMenuUrls = permissionMenus.stream().map((m) -> m.getUrl()).collect(Collectors.toSet());
                if (permissionMenuUrls.contains(requestUri)) {   //如果是此菜单被权限则直接返回
                    return true;
                } else {         //否则校验此菜单的上级菜单是否被权限
                    Menu parentMenu = MenuServiceImpl.getInstance().getParentMenu(requestUri);
                    if (Objects.nonNull(parentMenu)) {
                        return permissionMenuUrls.contains(parentMenu.getUrl());
                    }
                }
            }
        }
        return false;
    }
}
