package com.local.security.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Create-By: yanchen 2021/1/9 22:26
 * @Description: 角色类型
 */
public enum  RoleType {

    ADMIN("管理员"),BASE_USER("普通用户");
    private String description;

    RoleType(String description){
        this.description=description;
    }

    public String getDescription(){
        return this.description;
    }

    public static  RoleType valuesOf(String description){
     return Arrays.stream(RoleType.values())
               .filter((d)->Objects.equals(d.description,description))
               .findFirst().get();
    }

}

