package com.local.security.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @Create-By: yanchen 2021/1/23 12:11
 * @Description: TODO
 */
@Entity
@Data
public class Student extends MataEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer teacherId;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "teacherId",insertable = false,updatable = false)
    private Teacher teacher;
    @OneToOne(mappedBy = "student")
    private StudentDetail studentDetail;
}
