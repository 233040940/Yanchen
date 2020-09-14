package com.local.common.office.pdf;

import com.local.common.enums.DocSuffix;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * @program: yanchen
 * @description: TODO
 * @author: yc
 * @date: 2020-08-06 17:28
 **/
public class PDFHelper {

    public static  boolean toWordDoc(String filePath, String fileName, String outPutPath,DocSuffix docSuffix){
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(filePath + fileName + docSuffix.getSuffix()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int pageTotal = doc.getNumberOfPages();//获取总页数
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPutPath + fileName + DocSuffix.WORD.getSuffix());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(fos, "UTF-8");//文件按字节读取，然后按照UTF-8的格式编码显示
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PDFTextStripper stripper = null;//生成PDF文档内容剥离器
        try {
            stripper = new PDFTextStripper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stripper.setSortByPosition(true);//排序
        stripper.setStartPage(1);//设置转换的开始页
        stripper.setEndPage(pageTotal);//设置转换的结束页
        try {
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
           return true;
    }

}
