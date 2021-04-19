package com.local.springbatch.demo.entity.jpa.module.task;

import com.local.common.entity.MataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @Create-By: yanchen 2021/4/9 01:49
 * @Description: 推广任务
 */
@Entity
public class ExtensionTask  extends MataEntity {

    @Id
    private Integer id;
    private String name;    //名称
    @Lob
    @Column(columnDefinition="text")
    private String note;    //简介

}
