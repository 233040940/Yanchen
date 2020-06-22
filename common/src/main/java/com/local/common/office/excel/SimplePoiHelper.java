package com.local.common.office.excel;

import com.google.common.collect.Lists;
import com.local.common.enums.ExcelSuffix;
import com.local.common.enums.ExcelTemplateTitleOption;
import com.local.common.exception.CustomException;
import com.local.common.utils.CustomValidator;
import com.local.common.utils.ReflectionHelper;
import org.apache.commons.io.FileUtils;
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
public class SimplePoiHelper<T, S> extends PoiExcelHelper<T, S> {

    @Override
    Sheet createSheet(Workbook workbook, String sheetName) {

        return workbook.createSheet(sheetName);
    }

    @Override
    Row createRow(Sheet sheet, int index) {

        return sheet.createRow(index);
    }

    @Override
    List<Row> createRows(Sheet sheet, Collection<T> excelEntities) {

        return null;
    }

    @Override
    List<Cell> createCells(Row row, T excelEntity) {
        return null;
    }

    @Override
    List<Sheet> createSheets(Workbook workbook, String... sheetNames) {
        return null;
    }

    @Override
    boolean batchCreateTitles(Collection<Sheet> collection, Class<T>[] excelTemplates) {
        return false;
    }

    @Override
    public Collection<T> read(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix) {

        List<Field> fields = filterTemplateField(excelTemplate);

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

                    List<Triple<Field, Integer, ? extends Class<?>>> triples = ReflectionHelper.templateFieldOrderTypeTriples(fields);

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

    /**
     * @return boolean
     * @Description 此方法跟write()做区分，支持(模版:excelTemplate)和(模版对象:excelEntities)类型不同的策略
     * @Author yc
     */

    @Override
    public boolean write2(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix, Collection<S> excelEntities) {
        return false;
    }

    @Override
    public boolean write(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix, Collection<T> excelEntities) {

        List<Field> fields = filterTemplateField(excelTemplate); //过滤模版中不包含注解的属性

        CustomValidator.checkCollectionNotEmpty(fields, String.format("无效的excelTemplate"));

        checkColumnLimit(fields.size(), excelSuffix);

        final int excelEntitySize = excelEntities.size();   //excelContent所占行

        checkRowLimit(excelEntitySize, excelSuffix);

        Workbook workBook = createWorkBook(excelSuffix);

        Sheet sheet = createSheet(workBook, sheetName);

        createExcelTemplateTitle(createRow(sheet, 0), findExcelTemplateTitle(fields, excelSuffix), excelSuffix);

        int rowCounter = 1;

        final int defaultOrder = 1;   //默认排序从1开始

        for (T excelEntity : excelEntities) {

            Row row = createRow(sheet, rowCounter);

            for (Field field : fields) {

                Triple<Object, Integer, ? extends Class> triple = ReflectionHelper.fieldValueOrderTypeTriple(excelEntity, field);//获取属性值,类型和排序映射

                Cell cell = row.createCell(triple.getMiddle() - defaultOrder);

                setCellValue(triple.getRight(), cell, triple.getLeft());
            }
            rowCounter++;
        }

        outPut(path, excelName, excelSuffix, workBook);
        return true;
    }


    @Override
    public Map<String, Collection<?>> batchRead(String path, String excelName, Collection<String> sheetNames, Collection<Class<?>> excelEntities, ExcelSuffix excelSuffix) {
        return null;
    }

    @Override
    public int batchWrite(String path, String excelName, Collection<String> sheetNames, Collection<Class<?>> excelTemplates, ExcelSuffix excelSuffix, Collection<? extends Collection> excelEntities) {

        final int sheetSize = sheetNames.size();

        final int templatesSize = excelTemplates.size();

        if (sheetSize != templatesSize) {

            throw new CustomException("excel工作薄长度必须和模版对象长度一致");
        }

        ArrayList<String> sheetList = Lists.newArrayList(sheetNames);

        ArrayList<Class<?>> excelTemplateClassList = Lists.newArrayList(excelTemplates);

        ArrayList<? extends Collection> excelEntityList = Lists.newArrayList(excelEntities);

        for (int i = 0; i < sheetSize; i++) {

        }

        return 0;
    }


}

