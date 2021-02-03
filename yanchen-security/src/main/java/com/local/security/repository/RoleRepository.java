package com.local.security.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.security.entity.Role;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 01:01
 * @Description: TODO
 */
public interface RoleRepository extends BaseJpaRepository<Role,Integer> {

    List<Role> findAllByCodeIn(List<String> codes);
}
