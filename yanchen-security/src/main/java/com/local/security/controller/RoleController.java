package com.local.security.controller;

import com.local.common.entity.ResultResponse;
import com.local.common.exception.CustomException;
import com.local.security.entity.Role;
import com.local.security.entity.SysUserRole;
import com.local.security.entity.dto.UserRoleConfigDTO;
import com.local.security.service.RoleService;
import com.local.security.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 02:58
 * @Description: TODO
 */
@RestController
@Slf4j
public class RoleController {

    private final RoleService roleService;

    private final SysUserRoleService sysUserRoleService;

    public RoleController(RoleService roleService, SysUserRoleService sysUserRoleService) {
        this.roleService = roleService;
        this.sysUserRoleService = sysUserRoleService;
    }

    @PostMapping(value = "/addRole")
    public ResultResponse add(@RequestBody Role role){
        try {
            roleService.save(role.getCode(), role.getNote());
        } catch (Exception e) {
            if(e instanceof DataIntegrityViolationException){
                return ResultResponse.builder().code(400).msg("已经存在此角色，不可重复添加").build();
            }else {
                log.warn("发现异常:{}", e.getCause());
            }
        }
        return ResultResponse.builder().code(0).build();
    }
    @GetMapping(value = "/findRoles")
    public ResultResponse getList(Integer page, Integer limit){
        Page<Role> page1 = roleService.getPage(null, null, null, page - 1, limit);
        return ResultResponse.builder().code(0).count(page1.getTotalElements()).data(page1.getContent()).build();
    }

    @GetMapping(value = "/findAllRoles")
    public ResultResponse findAll(){
        List<Role> list = roleService.getList(null,null,null);
        return ResultResponse.builder().code(0).data(list).build();
    }

    @PostMapping(value = "/userRoleConfig")
    public ResultResponse userRoleConfig(@RequestBody UserRoleConfigDTO dto){

        try {
            roleService.saveUserRole(dto.getAccount(),dto.getRoleId());
        } catch (Exception e) {
           if(e instanceof CustomException){
               return ResultResponse.builder().code(400).msg(e.getMessage()).build();
           }
           if(e instanceof DataIntegrityViolationException){
               return ResultResponse.builder().code(400).msg("同个账号只能拥有一种角色").build();
           }
        }
        return ResultResponse.builder().code(0).build();
    }

    @GetMapping(value = "/findUsers")
    public ResultResponse findUsers(Integer roleId){
        List<SysUserRole> allByRole = sysUserRoleService.findAllByRole(roleId);
        return ResultResponse.builder().code(0).data(allByRole).build();
    }
}
