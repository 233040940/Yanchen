package com.local.common.office.excel;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.local.common.SystemConstant;
import com.local.common.annotation.ExcelField;
import com.local.common.enums.ExcelSuffix;
import com.local.common.enums.ExcelTemplateTitleOption;
import com.local.common.exception.CustomException;
import com.local.common.utils.CustomValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 使用apache poi读写excel基于自定义注解@ExcelField   TODO ==》校验excel模版策略待抽象,暂时通过枚举ExcelTemplateTitleOption进行区分
 * @date 2020-06-16 15:03
 */
public abstract class PoiExcelHelper<T>  implements ExcelHelper<T> {

    protected static final ImmutableSet effectiveFieldType;     //合法的模版属性类型

    static {

        effectiveFieldType = ImmutableSet.of(int.class, Integer.class, long.class, Long.class, short.class,
                Short.class, byte.class, Byte.class, double.class, Double.class,
                float.class, Float.class, boolean.class, Boolean.class, Date.class, String.class);
    }

    protected final Workbook createWorkBook(ExcelSuffix excelSuffix) {

        Workbook workbook;
        switch (excelSuffix) {
            case XLSX:
                workbook = new XSSFWorkbook();
                break;
            case XLS:
                workbook = new HSSFWorkbook();
                break;
            default:
                workbook = new SXSSFWorkbook();
        }
        return workbook;
    }

