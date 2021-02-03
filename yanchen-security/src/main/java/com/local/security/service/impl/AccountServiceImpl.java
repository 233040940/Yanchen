package com.local.security.service.impl;

import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.Account;
import com.local.security.entity.GoodsOrder;
import com.local.security.entity.vo.AccountOrderVO;
import com.local.security.event.AccountEvent;
import com.local.security.repository.AccountRepository;
import com.local.security.repository.OrderRepository;
import com.local.security.service.AccountService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final OrderRepository orderRepository;

    private final ApplicationEventPublisher publisher;

    public AccountServiceImpl(AccountRepository accountRepository,OrderRepository orderRepository, ApplicationEventPublisher publisher) {
        this.accountRepository = accountRepository;
        this.publisher = publisher;
        this.orderRepository=orderRepository;
    }

    public  static AccountService getInstance(){
        return ApplicationContextProvider.getBean(AccountServiceImpl.class);
    }
    @Override
    @Transactional
    public void save(@NotNull Account account) {
        accountRepository.save(account);
        publisher.publishEvent(new AccountEvent(account));
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account get(Integer id) {
        return accountRepository.findById(id).get();
    }

    @Override
    public Account get(String account) {
        return accountRepository.findByAccount(account);
    }

    @Override
    public void saveOrder(GoodsOrder goodsOrder) {
        orderRepository.save(goodsOrder);
    }

    @Override
    public Page<AccountOrderVO> getPage(Integer account) {
        return null;
    }

}
