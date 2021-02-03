package com.local.security.component;

import com.local.security.entity.Menu;
import com.local.security.entity.Role;
import com.local.security.entity.SysPermission;
import com.local.security.service.RoleService;
import com.local.security.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Create-By: yanchen 2021/1/12 15:15
 * @Description:
 */
//@Component(value = "customPermissionChecker")
@Slf4j
public class CustomPermissionChecker {

    private SysPermissionService sysPermissionService;
    private RoleService roleService;

    public CustomPermissionChecker(SysPermissionService sysPermissionService, RoleService roleService) {
        this.sysPermissionService = sysPermissionService;
        this.roleService = roleService;
    }

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
   log.warn("进入自定义权限校验========");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = principal.getClass().getName();
        log.warn("principal========",name);
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            List<String> authority = authorities.stream().map((auth) -> auth.getAuthority().substring(0,4)).collect(Collectors.toList());
            log.warn("authority：{}",authority);
            List<Role> roles = roleService.getList(authority);
            if (CollectionUtils.isEmpty(roles)) {
                return false;
            }
            List<SysPermission> permissions = sysPermissionService.findPermissions(roles);
            String requestURI = request.getRequestURI();
            for (SysPermission permission : permissions) {
                Set<Menu> menus = permission.getMenu();
                for (Menu menu : menus) {
                    if (menu.getUrl().equals(requestURI)) {
                        return true;
                    }
                }
            }
        }
        log.warn("自定义权限直接返回false");
        return false;
    }

}
