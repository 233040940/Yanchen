package com.local.security.entity.dto;

import com.local.security.enums.OperationSource;
import lombok.Data;

import java.io.Serializable;

/**
 * @Create-By: yanchen 2021/1/12 07:59
 * @Description:
 */
@Data
public class EditPermissionConfigDTO implements Serializable {

    private Integer permissionID;   //权限id
    private Integer roleID;         //角色id
    private Integer[] menuIDs;      //菜单id集合
    private OperationSource source; //操作类型
}
