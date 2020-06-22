package com.local.common.enums;

/**
 * @project yanchen
 * @description excel模版对象是否在意title,用于区分生成excel还是读取excel
 * @date 2020-06-22 01:45
 */
public enum ExcelTemplateTitleOption {

    CARE("在意"),NOT_CARE("不在意");

    private String description;

    ExcelTemplateTitleOption(String description){
        this.description=description;
    }
}
