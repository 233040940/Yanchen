package com.local.security.controller;

import com.local.security.entity.Account;
import com.local.security.entity.Menu;
import lombok.Data;

/**
 * @Create-By: yanchen 2020/12/23 11:08
 * @Description: TODO
 */
@Data
public class Test {

   private Account account;
   private Menu menu;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}
