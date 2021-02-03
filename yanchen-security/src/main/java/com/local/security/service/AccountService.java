package com.local.security.service;

import com.local.security.entity.GoodsOrder;
import com.local.security.entity.vo.AccountOrderVO;
import com.local.security.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface AccountService {

    void save(@NotNull Account account);

    Page<Account> findAll(Pageable pageable);

    Account get(Integer id);

    Account get(String account);

    void saveOrder(GoodsOrder goodsOrder);

    Page<AccountOrderVO> getPage(Integer account);
}
