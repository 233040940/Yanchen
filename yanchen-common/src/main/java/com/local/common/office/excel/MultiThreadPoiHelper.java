package com.local.common.office.excel;

import com.local.common.enums.ExcelSuffix;
import com.local.common.enums.ExcelTemplateTitleOption;
import com.local.common.enums.WorkerSource;
import com.local.common.exception.CustomException;
import com.local.common.office.excel.entity.ExcelTemplate;
import com.local.common.office.excel.reader.MultiThreadReader;
import com.local.common.office.excel.writer.MultiThreadWriter;
import com.local.common.utils.CustomValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 多线程读写excel
 * @date 2020-06-24 16:06
 */

@ThreadSafe
public class MultiThreadPoiHelper<T  extends ExcelTemplate> extends PoiExcelHelper<T> {

    private final WorkerSource workerSource;

    private MultiThreadPoiHelper(WorkerSource workerSource) {
        this.workerSource = workerSource;
    }

    public static <T  extends ExcelTemplate> MultiThreadPoiHelper of(WorkerSource workerSource){
        return new MultiThreadPoiHelper<T>(workerSource);
    }
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
                    if (CustomValidator.checkCollectionNotEmpty(triples)) {
                        return MultiThreadReader.of(workerSource).startRead(sheet, excelTemplate, triples);
                    }
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
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
        try {
            boolean flag  = MultiThreadWriter.of(workerSource).startWrite(sheet, fields, excelEntities);
            if (flag) {
                outPut(path, excelName, excelSuffix, workBook);
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Collection<? extends ExcelTemplate>> batchRead(String path, String excelName, Collection<Pair<String, Class<?  extends ExcelTemplate>>> excelEntities, ExcelSuffix excelSuffix) {
        return null;
    }

    @Override
    public int batchWrite(String path, String excelName,  Collection<Triple<String, Class<?  extends ExcelTemplate>, Collection<?  extends ExcelTemplate>>> excelEntities,ExcelSuffix excelSuffix) {
        return 0;
    }
}
