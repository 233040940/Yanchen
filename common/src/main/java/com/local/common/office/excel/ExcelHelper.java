package com.local.common.office.excel;

import com.local.common.enums.ExcelSuffix;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import java.util.Collection;
import java.util.Map;

/**
 * @author yc
 * @project yanchen
 * @description  读写Excel接口
 * @date 2020-05-23 14:07
 */
public interface ExcelHelper<T> {

      /**
       * @Description 读取单个sheet到集合
       * @Param path 生成excel文件路径
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
        * @Param path 生成excel文件路径
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
       * @Param path 生成excel文件路径
       * @param excelName excel名称
       * @param excelEntities 指定生成excel的多个模版,Pair[String(sheetName),Class<?>(模版类class)]
       * @param excelSuffix excel文件的后缀
       * @return Map<String,Collection <?>>
       * @Author yc
       */
      Map<String,Collection <?>> batchRead(String path, String excelName, Collection<Pair<String,Class<?>>> excelEntities,ExcelSuffix excelSuffix);

      /**
       * @Description 批量写excel多个sheet
       * @Param path 生成excel文件路径
       * @param excelName excel名称
       * @param excelSuffix 生成excel文件的后缀
       * @param excelEntities 多个模版对象集合;triple[String(sheetName),Class <?> (模版类class),Collection具体的模版对象集合]
       * @return int 写成功的个数
       * @Author yc
       */
      int  batchWrite(String path, String excelName, ExcelSuffix excelSuffix, Collection<Triple<String,Class<?>,Collection<?>>> excelEntities);
}
