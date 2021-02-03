package com.local.security.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.security.entity.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @Create-By: yanchen 2021/1/10 01:07
 * @Description:
 */
public interface MenuRepository extends BaseJpaRepository<Menu,Integer> {

    List<Menu> findByIdNotIn(List<Integer> ids);

    List<Menu> findByParentID(Integer parentID);

    List<Menu> findByParentIDNot(Integer parentID);

    @Query("select m from Menu as m join Menu as m2 on m.id=m2.parentID where m2.url=:url")
    Menu findParentMenuByUrl(@Param("url") String url);

    Optional<Menu> findByUrl(String url);
}
