package com.local.security.service.impl;

import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.Account;
import com.local.security.entity.Role;
import com.local.security.entity.SysUserRole;
import com.local.security.repository.SysUserRoleRepository;
import com.local.security.service.AccountService;
import com.local.security.service.RoleService;
import com.local.security.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Create-By: yanchen 2021/1/10 01:49
 * @Description: TODO
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    private final SysUserRoleRepository sysUserRoleRepository;

    private final RoleService roleService;

    private final AccountService accountService;

    public SysUserRoleServiceImpl(SysUserRoleRepository sysUserRoleRepository, RoleService roleService, AccountService accountService) {
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.roleService = roleService;
        this.accountService = accountService;
    }

    public static SysUserRoleService getInstance(){
        return ApplicationContextProvider.getBean(SysUserRoleServiceImpl.class);
    }
    @Override
    public void save(Integer accountID, Integer roleID) {
       sysUserRoleRepository.save(SysUserRole.builder().role(roleService.get(roleID)).user(accountService.get(accountID)).build());
    }

    @Override
    public void get(Integer id) {

    }

    @Override
    public SysUserRole findByAccount(Account account) {
        return sysUserRoleRepository.findByUser(account).orElse(null);
    }

    @Override
    public List<SysUserRole> findAllByUser(Account account) {
        return sysUserRoleRepository.findAllByUser(account);
    }

    @Override
    public List<SysUserRole> findAllByRole(Integer roleID) {
        if(Objects.nonNull(roleID)){
            Role role = roleService.get(roleID);
            return sysUserRoleRepository.findAllByRole(role);
        }
        return Collections.emptyList() ;
    }
}
