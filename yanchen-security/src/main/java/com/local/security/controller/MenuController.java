package com.local.security.controller;

import com.local.common.entity.ResultResponse;
import com.local.security.entity.Menu;
import com.local.security.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Create-By: yanchen 2021/1/10 06:15
 * @Description: TODO
 */
@RestController
@Slf4j
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping(value = "/addMenu")
    public ResultResponse addMenu(@RequestBody Menu menu){
        log.warn("Menu:{}",menu.toString());
        try {
            menuService.save(menu.getUrl(),menu.getName(), menu.getNote(), menu.getParentID(), menu.getOperationSource(),menu.getMenuSource());
        } catch (Exception e) {
           if(e instanceof DataIntegrityViolationException){
               return ResultResponse.builder().code(400).msg("已经存在此菜单，不能重复添加").build();
           }
        }
        return ResultResponse.builder().code(0).build();
    }
    @GetMapping(value = "/findMenus")
    public ResultResponse findAll(Integer page,Integer limit,Menu menu){

        Page<Menu> page1 = menuService.getPage(menu.getUrl(), menu.getNote(), menu.getParentID(), menu.getOperationSource(),menu.getMenuSource(), page - 1, limit);
        return ResultResponse.builder().code(0).count(page1.getTotalElements()).data(page1.getContent()).build();
    }

    @GetMapping(value = "/findAllMenus")
    public ResultResponse findAll(){
        List<Menu> menus = menuService.getList();
        return ResultResponse.builder().code(0).data(menus).build();
    }

    @GetMapping(value = "/findAllMenusNotIds")
    public ResultResponse findAllNotIds(Integer[] ids){
        List<Menu> menus = menuService.getListNot(Arrays.asList(ids));
        return ResultResponse.builder().code(0).data(menus).build();
    }

    @GetMapping(value = "/editMenu1")
    public ResultResponse editMenu(Integer id,String name,String note){
        Menu menu = menuService.get(id);
        Menu menu1=new Menu();
        menu1.setNote(note);
        menu1.setName(name);
        menu1.setParentID(menu.getParentID());
        menu1.setOperationSource(menu.getOperationSource());
        menu1.setMenuSource(menu.getMenuSource());
        menu1.setId(menu.getId());
        Menu edit = menuService.edit(menu1);
        return ResultResponse.builder().code(0).data(edit).build();
    }
    @GetMapping(value = "/editMenu2")
    public ResultResponse editMenu(Integer id){
        Menu menu = menuService.get(id);
        menu.setNote("测试注解");
        Menu edit = menuService.edit(menu);
        return ResultResponse.builder().code(0).data(edit).build();
    }
}

