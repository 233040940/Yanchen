package com.local.common;

import com.google.common.collect.*;
import com.local.common.entity.Dept;
import com.local.common.enums.ExcelSuffix;
import com.local.common.office.excel.PoiExcelHelper;
import com.local.common.office.excel.SimplePoiHelper;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yc
 * @project yanchen
 * @description TODO
 * @date 2020-06-19 20:54
 */
public class Test {

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {

//
//        PoiExcelHelper helper=new SimplePoiHelper();

//
//        String path="/Users/yc/IdeaProjects/yanchen/common/src/main/java/com/local/common/office/excel";
//
//        ArrayList<Object> objects = Lists.newArrayList();
//
//       StopWatch stopWatch=new StopWatch("counter");
//        stopWatch.start("单线程读");
//
//        Collection read = helper.read(path, "部门工资", "IT部", Dept.class, ExcelSuffix.XLSX);
//
//        stopWatch.stop();

//        PoiExcelHelper multiHelper=new MultiThreadPoiHelper(ReaderType.FUTURE);
//
//        stopWatch.start("多线程读");
//
//        Collection read = multiHelper.read(path, "部门工资", "IT部", Dept.class, ExcelSuffix.XLSX);
//
//        stopWatch.stop();
//        for (int i = 0; i <150000 ; i++) {
//
//            Dept dept1 = new Dept("jack", "四川成都", 188888888, 200000, new Date(), 1000L, 24.0f,true);
//
//            objects.add(dept1);
//        }
//        helper.write(path,"部门工资","IT部",Dept.class,ExcelSuffix.UP_XLSX,objects);



//        System.out.println(stopWatch.prettyPrint());
//
//        System.out.println(stopWatch.getTotalTimeSeconds());
//
//        System.out.println(read.size());

    }
}
