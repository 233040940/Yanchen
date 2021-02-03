package com.local.security.service.impl;

import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.AccountDetail;
import com.local.security.repository.AccountDetailRepository;
import com.local.security.service.AccountDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Create-By: yanchen 2021/1/12 11:32
 * @Description:
 */
@Service
public class AccountDetailServiceImpl implements AccountDetailService {
    private AccountDetailRepository accountDetailRepository;

    public AccountDetailServiceImpl(AccountDetailRepository accountDetailRepository) {
        this.accountDetailRepository = accountDetailRepository;
    }

    public static AccountDetailService getInstance(){
        return ApplicationContextProvider.getBean(AccountDetailServiceImpl.class);
    }

    @Override
    @Transactional
    public void save(Integer accountID, boolean banned) {
        accountDetailRepository.save(new AccountDetail(accountID,banned));
    }

    @Override
    public boolean banned(Integer accountID) {
        Optional<AccountDetail> accountDetail = accountDetailRepository.findByAccountID(accountID);
        if(accountDetail.isPresent()){
            return accountDetail.get().isBanned();
        }
        return false;
    }
}
