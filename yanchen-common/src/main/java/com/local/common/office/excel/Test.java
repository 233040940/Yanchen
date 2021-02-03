package com.local.common.office.excel;

import com.google.common.collect.Lists;
import com.local.common.office.excel.entity.Dept;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author yc
 * @project yanchen
 * @description TODO
 * @date 2020-06-19 20:54
 */
public class Test {


    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {
        ExcelProvider<Dept> excelProvider = new SimplePoiProvider();
        String path = "/Users/yanchen/IdeaProjects/yanchen/common/src/main/java/com/local/common/office/excel";
        String excelName = "部门工资表";
        String sheetName = "IT部";
        List<Dept> depts = Lists.newArrayList();
       for (int i = 0; i <13333; i++) {
           depts.add(new Dept("yanchen","四川成都",10086,10000.2,new Date()));
       }
         depts.add(new Dept("李四","西安",10010,7000,new Date()));
       boolean write = excelProvider.write(path, excelName, sheetName, Dept.class, ExcelProvider.ExcelSuffix.XLSX, depts);
       System.out.println(write);
//        Collection<Dept> read = excelHelper.read(path, excelName, sheetName, Dept.class, ExcelSuffix.XLSX);
//        int size = read.size();
//        System.out.println(size);
//        List list = Lists.newArrayList(read);
//        System.out.println(list.get(size - 1));
//
//        ArrayList<Object> objects = Lists.newArrayList();
//       StopWatch stopWatch=new StopWatch("counter");
//        stopWatch.start("单线程读");
//        Collection read = helper.read(path, "部门工资", "IT部", Dept.class, ExcelSuffix.XLSX);
//        stopWatch.stop();
//        ExcelHelper<Dept> multiHelper=MultiThreadPoiHelper.of(WorkerSource.FUTURE);
//        stopWatch.start("多线程读");
//        Collection read = multiHelper.read(path, "部门工资", "IT部", Dept.class, ExcelSuffix.XLSX);
//        stopWatch.stop();
//        for (int i = 0; i <150000 ; i++) {
//            Dept dept1 = new Dept("jack", "四川成都", 188888888, 200000, new Date(), 1000L, 24.0f,true);
//            objects.add(dept1);
//        }
//        helper.write(path,"部门工资","IT部",Dept.class,ExcelSuffix.UP_XLSX,objects);
//        System.out.println(stopWatch.prettyPrint());
//        System.out.println(stopWatch.getTotalTimeSeconds());
//        System.out.println(read.size());

    }

}
