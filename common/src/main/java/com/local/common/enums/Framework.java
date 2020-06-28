package com.local.common.enums;

/**
 * @project yanchen
 * @description 架构类型枚举
 * @date 2020-06-26 21:38
 */
public enum Framework {

    STANDALONE("单体架构"),DISTRIBUTED("分布式架构");

    private String description;

    Framework(String description){
        this.description=description;
    }
}
