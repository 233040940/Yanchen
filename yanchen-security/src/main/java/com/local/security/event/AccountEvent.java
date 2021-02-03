package com.local.security.event;

import com.local.security.entity.Account;
import org.springframework.context.ApplicationEvent;

/**
 * @Create-By: yanchen 2020/10/17 19:18
 * @Description: TODO
 */
public class AccountEvent extends ApplicationEvent {
    private Account account;
    public AccountEvent(Account account){
        super(account);
        this.account=account;
    }
    public Account getAccount(){
        return this.account;
    }
}
