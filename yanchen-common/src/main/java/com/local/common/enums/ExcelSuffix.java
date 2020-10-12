package com.local.common.enums;

/**
 * @author yc
 * @description excel后缀格式枚举
 * @date 2020-06-18 10:42
 */
public enum ExcelSuffix {

    XLS(".xls", "03版excel"), XLSX(".xlsx", "07版excel"),UP_XLSX(".xlsx","07升级版");

    private String suffix;
    private String description;

    ExcelSuffix(String suffix, String description) {
        this.suffix = suffix;
        this.description = description;
    }

    public String getSuffix() {
        return suffix;
    }

}
