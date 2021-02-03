package com.local.common.office.excel.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-21 14:46
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString

@Entity
public class Depart implements ExcelTemplate{


    @Id
    private long id;

    private String name;

    private Integer amount;

    private Integer age;
}
