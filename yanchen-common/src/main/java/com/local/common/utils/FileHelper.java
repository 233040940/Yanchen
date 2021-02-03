package com.local.common.utils;


import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 文件操作工具类  abs结尾方法都代表绝对路径,非abs结尾方法表示classPath相对路径
 * @date 2020-05-23 14:06
 */
public class FileHelper {

    private static final String EMPTY = "";
    private static final Charset CHARSET = Charsets.UTF_8;

    private FileHelper() {
        throw new RuntimeException("FileHelper is tool class,Not support instanced");
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Description 按行读取文件
     * @Param [filePath]
     * @Author yc
     * @Date 2020-06-12 12:38
     */
    public static List<String> readLines(String fileName) {
        return readLinesAbs(relativePath(), fileName);
    }

    public static List<String> readLinesAbs(String filePath, String fileName) {
        try {
            return Files.readLines(new File(filePath, fileName), CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * @return java.lang.String
     * @Description 读取全部
     * @Param [filePath]
     * @Author yc
     * @Date 2020-06-12 12:39
     * @version 1.0
     */

    public static String readAll(String fileName) {
        return readAllAbs(relativePath(), fileName);
    }

    public static String readAllAbs(String filePath, String fileName) {
        try {
            return IOUtils.toString(FileUtils.openInputStream(new File(filePath, fileName)), CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY;
        }
    }

    /**
     * @return java.lang.String
     * @Description 获取classPath 相对路径
     * @Author yc
     */

    public static String relativePath() {
        return FileHelper.class.getResource("/").getPath();
    }

    /**
     * @return boolean
     * @Description 写文件
     * @Param [fileName, writeContent]
     * @Author yc
     * @Date 2020-06-20 15:57
     */

    public static boolean write(String fileName, String writeContent) {
        return writeAbs(relativePath(), fileName, writeContent);
    }

    public static boolean writeAbs(String filePath, String fileName, String writeContent) {
        try {
            Files.write(writeContent.getBytes(CHARSET), new File(filePath, fileName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return boolean
     * @Description 向文件追加内容
     * @Param [fileName, appendContent]
     * @Author yc
     * @Date 2020-06-20 15:56
     */

    public static boolean append(String fileName, String appendContent) {
        return appendAbs(relativePath(), fileName, appendContent);
    }

    public static boolean appendAbs(String filePath, String fileName, String appendContent) {
        try {
            Files.asCharSink(new File(filePath, fileName), CHARSET, FileWriteMode.APPEND).write(appendContent);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return boolean
     * @Description 创建文件
     * @Param [fileName]
     * @Author yc
     * @Date 2020-06-20 15:58
     */

    public static boolean createFile(String fileName) {
        return createFileAbs(relativePath(), fileName);
    }

    public static boolean createFileAbs(String filePath, String fileName) {
        try {
            File file = new File(filePath, fileName);
            if (!exists(file)) {
                file.createNewFile();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return boolean
     * @Description 验证文件是否存在
     * @Param [file]
     * @Author yc
     * @Date 2020-06-20 15:58
     */

    public static boolean exists(File file) {
        return file.exists();
    }

    /**
     * @return java.lang.String
     * @Description 获取文件拓展名
     * @Param [fullName 表示文件所在全限定名]
     * @Author yc
     */

    public static String getFileExtensionName(String fullName) {
        return Files.getFileExtension(fullName);
    }

    /**
     * @return java.lang.String
     * @Description 获取文件名称不包含拓展名
     * @Param [fullName 表示文件所在全限定名]
     * @Author yc
     */

    public static String getFileNameExcludeExtension(String fullName) {
        return Files.getNameWithoutExtension(fullName);
    }

    /**
     * @return boolean
     * @Description 比较两个文件内容是否相同
     * @Param [to, from]
     * @Author yc
     * @Date 2020-06-16 13:04
     */

    public static boolean equals(File to, File from) {
        try {
            HashCode hash = Files.asByteSource(to).hash(Hashing.sha256());
            HashCode hash1 = Files.asByteSource(from).hash(Hashing.sha256());
            return hash.equals(hash1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @return boolean
     * @Description spring环境下上传文件
     * @Param [uploadPath 文件上传路径, fileName 文件名称, multipartFile]
     * @Author yc
     */

    public static boolean upLoadOfSpring(String uploadPath, String fileName, MultipartFile multipartFile) {
        try {
            multipartFile.transferTo(new File(uploadPath, fileName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return void
     * @Description 文件下载
     * @Param [filePath, fileName, response]
     * @Author yc
     * @Date 2020-06-12 12:41
     * @version 1.0
     */

    public static void downLoad(String filePath, String fileName, HttpServletResponse response) {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = FileUtils.openInputStream(new File(filePath, fileName));
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int temp;
            while ((temp = is.read(buffer)) != -1) {

                os.write(buffer, 0, temp);
            }
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding(CHARSET.toString());
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
