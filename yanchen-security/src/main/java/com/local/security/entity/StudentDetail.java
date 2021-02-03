package com.local.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @Create-By: yanchen 2021/1/23 12:13
 * @Description: TODO
 */
@Entity
@Data
public class StudentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer studentId;
    private String email;
    private String phone;
    @OneToOne
    @JoinColumn(name = "studentId",insertable = false,updatable = false)
    @JsonIgnore
    private Student student;
}
