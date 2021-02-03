package com.local.security.service;

import com.local.common.exception.CustomException;
import com.local.security.entity.Menu;
import com.local.security.enums.MenuSource;
import com.local.security.enums.OperationSource;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 01:41
 * @Description: 菜单服务
 */
public interface MenuService {


    /**
     * @create-by: yanchen 2021/1/10 01:42
     * @description: 新增菜单
     * @param url 菜单url
     * @param name 名称
     * @param parentID 上级菜单id
     * @param operationSource 操作类型:便于区分增删改查,导航节点菜单建议采用 OperationSource.DEFAULT
     * @param  menuSource    菜单类型
     * @return: void
     */
    void save(String url, String name,String note, Integer parentID, OperationSource operationSource,MenuSource menuSource);


    Menu get(Integer id);


    /**
     * @create-by: yanchen 2021/1/11 22:36
     * @description: 查询菜单是否存在
     * @param id
     * @return: boolean
     */
    boolean exist(Integer id) throws CustomException;

    /**
     * @create-by: yanchen 2021/1/11 22:35
     * @description: 菜单分页查询
     * @param url    菜单url
     * @param name   菜单名称
     * @param parentID 上级菜单id
     * @param operationSource  操作类型
     * @param menuSource  菜单类型
     * @param offset
     * @param limit
     * @return: org.springframework.data.domain.Page<com.local.security.entity.Menu>
     */
    Page<Menu> getPage(String url,String name,Integer parentID,OperationSource operationSource,MenuSource menuSource,Integer offset,Integer limit);

    /**
     * @create-by: yanchen 2021/1/11 22:37
     * @description: 查询所有菜单
     * @return: java.util.List<com.local.security.entity.Menu>
     */
    List<Menu> getList();


    List<Menu> getList(List<Integer> ids);

    List<Menu> getListNot(List<Integer> ids);

    /**
     * @create-by: yanchen 2021/1/15 20:02
     * @description: 通过菜单类型获取所有菜单
     * @param menuSource
     * @return: java.util.List<com.local.security.entity.Menu>
     */
    List<Menu> getList(MenuSource menuSource);

    /**
     * @create-by: yanchen 2021/1/16 04:11
     * @description: 条件查询菜单
     * @param id
     * @param url  菜单url
     * @param name 菜单名称
     * @return: com.local.security.entity.Menu
     */
    Menu get(Integer id,String url,String name);

    /**
     * @create-by: yanchen 2021/1/17 00:52
     * @description: 通过url查询父菜单
     * @param url
     * @return: com.local.security.entity.Menu
     */
    Menu getParentMenu(String url);

    List<Menu> getParentMenus(String url);

    Menu findByUrl(String url);

    Menu edit(Menu menu);
}
