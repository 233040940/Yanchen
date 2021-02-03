package com.local.security.service.impl;

import com.local.security.entity.Menu;
import com.local.security.service.MenuService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Create-By: yanchen 2021/1/17 00:55
 * @Description: TODO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MenuServiceImplTest extends TestCase {

    @Autowired
    private MenuService menuService;

    @Test
    public void testGetParentMenu() {
        Menu parentMenu = menuService.getParentMenu("/findMenus");
        System.out.println(parentMenu);
    }
}