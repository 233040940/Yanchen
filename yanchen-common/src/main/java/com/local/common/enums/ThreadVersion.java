package com.local.common.enums;

/**
 * @program: yanchen
 * @description: TODO
 * @author: yc
 * @date: 2020-08-12 14:31
 **/
public enum ThreadVersion {

    SINGLE("单线程版本"),MULTI("多线程版本");

    private String description;

    ThreadVersion(String description){
        this.description=description;
    }
}
