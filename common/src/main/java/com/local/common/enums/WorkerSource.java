package com.local.common.enums;

/**
 * @author WorkerSource
 * @project yanchen
 * @date 2020-06-25 19:41
 */
public enum WorkerSource {

    FORKJOIN("基于forkJoin框架进行多线程操作"),FUTURE("基于future进行多线程操作");

    private String description;

    WorkerSource(String description){
        this.description=description;
    }
}
