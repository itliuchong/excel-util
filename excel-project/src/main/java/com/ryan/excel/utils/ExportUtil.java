/*
* Personal Learning Use
 */
package com.ryan.excel.utils;

import com.ryan.excel.annotation.ExcelField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <P><B>Description: </B>excel导出工具</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/29 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class ExportUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExportUtil.class);

    private SXSSFWorkbook workbook;

    private Sheet sheet;


    /**
     * 导出为excel
     * @param fileName 文件名
     * @param dataList 导出数据
     * @param cls 导出类
     * @param <E> 导出数据类型
     * @return 结果
     */
    public static <E> File writeExcel(String fileName, List<E> dataList, Class<?> cls, boolean isShowHead) throws IllegalAccessException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();
        int headNum = 0;
        if(isShowHead) {
            SXSSFRow row = sheet.createRow(headNum);
            List<Object[]> headObjects = BaseUtil.getAnnotationList(cls);
            for (int i = 0; i < headObjects.size(); i++) {
                SXSSFCell cell = row.createCell(i);
                setHeadStyle(workbook, cell);
                cell.setCellValue(((ExcelField)headObjects.get(i)[0]).title());
            }
            headNum = 1;
        }
        if(null != dataList && !dataList.isEmpty()){
            for (int var1 = 0; var1 < dataList.size(); var1++) {
                SXSSFRow row = sheet.createRow(var1 + headNum);
                E e = dataList.get(var1);
                int column = 0;
                Field[] fields = BaseUtil.getAllFields(e.getClass());
                for (Field field : fields) {
                    ExcelField annotation = field.getAnnotation(ExcelField.class);
                    if(annotation != null){
                        field.setAccessible(true);
                        Object obj = field.get(e);
                        SXSSFCell cell = row.createCell(column);
                        setBodyCellStyle(workbook, cell);
                        cell.setCellValue(obj == null ? null : String.valueOf(obj));
                    }
                    column++;
                }
            }
        }
        return FileUtil.writeExcelFile(fileName, workbook);
    }

    /**
     * 设置头样式
     */
    public static void setHeadStyle(SXSSFWorkbook workbook, SXSSFCell cell){
        CellStyle headStyle = workbook.createCellStyle();
        // 设置背景色
        headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getColor().getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框
        headStyle.setBorderBottom(BorderStyle.THIN); //下边框
        headStyle.setBorderLeft(BorderStyle.THIN);//左边框
        headStyle.setBorderTop(BorderStyle.THIN);//上边框
        headStyle.setBorderRight(BorderStyle.THIN);//右边框
        //设置居中
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置字体
        Font font = workbook.createFont();
        font.setFontName("仿宋_GB2312");
        font.setFontHeightInPoints((short) 15);// 设置字体大小
        font.setBold(true);// 粗体显示
        headStyle.setFont(font);
        //设置编码
        cell.setCellValue(HSSFCell.ENCODING_UTF_16);

        cell.setCellStyle(headStyle);
    }

    /**
     * 设置单元格样式
     */
    public static void setBodyCellStyle(SXSSFWorkbook workbook, SXSSFCell cell){
        //设置边框
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);// 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框
        //设置居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置字体
        Font font = workbook.createFont();
        font.setFontName("仿宋_GB2312");
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        //设置编码
        cell.setCellValue(HSSFCell.ENCODING_UTF_16);
        cell.setCellStyle(cellStyle);

    }


}
