package com.local.common.office.excel.entity;

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

    private String name;

    private String address;

    private int phone;

    private double amount;

    private Date workDate;

}
