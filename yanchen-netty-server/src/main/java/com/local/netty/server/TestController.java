package com.local.netty.server;

import com.google.common.collect.ImmutableList;
import com.local.common.entity.ResultResponse;
import com.local.netty.server.entity.Menu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Create-By: yanchen 2021/3/3 22:10
 * @Description: TODO
 */
@RestController
public class TestController {

    @GetMapping(value = "/findMenus")
    public ResultResponse findMenus(){
        ImmutableList menus=ImmutableList.of(
                     Menu.builder().name("用户管理").note("页面菜单").parentID(-1).url("index.html").id(1).build(),
                      Menu.builder().name("角色管理").note("页面菜单").parentID(-1).url("role.html").id(2).build(),
                     Menu.builder().name("权限管理").note("页面菜单").parentID(-1).url("permission.html").id(3).build());
        return ResultResponse.builder().code(0).data(menus).count((long) menus.size()).build();
    }
}
