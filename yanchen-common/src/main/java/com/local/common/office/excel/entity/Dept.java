package com.local.common.office.excel.entity;

import com.local.common.annotation.ExcelField;
import com.local.common.office.excel.entity.ExcelTemplate;
import lombok.*;
import java.util.Date;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-17 20:48
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dept implements ExcelTemplate {

    @ExcelField(title="姓名",order=1)
    private String name;

    @ExcelField(title = "地址",order= 3)
    private String address;

    @ExcelField(title = "手机号",order = 5)
    private int phone;

    @ExcelField(title="工资",order = 7)
    private double amount;

    @ExcelField(title = "入职时间",order = 9)
    private Date workDate;

}
