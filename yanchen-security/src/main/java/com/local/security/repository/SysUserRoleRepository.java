package com.local.security.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.security.entity.Account;
import com.local.security.entity.Role;
import com.local.security.entity.SysUserRole;

import java.util.List;
import java.util.Optional;

/**
 * @Create-By: yanchen 2021/1/10 01:01
 * @Description: TODO
 */
public interface SysUserRoleRepository extends BaseJpaRepository<SysUserRole,Integer> {


    Optional<SysUserRole> findByUser(Account account);
    List<SysUserRole> findAllByUser(Account account);
    List<SysUserRole> findAllByRole(Role role);
}
