package com.local.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends MataEntity {

    @Id
    private Long id;
    @Column
    private String account;
    @JsonIgnore
    private String password;
    private String email;
    private String name;
    private String mobile;
    private String salt;

}
