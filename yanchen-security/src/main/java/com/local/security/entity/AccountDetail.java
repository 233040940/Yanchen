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
 * @Create-By: yanchen 2021/1/10 00:18
 * @Description: TODO
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetail extends MataEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer accountID;

    private boolean banned;

    public AccountDetail(Integer accountID, boolean banned) {
        this.accountID = accountID;
        this.banned = banned;
    }
}
