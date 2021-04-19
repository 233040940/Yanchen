package com.local.springbatch.demo.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.local.common.entity.ResultResponse;
import com.local.common.enums.ResponseStatus;
import com.local.common.utils.FileHelper;
import com.local.common.utils.JsonHelper;
import com.local.common.utils.RandomHelper;
import com.local.springbatch.demo.entity.jpa.Employee;
import com.local.springbatch.demo.service.EmployeeService;
import com.local.springbatch.demo.service.TestJson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @Create-By: yanchen 2021/4/9 00:48
 * @Description: TODO
 */
@RestController
public class BatchController {
    @Autowired
    private EmployeeService employeeService;

    private Map<Pair<Integer,String>, Pair<Integer,String>> valueMap= Maps.newConcurrentMap();
    @GetMapping(value = "/hello")
    public String hello(){

        return "hello world";
    }
    @GetMapping(value = "/save")
    public boolean startRandomSave(int count){
        final List<Employee> employees = Lists.newArrayListWithExpectedSize(count);
        for (int i = 0; i < count; i++) {
            String name= RandomHelper.oneString(6,true,true);
            Integer age=RandomHelper.oneInt(1,100);
            employees.add(Employee.builder().name(name).age(age).build());
        }
        int i = employeeService.batchSave(employees);
        return i==count;
    }

    @GetMapping(value = "/transfer")
    public ResultResponse startTransferData() throws IOException {
        try (InputStream inputStream = FileHelper.openInputStreamWithRelativePath("data.json")) {
            TestJson testJson = JsonHelper.deSerializable(inputStream, TestJson.class);
            List<Employee> employees = testJson.getEmployees();
            if(CollectionUtils.isNotEmpty(employees)){
                employees.forEach((e)->{
                    try {
                        Employee save = employeeService.save(e.getName(), e.getAge());
                        valueMap.put(Pair.of(e.getId(),e.getName()),Pair.of(save.getId(),save.getName()));
                    } catch (Exception exception) {
                        if(exception instanceof DataIntegrityViolationException){
                            String newName=e.getName()+"120";
                            Employee save = employeeService.save(newName, e.getAge());
                            valueMap.put(Pair.of(e.getId(),e.getName()),Pair.of(save.getId(),newName));
                        }
                    }
                });
            }
        }
        return ResultResponse.builder().code(200).status(ResponseStatus.SUCCESS).data(valueMap).build();
    }
}
