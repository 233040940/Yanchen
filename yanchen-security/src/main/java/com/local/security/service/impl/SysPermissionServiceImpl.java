package com.local.security.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.local.common.exception.CustomException;
import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.Menu;
import com.local.security.entity.Role;
import com.local.security.entity.SysPermission;
import com.local.security.enums.OperationSource;
import com.local.security.repository.SysPermissionRepository;
import com.local.security.service.MenuService;
import com.local.security.service.RoleService;
import com.local.security.service.SysPermissionService;
import com.local.security.service.SysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @Create-By: yanchen 2021/1/12 01:23
 * @Description:
 */
@Service
@Slf4j
public class SysPermissionServiceImpl implements SysPermissionService {
    private SysPermissionRepository sysPermissionRepository;
    private RoleService roleService;
    private MenuService menuService;

    public SysPermissionServiceImpl(SysPermissionRepository sysPermissionRepository, SysUserRoleService sysUserRoleService, RoleService roleService, MenuService menuService) {
        this.sysPermissionRepository = sysPermissionRepository;
        this.roleService = roleService;
        this.menuService = menuService;
    }

    public static SysPermissionService getInstance(){
        return ApplicationContextProvider.getBean(SysPermissionServiceImpl.class);
    }

    @Override
    public SysPermission get(Integer id) {
        return sysPermissionRepository.getOne(id);
    }

    @Override
    @Transactional
    public void save(Integer roleID, List<Integer> menuIDs) {
        Role role = roleService.get(roleID);
        if (Objects.isNull(role)) {
            throw new CustomException("角色信息不存在");
        }
        List<Menu> menus = menuService.getList(menuIDs);
        if (CollectionUtils.isEmpty(menus)) {
            throw new CustomException("菜单不存在");
        }
        if (menus.size() != menuIDs.size()) {
            throw new CustomException("请求涉嫌非法伪造");
        }
        if (sysPermissionExist(role)) {
            throw new CustomException("已经存在该角色权限配置信息，需要修改请前往编辑权限信息");
        }
        SysPermission permission = new SysPermission();
        permission.setMenu(Sets.newHashSet(menus));
        permission.setRole(role);
        sysPermissionRepository.save(permission);
    }

    @Override
    public Page<SysPermission> getPage(Integer roleID, Integer offset, Integer limit) {
        return sysPermissionRepository.findAll(new Specification<SysPermission>() {
            @Override
            public Predicate toPredicate(Root<SysPermission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final int queryPredicateCount = 1;
                List<Predicate> predicates = Lists.newArrayListWithExpectedSize(queryPredicateCount);
                if (Objects.nonNull(roleID)) {
                    Role role = roleService.get(roleID);
                    if (Objects.isNull(role)) {
                        throw new CustomException("不存在该角色信息");
                    }
                    predicates.add(criteriaBuilder.equal(root.get("role"), role));
                }
                if (CollectionUtils.isNotEmpty(predicates)) {
                    query.where(predicates.toArray(new Predicate[predicates.size()]));
                }
                return query.getRestriction();
            }
        }, PageRequest.of(offset, limit, Sort.Direction.DESC, "createTimeStamp"));
    }

    @Override
    public boolean sysPermissionExist(Role role) {
        Optional<SysPermission> byRole = sysPermissionRepository.findByRole(role);
        return byRole.isPresent();
    }

    @Override
    @Transactional
    public void editPermission(Integer permissionID, Integer roleID, List<Integer> menuIDs, OperationSource source) {
        List<Menu> menus = menuService.getList(menuIDs);
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        Role role = roleService.get(roleID);
        if (Objects.isNull(role)) {
            return;
        }
        SysPermission sysPermission = this.get(permissionID);
        if (Objects.isNull(sysPermission)) {
            return;
        }
        Set<Menu> oldPermissionMenus = sysPermission.getMenu();
        Set<Menu> newPermissionMenus = Sets.newHashSet();
        switch (source) {
            case INSERT:       //新增菜单权限
                Sets.SetView<Menu> newMenus = Sets.union(oldPermissionMenus, Sets.newHashSet(menus));//求并集
                newMenus.copyInto(newPermissionMenus);
                break;
            case DELETE:      //删除菜单权限
                Sets.SetView<Menu> nowMenus = Sets.difference(oldPermissionMenus, Sets.newHashSet(menus));  //求差集
                nowMenus.copyInto(newPermissionMenus);
                break;
        }
        sysPermission.setId(permissionID);
        sysPermission.setRole(role);
        sysPermission.setMenu(newPermissionMenus);
        sysPermissionRepository.save(sysPermission);
    }

    @Override
    public List<SysPermission> findPermissions(List<Role> roles) {
        return sysPermissionRepository.findAllByRoleIn(roles);
    }

    @Override
    public SysPermission findPermission(Role role) {
        return sysPermissionRepository.findByRole(role).orElse(null);
    }
}
