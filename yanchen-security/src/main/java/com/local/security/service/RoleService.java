package com.local.security.service;

import com.local.common.exception.CustomException;
import com.local.security.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 01:15
 * @Description: 角色服务类
 */
public interface RoleService {


    /**
     * @create-by: yanchen 2021/1/10 01:22
     * @description: 新增角色
     * @param code 角色代码：形如：ADMIN,SUPER_ADMIN,USER...
     * @param note 描述：形如:管理员，超级管理员...
     * @return: void
     */
     void save(String code,String note);

     /**
      * @create-by: yanchen 2021/1/10 01:24
      * @description: 查询角色
      * @param id  角色id
      * @return: com.local.security.entity.Role
      */
     Role  get(Integer id);


     /**
      * @create-by: yanchen 2021/1/10 01:26
      * @description: 分页查询
      * @param id
      * @param code
      * @param note
      * @param offset
      * @param limit
      * @return: org.springframework.data.domain.Page<com.local.security.entity.Role>
      */
     Page<Role> getPage(Integer id,String code,String note,Integer offset,Integer limit);


     /**
      * @create-by: yanchen 2021/1/10 05:26
      * @description: 设置用户角色
      * @param account 账号
      * @param roleID 角色id
      * @return: void
      */
     void saveUserRole(String account,Integer roleID) throws CustomException;


     /**
      * @create-by: yanchen 2021/1/11 22:33
      * @description: 查询所有角色
      * @return: java.util.List<com.local.security.entity.Role>
      */
     List<Role> getList();


     /**
      * @create-by: yanchen 2021/1/12 18:47
      * @description: 根据角色code批量查询
      * @param roleCodes
      * @return: java.util.List<com.local.security.entity.Role>
      */
     List<Role> getList(List<String> roleCodes);

     /**
      * @create-by: yanchen 2021/1/16 03:02
      * @description: 条件查询所有角色
      * @param id
      * @param code 角色代码
      * @param note 角色描述
      * @return: java.util.List<com.local.security.entity.Role>
      */
     List<Role> getList(Integer id,String code,String note);
}
