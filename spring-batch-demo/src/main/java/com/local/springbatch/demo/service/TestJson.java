package com.local.springbatch.demo.service;

import com.local.springbatch.demo.entity.jpa.Employee;

import java.util.List;

/**
 * @Create-By: yanchen 2021/4/3 09:39
 * @Description: TODO
 */
public class TestJson {

    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
