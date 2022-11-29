/*
 * Personal Learning Use
 */
package com.ryan.excel.utils;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ryan.excel.exception.CommonException;
import com.ryan.excel.model.enums.ErrorEnum;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

/**
 * <P><B>Description: </B>文件工具</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/29 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class FileUtil {

    /**
     * 上传保存路径
     */
    public static final String FILE_DOWN_STORAGE_PATH = "/downloadFile";

    /**
     * 文件夹格式
     */
    public static final String FILE_PATH_FORMAT = "yyyyMMddHHmmss";

    public static final String SYSTEM = "system";

    /**
     * 创建FileItem对象
     * Revision Trail: (Date/Author/Description)
     * 2022/7/27 0016 Colin Lin CREATE
     *
     * @author Ryan Huang
     */
    public static FileItem createFileItem(File file, String fieldName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }


    /**
     * 将excel数据写入文件
     * @param fileName 文件名
     * @param user 用户
     * @param workbook 工作簿
     * @return 结果
     */
    public static File writeExcelFile(String fileName, String user, SXSSFWorkbook workbook){
        if(StringUtils.isBlank(user)){
            user = SYSTEM;
        }
        String folderPath = FILE_DOWN_STORAGE_PATH + File.separator + user + File.separator + DateUtil.format(new Date(), FILE_PATH_FORMAT) + File.separator;
        String filePath = folderPath + fileName;
        File folder = new File(folderPath);
        //创建文件夹
        if (!folder.exists() && !folder.mkdirs()) {
            throw new CommonException(ErrorEnum.MKDIR_ERROR.getCode(), ErrorEnum.MKDIR_ERROR.getErrorMessage());
        }
        File file = new File(filePath);
        try (OutputStream os = new FileOutputStream(file)) {
            workbook.write(os);
            os.flush();
        }catch (Exception e){
            throw new CommonException(ErrorEnum.WRITE_FILE_ERROR.getCode(), ErrorEnum.WRITE_FILE_ERROR.getErrorMessage());
        }
        return file;
    }

}
