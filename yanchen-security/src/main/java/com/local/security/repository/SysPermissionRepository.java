package com.local.security.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.security.entity.Role;
import com.local.security.entity.SysPermission;

import java.util.List;
import java.util.Optional;

/**
 * @Create-By: yanchen 2021/1/10 01:03
 * @Description:
 */
public interface SysPermissionRepository extends BaseJpaRepository<SysPermission,Integer> {

    Optional<SysPermission> findByRole(Role role);

    List<SysPermission> findAllByRoleIn(List<Role> roles);
}
