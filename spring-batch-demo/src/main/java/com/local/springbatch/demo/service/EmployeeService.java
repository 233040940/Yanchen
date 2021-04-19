package com.local.springbatch.demo.service;

import com.local.springbatch.demo.entity.jpa.Employee;

import java.util.List;

/**
 * @Create-By: yanchen 2021/4/3 07:24
 * @Description: TODO
 */
public interface EmployeeService {

    Employee save(String name,Integer age);

    int batchSave(List<Employee> employees);
}
