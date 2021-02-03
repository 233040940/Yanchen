package com.local.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Create-By: yanchen 2021/1/10 00:22
 * @Description: TODO
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints={@UniqueConstraint(columnNames={"code","note"})})
public class Role  extends MataEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String code;
    @Column(unique = true)
    private String note;
}
