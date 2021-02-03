package com.local.security.service.impl;

import com.google.common.collect.Lists;
import com.local.common.exception.CustomException;
import com.local.common.utils.ApplicationContextProvider;
import com.local.security.entity.Menu;
import com.local.security.enums.MenuSource;
import com.local.security.enums.OperationSource;
import com.local.security.repository.MenuRepository;
import com.local.security.service.MenuService;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Create-By: yanchen 2021/1/10 06:17
 * @Description: TODO
 */
@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private static final int ROOT_NODE_MENU_ID=-1;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public static MenuService getInstance(){
        return ApplicationContextProvider.getBean(MenuServiceImpl.class);
    }
    @Override
    @Transactional
    public void save(String url, String name, String note,Integer parentID, OperationSource operationSource,MenuSource menuSource) throws CustomException {
        if(menuSource == MenuSource.NAVIGATE){    //表示导航菜单
            if(parentID != ROOT_NODE_MENU_ID){
                throw new CustomException("导航菜单父级菜单ID只能为-1");
            }
        }
     Menu menu=new Menu();
     menu.setUrl(url);
     menu.setName(name);
     menu.setNote(note);
     menu.setParentID(parentID);
     menu.setOperationSource(operationSource);
     menu.setMenuSource(menuSource);
     menuRepository.save(menu);
    }

    @Override
    public Menu get(Integer id) {
        return menuRepository.getOne(id);
    }

    @Override
    public boolean exist(Integer id) {
        return menuRepository.existsById(id);
    }

    @Override
    public Page<Menu> getPage(String url, String name, Integer parentID, OperationSource operationSource,MenuSource menuSource, Integer offset, Integer limit) {
        return menuRepository.findAll(new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                final int queryPredicateCount=4;
                List<Predicate> predicates= Lists.newArrayListWithExpectedSize(queryPredicateCount);
                if(StringUtils.hasText(url)){
                    predicates.add(criteriaBuilder.equal(root.get("url"),url));
                }
                if(StringUtils.hasText(name)){
                    predicates.add(criteriaBuilder.equal(root.get("name"),name));
                }
                if(Objects.nonNull(parentID)){
                    predicates.add(criteriaBuilder.equal(root.get("parentID"),parentID));
                }
                if(Objects.nonNull(operationSource)){
                    predicates.add(criteriaBuilder.equal(root.get("operationSource"),operationSource));
                }
                if(Objects.nonNull(menuSource)){
                    predicates.add(criteriaBuilder.equal(root.get("menuSource"),menuSource));
                }
                if(!CollectionUtils.isEmpty(predicates)){
                    query.where(predicates.toArray(new Predicate[predicates.size()]));
                }
                return query.getRestriction();
            }
        }, PageRequest.of(offset,limit, Sort.Direction.DESC,"createTimeStamp"));
    }

    @Override
    public List<Menu> getList() {
        return menuRepository.findAll();
    }

    @Override
    public List<Menu> getList(List<Integer> ids) {
        return menuRepository.findAllById(ids);
    }

    @Override
    public List<Menu> getListNot(List<Integer> ids) {
        return menuRepository.findByIdNotIn(ids);
    }

    @Override
    public List<Menu> getList(MenuSource menuSource) {
        if(menuSource==MenuSource.NAVIGATE){
           return  menuRepository.findByParentID(ROOT_NODE_MENU_ID);
        }
        return menuRepository.findByParentIDNot(ROOT_NODE_MENU_ID);
    }

    @Override
    public Menu get(Integer id, String url, String name) {
        return menuRepository.findOne((Specification<Menu>) (root, query, criteriaBuilder) -> {
             final int predicateCount=3;
            List<Predicate> predicates=Lists.newArrayListWithExpectedSize(predicateCount);
            if(Objects.nonNull(id)){
                predicates.add(criteriaBuilder.equal(root.get("id"),id));
            }
            if(StringUtils.hasText(url)){
                predicates.add(criteriaBuilder.equal(root.get("url"),url));
            }
            if(StringUtils.hasText(name)){
                predicates.add(criteriaBuilder.equal(root.get("name"),name));
            }
            if(!CollectionUtils.isEmpty(predicates)){
                query.where(predicates.toArray(new Predicate[predicates.size()]));
            }
            return query.getRestriction();
        }).orElse(null);
    }

    @Override
    public Menu getParentMenu(String url) {
        return menuRepository.findParentMenuByUrl(url);
    }

    @Override
    public List<Menu> getParentMenus(String url) {
        if(StringUtils.hasText(url)){
            Menu menu = this.findByUrl(url);
            if(Objects.nonNull(menu)){
               return findAllPrentMenus(Lists.newArrayList(), menu.getParentID());
            }
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Menu findByUrl(String url) {
        return menuRepository.findByUrl(url).orElse(null);
    }

    /**
     * @create-by: yanchen 2021/1/20 01:20
     * @description: 根据菜单parentID递归查询所有上级菜单
     * @param menus
     * @param parentID
     * @return: java.util.List<com.local.security.entity.Menu>
     */
    private List<Menu> findAllPrentMenus(List<Menu> menus,Integer parentID){
         if(parentID != ROOT_NODE_MENU_ID){
             Menu menu = this.get(parentID);
             if(Objects.nonNull(menu)){
                 menus.add(menu);
                 findAllPrentMenus(menus,menu.getParentID());
             }
         }
         return menus;
    }

    @Override
    public Menu edit(Menu menu) {
        return menuRepository.save(menu);
    }
}
