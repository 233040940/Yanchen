package com.local.security;

import com.local.security.entity.Account;
import com.local.security.entity.Menu;
import lombok.Data;

/**
 * @Create-By: yanchen 2021/3/7 17:06
 * @Description: TODO
 */
@Data
public class TestVO{
    private Account account;
    private Menu menu;

    public TestVO(){}
}
