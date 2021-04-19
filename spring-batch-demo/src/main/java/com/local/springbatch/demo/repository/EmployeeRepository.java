package com.local.springbatch.demo.repository;

import com.local.common.jpa.BaseJpaRepository;
import com.local.springbatch.demo.entity.jpa.Employee;

/**
 * @Create-By: yanchen 2021/4/3 06:49
 * @Description: TODO
 */
public interface EmployeeRepository extends BaseJpaRepository<Employee,Integer> {
}
