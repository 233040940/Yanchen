package com.local.security.entity.vo;

import com.local.security.entity.GoodsOrder;
import com.local.security.entity.Account;

/**
 * @Create-By: yanchen 2020/12/25 10:42
 * @Description: TODO
 */
public class AccountOrderVO {

    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public GoodsOrder getOrder() {
        return order;
    }

    public void setOrder(GoodsOrder order) {
        this.order = order;
    }

    private GoodsOrder order;

}
