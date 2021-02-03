package com.local.security.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Create-By: yanchen 2021/1/12 01:46
 * @Description: TODO
 */
@Data
public class PermissionConfigDTO implements Serializable {
    private Integer roleID;
    private List<Integer> menuIDs;
}
