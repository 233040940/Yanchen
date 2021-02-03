package com.local.security.entity;

import com.local.security.enums.MenuSource;
import com.local.security.enums.OperationSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Create-By: yanchen 2021/1/10 00:41
 * @Description: TODO
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends MataEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String url;

    @Column(unique = true)
    private String name;

    private String note;     //描述

    private Integer parentID;     //-1表示含有子菜单节点

    @Enumerated(value=EnumType.STRING)
    private OperationSource operationSource;     //操作类型

    @Enumerated(value=EnumType.STRING)
    private MenuSource menuSource;          //菜单类型

}
