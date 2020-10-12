package com.local.common.enums;

/**
 * @program: yanchen
 * @description: TODO
 * @author: yc
 * @date: 2020-08-06 18:04
 **/
public enum DocSuffix {

    PDF(".pdf"),WORD(".doc");

    private String suffix;

    DocSuffix(String suffix){
        this.suffix=suffix;
    }

    public String getSuffix() {
        return suffix;
    }}
