package com.local.common.entity;

import com.local.common.annotation.ExcelField;
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
public class Depart {


    @Id
    private long id;

    @ExcelField(title = "名字")
    private String name;

    @ExcelField(title = "金额",order = 2)
    private Integer amount;

    @ExcelField(title = "年龄",order = 3)
    private Integer age;
}
