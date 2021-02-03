package com.local.security.controller;

import com.google.common.collect.Lists;
import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import com.local.security.entity.SysPermission;
import com.local.security.entity.dto.EditPermissionConfigDTO;
import com.local.security.entity.dto.PermissionConfigDTO;
import com.local.security.service.SysPermissionService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Create-By: yanchen 2021/1/12 01:17
 * @Description: TODO
 */
@RestController
public class PermissionController {

    private SysPermissionService sysPermissionService;

    public PermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    @PostMapping(value = "/permissionConfig")
    public ResultResponse permissionConfig(@RequestBody PermissionConfigDTO dto){
          sysPermissionService.save(dto.getRoleID(), dto.getMenuIDs());
          return ResultResponse.builder().code(0).status(ResponseStatus.SUCCESS).build();
    }

    @GetMapping(value = "/findPermissions")
    public ResultResponse findPermissions(Integer page, Integer limit, @RequestParam(name = "roleID",required = false)Integer roleID){
        Page<SysPermission> page1 = sysPermissionService.getPage(roleID, page - 1, limit);
        return ResultResponse.builder().code(0).status(ResponseStatus.SUCCESS).count(page1.getTotalElements()).data(page1.getContent()).build();

    }
    @PostMapping(value = "/editPermissionConfig")
    public ResultResponse editPermission(@RequestBody EditPermissionConfigDTO dto){
        sysPermissionService.editPermission(dto.getPermissionID(),dto.getRoleID(), Lists.newArrayList(dto.getMenuIDs()),dto.getSource());
        return ResultResponse.builder().code(0).status(ResponseStatus.SUCCESS).build();
    }
}
