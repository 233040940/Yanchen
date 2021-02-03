package com.local.security.entity.dto;

import java.io.Serializable;

/**
 * @Create-By: yanchen 2021/1/10 05:56
 * @Description: TODO
 */
public class UserRoleConfigDTO implements Serializable {

    private String account;
    private Integer roleId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
