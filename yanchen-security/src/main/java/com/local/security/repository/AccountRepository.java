package com.local.security.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.security.entity.Account;

public interface AccountRepository extends BaseJpaRepository<Account,Integer> {

    Account findByAccount(String account);
}
