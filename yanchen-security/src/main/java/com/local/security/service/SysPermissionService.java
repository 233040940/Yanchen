package com.local.security.service;

import com.local.common.exception.CustomException;
import com.local.security.entity.Role;
import com.local.security.entity.SysPermission;
import com.local.security.enums.OperationSource;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 01:30
 * @Description: 系统权限服务
 */
public interface SysPermissionService {


       SysPermission get(Integer id);

       /**
        * @create-by: yanchen 2021/1/12 01:22
        * @description: 保存权限配置
        * @param roleID  角色ID
        * @param menuIDs  菜单ID集合
        * @return: void
        */
       void save(Integer roleID, List<Integer> menuIDs) throws CustomException;

       /**
        * @create-by: yanchen 2021/1/12 02:25
        * @description: 权限分页查询
        * @param roleID  角色ID
        * @param offset
        * @param limit
        * @return: org.springframework.data.domain.Page<com.local.security.entity.SysPermission>
        */
       Page<SysPermission> getPage(Integer roleID,Integer offset,Integer limit);

       /**
        * @create-by: yanchen 2021/1/12 03:15
        * @description: 查询是否存在该角色权限信息
        * @param role
        * @return: boolean
        */
       boolean sysPermissionExist(Role role);
       
       /**
        * @create-by: yanchen 2021/1/12 06:56
        * @description: 编辑权限信息
        * @param permissionID 权限ID
        * @param roleID 角色ID
        * @param menuIDs 菜单ID集合
        * @param source  操作类型
        * @return: void
        */      
       void editPermission(Integer permissionID,Integer roleID,List<Integer> menuIDs, OperationSource source);


       /**
        * @create-by: yanchen 2021/1/12 18:40
        * @description: 通过多个角色信息查询权限
        * @param roles
        * @return: java.util.List<com.local.security.entity.SysPermission>
        */
        List<SysPermission> findPermissions(List<Role> roles);

        /**
         * @create-by: yanchen 2021/1/15 21:03
         * @description: 根据角色查询权限
         * @param role
         * @return: com.local.security.entity.SysPermission
         */
        SysPermission findPermission(Role role);
}
