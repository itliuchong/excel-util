package com.ryan.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

@SpringBootTest
class ExcelApplicationTests {

    /**
     * 创建工作簿
     */
    @Test
    public void workbookTest(){
        //.xls文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //.xlsx文件
        XSSFWorkbook wb2 = new XSSFWorkbook();
        try(OutputStream file = new FileOutputStream("workbook.xlsx")){
            wb2.write(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建sheet
     */
    @Test
    public void sheetTest(){
        HSSFWorkbook wb = new HSSFWorkbook();
        /**
         * sheet名称不能超过31字符, 且不能包含以下符号
         *   0x0000
         *   0x0003
         *   colon (:)
         *   backslash (\)
         *   asterisk (*)
         *   question mark (?)
         *   forward slash (/)
         *   opening square bracket ([)
         *   closing square bracket (])
         */
        HSSFSheet sheet = wb.createSheet("sheet");
        //该方法可将无效字符替换为空格
        String safeSheetName = WorkbookUtil.createSafeSheetName("??sheet");
        // return sheet
        HSSFSheet sheetTwo = wb.createSheet(safeSheetName);
        try(OutputStream file = new FileOutputStream("sheet.xls")){
            wb.write(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void cellTest(){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("sheet");
        //创建行
        HSSFRow row = sheet.createRow(0);
        //创建单元格
        HSSFCell cell = row.createCell(0);
        //单元格赋值
        cell.setCellValue("这是一个单元格");
        writeFile(wb, "cell.xls");
    }

    /**
     * 文件输出
     * @param work
     * @param fileName
     */
    public static void writeFile(HSSFWorkbook work, String fileName){
//        String path = System.getProperty("user.dir");
//        File fileDir = new File(path + File.separator + "file");
//        try(!fileDir.mkdir()){
//            throw new  RuntimeException("文件夹创建错误！");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try(OutputStream file = new FileOutputStream(new File(fileDir + File.separator + fileName))){
//            work.write(file);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Test
    void test1(){
        //工作簿
        HSSFWorkbook work = new HSSFWorkbook();
        HSSFCreationHelper createHelper = work.getCreationHelper();
        //sheet
        String sheetName = WorkbookUtil.createSafeSheetName("sheet1");
        HSSFSheet sheet = work.createSheet(sheetName);
        //row
        HSSFRow row = sheet.createRow(0);
        HSSFRow rowOne = sheet.createRow(1);
        //cell
        HSSFCellStyle cellStyle = work.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        HSSFCell cell = row.createCell(0);
        HSSFCell cellOne = row.createCell(1);
        cell.setCellValue("单元格值");
        cellOne.setCellValue(new Date());
        cellOne.setCellStyle(cellStyle);
        System.out.println("开头：" + sheet.getFirstRowNum());
        System.out.println("最后一行：" + sheet.getLastRowNum());
        try(OutputStream file = new FileOutputStream("新建.xls")){
            work.write(file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor hssfClientAnchor = new HSSFClientAnchor(0, 0, 10, 10, (short) 0, 0, (short) 3, 3);
        HSSFSimpleShape shape = patriarch.createSimpleShape(hssfClientAnchor);
        shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_ARC);
        try(OutputStream file = new FileOutputStream("新建.xls")){
            wb.write(file);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
