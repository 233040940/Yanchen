package com.local.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * @Create-By: yanchen 2021/1/10 00:27
 * @Description: TODO
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints={@UniqueConstraint(columnNames={"user_id"})})
public class SysUserRole  extends MataEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id",unique = true)
    private Account user;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
}
