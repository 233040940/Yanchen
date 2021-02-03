package com.local.security.enums;

/**
 * @Create-By: yanchen 2021/1/10 00:48
 * @Description: 操作类型
 */
public enum OperationSource {

    INSERT("增加"),DELETE("删除"),UPDATE("修改"),SELECT("查询"),DEFAULT("默认");

    private String note;
    OperationSource(String note){
        this.note=note;
    }

}
