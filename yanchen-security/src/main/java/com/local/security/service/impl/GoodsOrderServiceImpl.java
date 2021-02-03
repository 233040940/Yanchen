package com.local.security.service.impl;

import com.local.security.entity.GoodsOrder;
import com.local.security.repository.OrderRepository;
import com.local.security.service.AccountService;
import com.local.security.entity.Account;
import com.local.security.service.GoodsOrderService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;

/**
 * @Create-By: yanchen 2020/12/26 09:11
 * @Description: TODO
 */
@Service
public class GoodsOrderServiceImpl implements GoodsOrderService {

    private final AccountService accountService;
    private final OrderRepository orderRepository;

    public GoodsOrderServiceImpl(AccountService accountService, OrderRepository orderRepository) {
        this.accountService = accountService;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void save(Integer account, String goods) {
        Account account1 = accountService.get(account);
        GoodsOrder order=new GoodsOrder();
        order.setAccount(account1);
        order.setGoods(goods);
        orderRepository.save(order);
    }

    @Override
    public Page<GoodsOrder> getPage(Integer account, String goods,int offset, int limit) {
        return orderRepository.findAll((Specification<GoodsOrder>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates= Lists.newArrayList();
            if(Objects.nonNull(account)){
                Join<Account,GoodsOrder> join=root.join("account", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(join.get("id"),account));
            }
            if(StringUtils.hasText(goods)){
                predicates.add(criteriaBuilder.equal(root.get("goods"),goods));
            }
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return query.getRestriction();
        }, PageRequest.of(offset,limit));
    }
}
