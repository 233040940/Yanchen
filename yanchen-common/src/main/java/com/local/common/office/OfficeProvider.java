package com.local.common.office;

import com.local.common.enums.DocSuffix;
import com.local.common.office.pdf.PDFHelper;
import com.local.common.office.word.WordHelper;

/**
 * @program: yanchen
 * @description: TODO
 * @author: yc
 * @date: 2020-08-12 13:44
 **/
public class OfficeProvider {

    public static boolean PDFToWord(String filePath, String fileName, String outPutPath){
        return PDFHelper.toWordDoc(filePath, fileName, outPutPath, DocSuffix.PDF);
    }

    public static boolean PDFToWord(String filePath, String fileName){
        return PDFHelper.toWordDoc(filePath, fileName, filePath, DocSuffix.PDF);
    }

    public static boolean WordToPDF(String filePath, String fileName, String outPutPath){
        return WordHelper.toPDFDoc(filePath, fileName, outPutPath, DocSuffix.WORD);
    }
}
