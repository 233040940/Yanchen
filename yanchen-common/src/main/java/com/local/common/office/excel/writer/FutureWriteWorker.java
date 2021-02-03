package com.local.common.office.excel.writer;

import com.google.common.collect.Lists;
import com.local.common.office.excel.ExcelHelper;
import com.local.common.office.excel.PoiExcelProvider;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-25 18:36
 */
public class FutureWriteWorker implements Callable<Boolean> {

    private  int start;

    private  int end;

    private  Sheet sheet;

    private Collection  excelEntities;

    private List<Field> fields;

    public FutureWriteWorker(int start,int end,Sheet sheet,Collection  excelEntities,List<Field> fields){
        this.start=start;
        this.end=end;
        this.sheet=sheet;
        this.excelEntities=excelEntities;
        this.fields=fields;
    }
    @Override
    public Boolean call() {
        final int defaultOrder=1;
        ArrayList  entities = Lists.newArrayList(excelEntities);
        try {
            int counter=0;
            for (int i = start; i <end; i++) {
                Row row;
                synchronized (sheet){    //解决并发冲突
                    row =sheet.createRow(i);
                }
                for (Field field : fields) {
                        Triple<Object, Integer, ? extends Class> triple = ExcelHelper.fieldValueOrderTypeTriple(entities.get(counter), field);//获取属性值,类型和排序映射
                        Cell cell = row.createCell(triple.getMiddle() - defaultOrder);
                        PoiExcelProvider.setCellValue(triple.getRight(),cell,triple.getLeft());
                    }
                    counter++;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
