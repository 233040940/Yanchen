package com.local.common.enums;

/**
 * @author ReaderType
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-25 19:41
 */
public enum ReaderType {

    FORKJOIN("基于forkJoin框架进行多线程操作"),FUTURE("基于future进行多线程操作");

    private String description;

    ReaderType(String description){
        this.description=description;
    }
}
