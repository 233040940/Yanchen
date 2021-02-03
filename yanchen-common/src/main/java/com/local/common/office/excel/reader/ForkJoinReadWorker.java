package com.local.common.office.excel.reader;

import com.local.common.office.excel.PoiExcelProvider;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author yc
 * @project yanchen
 * @description TODO
 * @date 2020-06-24 16:08
 */
public class ForkJoinReadWorker extends RecursiveTask<Collection> {

    private static final int READ_MAX_ROW = 10000;

    private Sheet sheet;

    private Class<?> excelTemplate;

    private List<Triple<Field, Integer, ? extends Class<?>>> triples;

    private int start;

    private int end;

    public ForkJoinReadWorker(int start, int end, Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) {
        this.start = start;
        this.end = end;
        this.sheet = sheet;
        this.excelTemplate = excelTemplate;
        this.triples = triples;
    }

    @Override
    protected Collection compute() {
        List result = new ArrayList(READ_MAX_ROW);
        try {
            boolean canCompute = (end - start) <= READ_MAX_ROW;
            if (canCompute) {
                for (int i = start; i < end; i++) {
                    Row row = sheet.getRow(i);
                    Object t = excelTemplate.newInstance();
                    for (Triple<Field, Integer, ? extends Class<?>> triple : triples) {
                        final int defaultOrder = 1;
                        Cell cell = row.getCell(triple.getMiddle() - defaultOrder);
                        PoiExcelProvider.setExcelTemplateFieldValue(t, cell, triple.getLeft(), triple.getRight());
                    }
                    result.add(t);
                }
            } else {
                int middle = (end + start) / 2;
                ForkJoinReadWorker first = new ForkJoinReadWorker(start, middle, sheet, excelTemplate, triples);
                ForkJoinReadWorker seconds = new ForkJoinReadWorker(middle, end, sheet, excelTemplate, triples);
                first.fork();
                seconds.fork();
                Collection join = first.join();
                Collection join1 = seconds.join();
                result.addAll(join);
                result.addAll(join1);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
