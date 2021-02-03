package com.local.security.enums;

/**
 * @Create-By: yanchen 2021/1/15 19:59
 * @Description: 菜单类型
 */
public enum  MenuSource {

    NAVIGATE("导航菜单"),OPERATION("功能操作菜单");
    private String note;
    MenuSource(String note){
        this.note=note;
    }
}