    protected final Workbook createWorkBook(InputStream inputStream, ExcelSuffix excelSuffix) throws IOException {

        Workbook workbook;
        switch (excelSuffix) {
            case XLS:
                workbook = new HSSFWorkbook(inputStream);
                break;
            default:
                workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    abstract Sheet createSheet(Workbook workbook, String sheetName);

    abstract Row createRow(Sheet sheet, int index);

    abstract List<Row> createRows(Sheet sheet, Collection<?> excelEntities);

    abstract List<Sheet> createSheets(Workbook workbook, String... sheetNames);

    //过滤模版属性
    protected List<Field> filterTemplateFields(Class<?> excelTemple) {

        return ExcelProvider.filterTemplateFields(excelTemple,ExcelField.class);
    }


    //查找模版title
    protected final List<Pair<String, Integer>> findExcelTemplateTitle(List<Field> fields, ExcelSuffix suffix) {

        if (CustomValidator.checkCollectionNotEmpty(fields)) {

            boolean effective = checkExcelTemplatePropertiesEffective(fields, suffix, ExcelTemplateTitleOption.CARE);

            if (!effective) {
                throw new CustomException("校验excel模版title失败");
            }
            List<Pair<String, Integer>> collect = fields.stream().map((field) -> {

                ExcelField excelField = field.getAnnotation(ExcelField.class);

                String title = excelField.title();                                  //模版标题名

                Integer order = excelField.order();                                 //属性顺序

                Pair<String, Integer> templatePropertyPair = Pair.of(title, order);     //excel模版属性映射

                return templatePropertyPair;
            }).collect(Collectors.toList());

            return collect;
        }
       throw new CustomException("excel模版属性为空");
    }

    protected List<Pair<String, Integer>> preCreateExcelTemplateTitle(Class<?> excelTemplate,ExcelSuffix excelSuffix){

        return findExcelTemplateTitle(filterTemplateFields(excelTemplate), excelSuffix);
    }
    //创建excel的title
    protected final Sheet createExcelTemplateTitle(Sheet sheet,Class<?> excelTemplate, ExcelSuffix excelSuffix) {

        List<Pair<String, Integer>> excelTemplateProperties = preCreateExcelTemplateTitle(excelTemplate, excelSuffix);

        return createExcelTemplateTitle(sheet,excelTemplateProperties,excelSuffix);
    }

    protected final Sheet createExcelTemplateTitle(Sheet sheet,List<Pair<String,Integer>> excelTemplateProperties,ExcelSuffix excelSuffix){

        checkColumnLimit(excelTemplateProperties.size(), excelSuffix);

        Collections.sort(excelTemplateProperties, Comparator.comparing(Pair::getRight));      //将模版属性按照给定的order进行排序

        final int defaultOrder = 1;   //默认模版顺序从1开始

        Row row = createRow(sheet, 0);

        for (Pair<String, Integer> pair : excelTemplateProperties) {

            Cell cell = row.createCell(pair.getRight() - defaultOrder);
            cell.setCellValue(pair.getLeft());
        }
        return sheet;
    }

    //生成excel表头并输出
    public final boolean outPutExcelTemplate(String path, String excelName, String sheetName, Class<?> excelTemplate, ExcelSuffix excelSuffix) {

        Workbook workBook = createWorkBook(excelSuffix);     //创建工作薄

        Sheet sheet = createSheet(workBook, sheetName);      //创建工作表

        createExcelTemplateTitle(sheet,excelTemplate, excelSuffix);            //创建模版title

        return outPut(path, excelName, excelSuffix, workBook);
    }

    //批量生成excel表头并输出
    public final int batchOutPutExcelTemplate(String path, String excelName, Collection<Pair<String, Class<?>>> excelTemplates, ExcelSuffix excelSuffix) {

        CustomValidator.checkCollectionNotEmpty(excelTemplates, "模版集合不能为空");

        Workbook workBook = createWorkBook(excelSuffix);     //创建工作薄

        int writeSuccessCounter = 0;

        for (Pair<String, Class<?>> excelTemplate : excelTemplates) {

            try {
                  Sheet sheet = createSheet(workBook, excelTemplate.getLeft());
                  createExcelTemplateTitle(sheet, excelTemplate.getRight(), excelSuffix);
            } catch (CustomException e) {
                continue;
            }

            writeSuccessCounter++;
        }
        outPut(path,excelName,excelSuffix,workBook);
        return writeSuccessCounter;
    }


    protected List<Field> preWrite(Class<?> excelTemplate,ExcelSuffix excelSuffix,Collection<?> excelEntities){

        List<Field> fields = filterTemplateFields(excelTemplate); //过滤模版中不包含注解的属性

        CustomValidator.checkCollectionNotEmpty(fields, String.format("无效的excelTemplate"));

        checkColumnLimit(fields.size(), excelSuffix);

        final int excelEntitySize = excelEntities.size();   //excelContent所占行

        checkRowLimit(excelEntitySize, excelSuffix);

        return fields;
    }

    //验证模版属性是否有效
    protected final boolean checkExcelTemplatePropertiesEffective(List<Field> fields, ExcelSuffix suffix, ExcelTemplateTitleOption option) {

        List<ExcelField> annotations = fields.stream().map((f) -> f.getAnnotation(ExcelField.class)).collect(Collectors.toList());

        final int capacity = annotations.size();

        Set<Integer> orders = Sets.newHashSetWithExpectedSize(capacity);

        int orderMaxLimit;              //排序字段最大限制

        switch (suffix) {
            case XLS:
                orderMaxLimit = SystemConstant.EXCEL_MAX_COLUMN_03;
                break;
            default:
                orderMaxLimit = SystemConstant.EXCEL_MAX_COLUMN_07;
        }

        final int defaultOrder = 1;         //excel模版,属性顺序从1开始

        if (option == ExcelTemplateTitleOption.CARE) {

            Set<String> titles = Sets.newHashSetWithExpectedSize(capacity);

            for (ExcelField excelField : annotations) {

                if (!StringUtils.hasText(excelField.title())) {
                    return false;
                }
                if (excelField.order() < defaultOrder || excelField.order() > orderMaxLimit) {
                    return false;
                }

                if (titles.contains(excelField.title())) {
                    return false;
                }
                titles.add(excelField.title());

                if (orders.contains(excelField.order())) {
                    return false;
                }
                orders.add(excelField.order());
            }
            return true;
        }

        for (ExcelField excelField : annotations) {

            if (excelField.order() < defaultOrder || excelField.order() > orderMaxLimit) {
                return false;
            }

            if (orders.contains(excelField.order())) {
                return false;
            }
            orders.add(excelField.order());
        }
        return true;
    }

    //输出excel
    protected boolean outPut(String outPutPath, String excelName, ExcelSuffix suffix, Workbook workbook) {

        FileOutputStream outputStream = null;
        try {
            outputStream = FileUtils.openOutputStream(new File(outPutPath, excelName + suffix.getSuffix()));
            workbook.write(outputStream);

            if (suffix == ExcelSuffix.UP_XLSX) {       //如果是07升级版excel,手动清理临时文件
                SXSSFWorkbook sxssfWorkbook = (SXSSFWorkbook) workbook;
                sxssfWorkbook.dispose();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeOutPutStream(outputStream, workbook);
        }
    }

    //关闭输出流
    protected final void closeOutPutStream(OutputStream outputStream, Workbook workbook) {

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭输入流
    protected final void closeInputStream(InputStream inputStream, Workbook workbook) {

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //验证excel 行是否超过最大值
    protected void checkRowLimit(int size, ExcelSuffix suffix) {

        switch (suffix) {
            case XLS:
                if (size > SystemConstant.EXCEL_MAX_ROW_03) {
                    throw new CustomException(String.format("03版excelRow不超过%s行", SystemConstant.EXCEL_MAX_ROW_03));
                }
                break;
            default:
                if (size > SystemConstant.EXCEL_MAX_ROW_07) {
                    throw new CustomException(String.format("07版excelRow不超过%s行", SystemConstant.EXCEL_MAX_ROW_07));
                }
        }
    }

    //验证excel 列是否超过最大值
    protected void checkColumnLimit(int size, ExcelSuffix suffix) {

        switch (suffix) {
            case XLS:
                if (size > SystemConstant.EXCEL_MAX_COLUMN_03) {
                    throw new CustomException(String.format("03版excelColumn不超过%s列", SystemConstant.EXCEL_MAX_COLUMN_03));
                }
                break;
            default:
                if (size > SystemConstant.EXCEL_MAX_COLUMN_07) {
                    throw new CustomException(String.format("07版excelColumn不超过%s列", SystemConstant.EXCEL_MAX_COLUMN_07));
                }
        }
    }

    //设置单元格的值
    protected void setCellValue(Class<?> fieldType, Cell cell, Object value) {

        ImmutableSet cellNumberType = ImmutableSet.of(int.class, Integer.class, long.class, Long.class, short.class,
                Short.class, byte.class, Byte.class, double.class, Double.class,
                float.class, Float.class);

        ImmutableSet cellBooleanType = ImmutableSet.of(boolean.class, Boolean.class);

        if (effectiveFieldType.contains(fieldType)) {

            if (fieldType == String.class) {
                cell.setCellValue(String.valueOf(value));
                return;
            }

            if (cellNumberType.contains(fieldType)) {

                cell.setCellValue(Double.parseDouble(value.toString()));
                return;
            }

            if (cellBooleanType.contains(fieldType)) {
                cell.setCellValue((boolean) value);
                return;
            }

            if (fieldType == Date.class) {
                cell.setCellValue((Date) value);
                return;
            }
        }
        throw new CustomException("无效的excel模版Cell类型");
    }

    //设置模版属性的值
    public static void setExcelTemplateFieldValue(Object t, Cell cell, Field field, Class<?> fieldType) {

        field.setAccessible(true);

        try {

            if (effectiveFieldType.contains(fieldType)) {

                if (fieldType == String.class) {

                    CellValueType.STRING.setFieldValue(t, cell, field);
                    return;
                }

                if (fieldType == Date.class) {
                    CellValueType.DATE.setFieldValue(t, cell, field);
                    return;
                }

                if (fieldType == int.class || fieldType == Integer.class) {
                    CellValueType.INTEGER.setFieldValue(t, cell, field);
                    return;
                }

                if (fieldType == long.class || fieldType == Long.class) {
                    CellValueType.LONG.setFieldValue(t, cell, field);
                    return;
                }

                if (fieldType == short.class || fieldType == Short.class) {
                    CellValueType.SHORT.setFieldValue(t, cell, field);
                    return;
                }

                if (fieldType == byte.class || fieldType == Byte.class) {
                    CellValueType.BYTE.setFieldValue(t, cell, field);
                    return;
                }

                if (fieldType == double.class || fieldType == Double.class) {
                    CellValueType.DOUBLE.setFieldValue(t, cell, field);
                    return;
                }
                if (fieldType == float.class || fieldType == Float.class) {
                    CellValueType.FLOAT.setFieldValue(t, cell, field);
                    return;
                }
                if (fieldType == boolean.class || fieldType == Boolean.class) {
                    CellValueType.BOOLEAN.setFieldValue(t, cell, field);
                    return;
                }
            }
            throw new CustomException("excel模版中出现不支持的属性类型");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description 此处用枚举的好处在于解耦合, 比如对模版属性类型进行拓展，
     * 需要添加对JSON类型的支持，那么可直接在CellValueType枚举类中声明JSON的支持。
     * 并将指定的类型添加到 ImmutableSet effectiveFieldType 中就可以拓展，
     * 至于实现:则只需要在注解 @ExcelField 中添加一个类似resolve() 区分是普通类型还是特殊类型,相应的校验策略进行变通即可
     * @Author yc
     * @Date 2020-06-22 18:39
     */

    enum CellValueType {

        STRING() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {

                field.set(t, cell.getStringCellValue());

            }
        }, LONG() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {

                field.set(t, (long) cell.getNumericCellValue());
            }
        }, SHORT() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {

                field.set(t, (short) cell.getNumericCellValue());
            }
        },
        DOUBLE() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {

                field.set(t, cell.getNumericCellValue());
            }
        }, FLOAT() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {

                field.set(t, (float) cell.getNumericCellValue());
            }
        }, DATE() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {
                field.set(t, cell.getDateCellValue());
            }
        },
        BYTE() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {
                field.set(t, (byte) cell.getNumericCellValue());
            }
        },
        INTEGER() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {
                field.set(t, (int) cell.getNumericCellValue());
            }
        },
        BOOLEAN() {
            @Override
            void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException {
                field.set(t, cell.getBooleanCellValue());
            }
        };

        abstract void setFieldValue(Object t, Cell cell, Field field) throws IllegalAccessException;
    }
}
