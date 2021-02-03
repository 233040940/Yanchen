package com.local.security.service;

import com.local.security.entity.Account;
import com.local.security.entity.SysUserRole;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 01:28
 * @Description: 系统用户角色服务
 */
public interface SysUserRoleService {

    void save(Integer accountID, Integer roleID);

    void get(Integer id);

    SysUserRole findByAccount(Account account);

    List<SysUserRole> findAllByUser(Account account);

    List<SysUserRole> findAllByRole(Integer roleID);
}
