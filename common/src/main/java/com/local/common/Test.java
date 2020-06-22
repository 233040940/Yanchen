package com.local.common;

import com.local.common.office.excel.PoiExcelHelper;
import com.local.common.office.excel.SimplePoiHelper;

/**
 * @author yc
 * @project yanchen
 * @description TODO
 * @date 2020-06-19 20:54
 */
public class Test {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {

        PoiExcelHelper helper=new SimplePoiHelper();

//
//        String path="/Users/yc/IdeaProjects/yanchen/common/src/main/java/com/local/common/excel";
//
//        ArrayList<Object> objects = Lists.newArrayList();
//
//        for(int i=0;i<20;i++){
//
//            Dept dept1 = new Dept("jack", "四川成都", 188888888, 200000, new Date(), 1000L, 24.0f,true);
//
//            objects.add(dept1);
//        }
//
//        boolean write = helper.write(path, "部门工资", "IT部", Dept.class, ExcelSuffix.XLSX, objects);
//
//        System.out.println(write);
//
//
//        Collection<Dept> read = helper.read(path, "部门工资", "IT部", Dept.class, ExcelSuffix.XLSX);
//
//        read.forEach(System.out::println);

    }
}
