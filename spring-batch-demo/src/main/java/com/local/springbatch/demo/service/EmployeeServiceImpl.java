package com.local.springbatch.demo.service;

import com.local.springbatch.demo.entity.jpa.Employee;
import com.local.springbatch.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Create-By: yanchen 2021/4/3 06:49
 * @Description: TODO
 */
@Service
public class EmployeeServiceImpl  implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public Employee save(String name,Integer age){
       return employeeRepository.save(Employee.builder().name(name).age(age).build());
    }
    @Transactional
    @Override
    public int batchSave(List<Employee> employees){
      return  employeeRepository.saveAll(employees).size();
    }


}
