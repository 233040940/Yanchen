package com.local.common.office.excel.reader;

import com.google.common.collect.Lists;
import com.local.common.office.excel.PoiExcelHelper;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO
 * @date 2020-06-25 19:48
 */
public class FutureReadWorker implements Callable<Collection> {

    private Sheet sheet;

    private Class<?> excelTemplate;

    private List<Triple<Field, Integer, ? extends Class<?>>> triples;

    private int start;

    private int end;

    public FutureReadWorker(int start, int end, Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) {
        this.start = start;
        this.end = end;
        this.sheet = sheet;
        this.excelTemplate = excelTemplate;
        this.triples = triples;
    }
    @Override
    public Collection call() throws Exception {

        List result= Lists.newArrayListWithCapacity(end);

        for (int i = start; i <end; i++) {

            Row row = sheet.getRow(i);

            Object t = excelTemplate.newInstance();

            for (Triple<Field, Integer, ? extends Class<?>> triple : triples) {

                final int defaultOrder = 1;

                Cell cell = row.getCell(triple.getMiddle() - defaultOrder);

                PoiExcelHelper.setExcelTemplateFieldValue(t, cell, triple.getLeft(), triple.getRight());
            }

            result.add(t);
        }
        return result;
    }
}
