package com.local.common.office.excel;

import com.local.common.office.excel.entity.ExcelTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collection;
import java.util.Map;

/**
 * @author yc
 * @project yanchen
 * @description  读写Excel接口，Pair,Triple类需要导入org.apache.commons.lang包
 * @date 2020-05-23 14:07
 */
public interface ExcelProvider<T extends ExcelTemplate> {

       int EXCEL_MAX_COLUMN_03=256;       //03版最大列

       int EXCEL_MAX_COLUMN_07=16384;     //07版最大列

       int EXCEL_MAX_ROW_03=65536;        //03版最大行

       int EXCEL_MAX_ROW_07=1048576;      //07版最大行

    /**
     * @author yc
     * @description excel后缀
     * @date 2020-06-18 10:42
     */
    enum ExcelSuffix {

        XLS(".xls", "03版excel"), XLSX(".xlsx", "07版excel"),UP_XLSX(".xlsx","07升级版");

        private String suffix;
        private String description;

        ExcelSuffix(String suffix, String description) {
            this.suffix = suffix;
            this.description = description;
        }

        public String getSuffix() {
            return suffix;
        }

    }

    /**
     * @project yanchen
     * @description excel模版对象是否在意title,用于区分生成excel还是读取excel
     * @date 2020-06-22 01:45
     */
     enum ExcelTemplateTitleOption {
        CARE("生成excel在意"),NOT_CARE("读取excel不在意");
        private String description;
        ExcelTemplateTitleOption(String description){
            this.description=description;
        }
    }

      /**
       * @Description 读取单个sheet到集合
       * @param path 生成excel文件路径
       * @param excelName excel名称
       * @param sheetName 工作簿名称
       * @param excelTemplate 指定生成excel的模版
       * @param excelSuffix 生成excel文件的后缀
       * @return Collection
       * @Author yc
       */
      Collection<T> read(String path, String excelName, String sheetName, Class<T>excelTemplate, ExcelSuffix excelSuffix);

      /**
        * @Description 写单个sheet
        * @param  path 生成excel文件路径
        * @param excelName excel名称
        * @param sheetName sheet名称
        * @param excelTemplate 指定生成excel的模版
        * @param excelSuffix excel文件的后缀
        * @param excelEntities excel中单个sheet的内容对象集合
        * @return boolean
        * @Author yc
        */

      boolean write(String path, String excelName, String sheetName, Class<T> excelTemplate, ExcelSuffix excelSuffix,Collection<T> excelEntities) ;

      /**
       * @Description 批量读取多个sheet到map
       * @param path 生成excel文件路径
       * @param excelName excel名称
       * @param excelEntities 指定生成excel的多个模版,Pair[String(工作簿名称),Class<? extends ExcelTemplate >(模版类class)]
       * @param excelSuffix excel文件的后缀
       * @return Map<String,Collection <?  extends ExcelTemplate>>
       * @Author yc
       */
      Map<String,Collection <? extends ExcelTemplate>> batchRead(String path, String excelName, Collection<Pair<String,Class<? extends ExcelTemplate>>> excelEntities,ExcelSuffix excelSuffix);

      /**
       * @Description 批量写excel多个sheet
       * @param path 生成excel文件路径
       * @param excelName excel名称
       * @param excelEntities 多个模版对象集合;triple[String(工作簿名称),Class <?  extends ExcelTemplate> (模版类class),Collection具体的模版对象集合]
       * @param excelSuffix 生成excel文件的后缀
       * @return int 写成功的个数
       * @Author yc
       */
      int  batchWrite(String path, String excelName,Collection<Triple<String,Class<?  extends ExcelTemplate>,Collection<?  extends ExcelTemplate>>> excelEntities,ExcelSuffix excelSuffix);
}
