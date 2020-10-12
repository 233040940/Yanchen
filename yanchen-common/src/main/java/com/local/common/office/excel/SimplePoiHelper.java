package com.local.common.office.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.local.common.enums.ExcelSuffix;
import com.local.common.enums.ExcelTemplateTitleOption;
import com.local.common.exception.CustomException;
import com.local.common.office.excel.entity.ExcelTemplate;
import com.local.common.utils.CustomValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yc
 * @project yanchen
 * @description 简单的excel导入导出
 * @date 2020-06-17 19:20
 */
public class SimplePoiHelper<T extends ExcelTemplate> extends PoiExcelHelper<T> {

    @Override
    Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    @Override
    Row createRow(Sheet sheet, int index) {
        return sheet.createRow(index);
    }

    @Override
    List<Row> createRows(Sheet sheet, Collection<?> excelEntities) {
        return null;
    }


    @Override
    List<Sheet> createSheets(Workbook workbook, String... sheetNames) {
        return null;
    }

    @Override
    public Collection<T> read(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix) {
        List<Field> fields = filterTemplateFields(excelTemplate);
        if (CustomValidator.checkCollectionNotEmpty(fields)) {
            boolean effective = checkExcelTemplatePropertiesEffective(fields, excelSuffix, ExcelTemplateTitleOption.NOT_CARE);
            if (!effective) {
                throw new CustomException("无效的excelTemplate");
            }
            FileInputStream inputStream = null;
            Workbook workBook = null;
            try {
                inputStream = FileUtils.openInputStream(new File(path, excelName + excelSuffix.getSuffix()));
                workBook = createWorkBook(inputStream, excelSuffix);
                Sheet sheet = workBook.getSheet(sheetName);
                if (CustomValidator.checkObjectNotNull(sheet)) {
                    List<Triple<Field, Integer, ? extends Class<?>>> triples = ExcelProvider.templateFieldOrderTypeTriples(fields);
                    final int defaultOrder = 1;
                    List<T> excelEntities = Lists.newArrayListWithCapacity(sheet.getLastRowNum());
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        T t = excelTemplate.newInstance();
                        for (Triple<Field, Integer, ? extends Class<?>> triple : triples) {
                            Cell cell = row.getCell(triple.getMiddle() - defaultOrder);
                            setExcelTemplateFieldValue(t, cell, triple.getLeft(), triple.getRight());
                        }
                        excelEntities.add(t);
                    }
                    return excelEntities;
                }
            } catch (IOException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                closeInputStream(inputStream, workBook);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean write(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix, Collection<T> excelEntities) {
        List<Field> fields = preWrite(excelTemplate, excelSuffix, excelEntities);
        Workbook workBook = createWorkBook(excelSuffix);
        Sheet sheet = createSheet(workBook, sheetName);
        createExcelTemplateTitle(sheet, findExcelTemplateTitle(fields, excelSuffix), excelSuffix);
        int rowCounter = 1;
        final int defaultOrder = 1;   //默认排序从1开始
        for (T excelEntity : excelEntities) {
            Row row = createRow(sheet, rowCounter);
            for (Field field : fields) {
                Triple<Object, Integer, ? extends Class> triple = ExcelProvider.fieldValueOrderTypeTriple(excelEntity, field);//获取属性值,排序,类型映射
                Cell cell = row.createCell(triple.getMiddle() - defaultOrder);
                setCellValue(triple.getRight(), cell, triple.getLeft());
            }
            rowCounter++;
        }
        outPut(path, excelName, excelSuffix, workBook);
        return true;
    }

    @Override
    public Map<String, Collection<? extends ExcelTemplate>> batchRead(String path, String excelName, Collection<Pair<String, Class<?  extends ExcelTemplate>>> excelEntities, ExcelSuffix excelSuffix) {
        FileInputStream inputStream = null;
        Workbook workbook = null;
        Map<String, Collection<?  extends ExcelTemplate>> sheetResult = Maps.newHashMapWithExpectedSize(excelEntities.size());
        try {
            inputStream = FileUtils.openInputStream(new File(path, excelName + excelSuffix.getSuffix()));
            workbook = createWorkBook(inputStream, excelSuffix);
            for (Pair<String, Class<?  extends ExcelTemplate>> excelEntity : excelEntities) {
                List<Field> fields = filterTemplateFields(excelEntity.getRight());
                if (!CustomValidator.checkCollectionNotEmpty(fields)) {
                    continue;
                }
                boolean effective = checkExcelTemplatePropertiesEffective(fields, excelSuffix, ExcelTemplateTitleOption.NOT_CARE);
                if (!effective) {
                    continue;
                }
                Sheet sheet = workbook.getSheet(excelEntity.getLeft());
                if (!CustomValidator.checkObjectNotNull(sheet)) {
                    continue;
                }
                List<Triple<Field, Integer, ? extends Class<?>>> triples = ExcelProvider.templateFieldOrderTypeTriples(fields);
                final int defaultOrder = 1;
                List entities = Lists.newArrayListWithCapacity(sheet.getLastRowNum());
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    Object o = excelEntity.getRight().newInstance();
                    for (Triple<Field, Integer, ? extends Class<?>> triple : triples) {
                        Cell cell = row.getCell(triple.getMiddle() - defaultOrder);
                        setExcelTemplateFieldValue(o, cell, triple.getLeft(), triple.getRight());
                    }
                    entities.add(o);
                    sheetResult.put(excelEntity.getLeft(), entities);
                }
            }
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(inputStream, workbook);
        }
        return sheetResult;
    }

    @Override
    public int batchWrite(String path, String excelName, Collection<Triple<String, Class<?  extends ExcelTemplate>, Collection<?  extends ExcelTemplate>>> excelEntities, ExcelSuffix excelSuffix) {
        CustomValidator.checkCollectionNotEmpty(excelEntities, "模版集合不能为空");
        Workbook workBook = createWorkBook(excelSuffix);
        int writeSuccessCounter = 0;
        for (Triple<String, Class<? extends ExcelTemplate>, Collection<?  extends ExcelTemplate>> excelEntity : excelEntities) {
            try {
                List<Field> fields = preWrite(excelEntity.getMiddle(), excelSuffix, excelEntity.getRight());
                Sheet sheet = workBook.createSheet(excelEntity.getLeft());
                createExcelTemplateTitle(sheet, findExcelTemplateTitle(fields, excelSuffix), excelSuffix);//创建模版title
                int rowCounter = 1;
                final int defaultOrder = 1;   //默认排序从1开始
                for (Object entity : excelEntity.getRight()) {
                    Row row = createRow(sheet, rowCounter);
                    for (Field field : fields) {
                        Triple<Object, Integer, ? extends Class> triple = ExcelProvider.fieldValueOrderTypeTriple(entity, field);//获取属性值,类型和排序映射
                        Cell cell = row.createCell(triple.getMiddle() - defaultOrder);
                        setCellValue(triple.getRight(), cell, triple.getLeft());
                    }
                    rowCounter++;
                }
                writeSuccessCounter++;
            } catch (CustomException e) {
                continue;
            }

        }
        outPut(path, excelName, excelSuffix, workBook);
        return writeSuccessCounter;
    }
}

