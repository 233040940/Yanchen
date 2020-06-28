package com.local.common.office.excel;

import com.local.common.enums.ExcelSuffix;
import com.local.common.enums.ExcelTemplateTitleOption;
import com.local.common.enums.ReaderType;
import com.local.common.exception.CustomException;
import com.local.common.office.excel.reader.MultiThreadReader;
import com.local.common.utils.CustomValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 多线程版本
 * @date 2020-06-24 16:06
 */
public class MultiThreadPoiHelper<T> extends PoiExcelHelper<T> {

    private ReaderType readerType;


    public MultiThreadPoiHelper(){

        this.readerType=ReaderType.FORKJOIN;
    }

    public MultiThreadPoiHelper(ReaderType readerType){
        this.readerType=readerType;
    }

    @Override
    Sheet createSheet(Workbook workbook, String sheetName) {
        return null;
    }

    @Override
    Row createRow(Sheet sheet, int index) {
        return null;
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
    public Collection read(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix) {

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

                return  new MultiThreadReader(readerType).startRead(sheet,excelTemplate,triples);
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
        return false;
    }

    @Override
    public Map<String, Collection<?>> batchRead(String path, String excelName, Collection<Pair<String, Class<?>>> excelEntities, ExcelSuffix excelSuffix) {
        return null;
    }

    @Override
    public int batchWrite(String path, String excelName, ExcelSuffix excelSuffix, Collection<Triple<String, Class<?>, Collection<?>>> excelEntities) {
        return 0;
    }
}
