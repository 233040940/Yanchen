package com.local.netty.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Create-By: yanchen 2021/1/10 00:41
 * @Description: TODO
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu{

    private Integer id;

    private String url;

    private String name;

    private String note;     //描述

    private Integer parentID;     //-1表示含有子菜单节点

}

