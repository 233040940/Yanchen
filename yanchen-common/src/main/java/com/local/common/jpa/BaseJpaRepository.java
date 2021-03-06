package com.local.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yc
 * @description  抽象的JpaRepository
 * @date 2020-05-21 16:57
 */
public interface BaseJpaRepository<E,ID> extends JpaRepository<E,ID>, JpaSpecificationExecutor<E> {
}
