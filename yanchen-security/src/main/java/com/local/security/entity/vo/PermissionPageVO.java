package com.local.security.entity.vo;

import com.local.security.entity.Menu;
import com.local.security.entity.Role;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/12 02:13
 * @Description:
 */
public class PermissionPageVO {
    private List<Menu> menu;
    private Role role;

    public PermissionPageVO(List<Menu> menu, Role role) {
        this.menu = menu;
        this.role = role;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public Role getRole() {
        return role;
    }
}
