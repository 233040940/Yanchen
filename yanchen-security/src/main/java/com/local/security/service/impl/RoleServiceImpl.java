package com.local.security.service.impl;

import com.google.common.collect.Lists;
import com.local.common.exception.CustomException;
import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.Account;
import com.local.security.entity.Role;
import com.local.security.entity.SysUserRole;
import com.local.security.repository.RoleRepository;
import com.local.security.repository.SysUserRoleRepository;
import com.local.security.service.AccountService;
import com.local.security.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

/**
 * @Create-By: yanchen 2021/1/10 01:55
 * @Description: TODO
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final SysUserRoleRepository sysUserRoleRepository;

    private final AccountService accountService;

    public RoleServiceImpl(RoleRepository roleRepository, SysUserRoleRepository sysUserRoleRepository, AccountService accountService) {
        this.roleRepository = roleRepository;
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.accountService = accountService;
    }

    public static RoleService getInstance(){
        return ApplicationContextProvider.getBean(RoleServiceImpl.class);
    }
    @Override
    @Transactional
    public void save(String code, String note) {
        roleRepository.save(Role.builder().code(code.toUpperCase()).note(note).build());
    }

    @Override
    public Role get(Integer id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Page<Role> getPage(Integer id, String code, String note, Integer offset, Integer limit) {
        return roleRepository.findAll(new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final int queryPredicateCount=3;
                List<Predicate>  predicates= Lists.newArrayListWithExpectedSize(queryPredicateCount);
                if(Objects.nonNull(id)){
                    predicates.add(criteriaBuilder.equal(root.get("id"),id));
                }
                if(StringUtils.hasText(code)){
                    predicates.add(criteriaBuilder.equal(root.get("code"),code));
                }
                if(StringUtils.hasText(note)){
                    predicates.add(criteriaBuilder.equal(root.get("note"),note));
                }
                if(!CollectionUtils.isEmpty(predicates)){
                    query.where(predicates.toArray(new Predicate[predicates.size()]));
                }
                return query.getRestriction();
            }
        }, PageRequest.of(offset,limit, Sort.Direction.DESC,"createTimeStamp"));
    }

    @Override
    @Transactional
    public void saveUserRole(String account, Integer roleID) throws CustomException {
        Account account1 = accountService.get(account);
        if(Objects.isNull(account1)){
            throw new CustomException("账号不存在");
        }
        Role role=get(roleID);
        if(Objects.isNull(role)){
            throw new CustomException("角色不存在");
        }
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setRole(role);
        sysUserRole.setUser(account1);
       sysUserRoleRepository.save(sysUserRole);
    }

    @Override
    public List<Role> getList() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getList(List<String> roleCodes) {
        return roleRepository.findAllByCodeIn(roleCodes);
    }

    @Override
    public List<Role> getList(Integer id, String code, String note) {
        return roleRepository.findAll((Specification<Role>) (root, query, criteriaBuilder) -> {
           final int predicateCount=3;
           List<Predicate> predicates=Lists.newArrayListWithExpectedSize(predicateCount);
           if(Objects.nonNull(id)){
               predicates.add(criteriaBuilder.equal(root.get("id"),id));
           }
           if(StringUtils.hasText(code)){
               predicates.add(criteriaBuilder.equal(root.get("code"),code));
           }
           if(StringUtils.hasText(note)){
               predicates.add(criteriaBuilder.equal(root.get("note"),note));
           }
           if(!CollectionUtils.isEmpty(predicates)){
               query.where(predicates.toArray(new Predicate[predicates.size()]));
           }
            return query.getRestriction();
        });
    }
}
