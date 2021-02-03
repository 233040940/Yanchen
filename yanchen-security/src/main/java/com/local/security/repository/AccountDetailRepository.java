package com.local.security.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.security.entity.AccountDetail;

import java.util.Optional;

/**
 * @Create-By: yanchen 2021/1/10 01:05
 * @Description:
 */
public interface AccountDetailRepository extends BaseJpaRepository<AccountDetail,Integer> {

    Optional<AccountDetail> findByAccountID(Integer accountID);
}
