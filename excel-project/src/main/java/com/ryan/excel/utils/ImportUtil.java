/*
* Personal Learning Use
 */
package com.ryan.excel.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ryan.excel.annotation.ExcelField;
import com.ryan.excel.constants.ExcelConstant;
import com.ryan.excel.conversion.StrategyConversion;
import com.ryan.excel.exception.CommonException;
import com.ryan.excel.model.enums.ErrorEnum;
import com.ryan.excel.reflections.Reflections;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class ImportUtil {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImportUtil.class);

    /**
     * 工作簿
     */
    private Workbook workBook;

    /**
     * sheet
     */
    private Sheet sheet;

    /**
     * 头部行数
     */
    private Integer headNum;

    /**
     * sheet 索引
     */
    private Integer sheetNum;

    /**
     * 获取指定行
     */
    public Row getRow(int rowNum) {return this.sheet.getRow(rowNum);}

    /**
     * 获取最后一个数据的行索引
     */
    public int getLastRowNum() {return this.sheet.getLastRowNum() + headNum; }

    /**
     * 获取单元格值
     */
    public Object getCellValue(Row row, int column){
        Object val = StringPool.EMPTY;
        Cell cell = row.getCell(column);
        if(cell != null){
            CellType cellType = cell.getCellType();
            if(CellType.STRING.equals(cellType)){
                val = cell.getStringCellValue();
            }else if(CellType.BOOLEAN.equals(cellType)){
                val = cell.getBooleanCellValue();
            }else if(CellType.NUMERIC.equals(cellType)){
                DecimalFormat decimalFormat = new DecimalFormat("0");
                val = decimalFormat.format(cell.getNumericCellValue());
            }else if(CellType.ERROR.equals(cellType)){
                val = cell.getErrorCellValue();
            }
        }
        return val;
    }

    /**
     * 获取ExcelField注解的方法或字段
     */
    public List<Object[]> getAnnotationList(Class<?> cls){
        ArrayList<Object[]> annotationList = new ArrayList<>();
        //字段
        annotationList.addAll(getAnnotationList(cls.getDeclaredFields()));
        //方法
        annotationList.addAll(getAnnotationList(cls.getDeclaredMethods()));
        return annotationList;
    }



    /**
     * 获取ExcelField注解的方法或字段
     */
    public List<Object[]> getAnnotationList(Object[] objects){
        List<Object[]> annotationList = new ArrayList<>();
        for (Object object : objects) {
            ExcelField excelField;
            if(object instanceof Field){
                excelField = ((Field) object).getAnnotation(ExcelField.class);
            }else if(object instanceof Method){
                excelField = ((Method) object).getAnnotation(ExcelField.class);
            }else{
                throw new CommonException(ErrorEnum.FILE_NOT_EXIST.getCode(), ErrorEnum.FILE_NOT_EXIST.getErrorMessage());
            }
            if(excelField != null && (excelField.type() == 0 || excelField.type() == 1)){
                annotationList.add(new Object[]{excelField, object});
            }
        }
        return annotationList;
    }

    /**
     * 获取ExcelField注解中 字段的修饰类型 或 方法的返回类型
     */
    private Class<?> getValueType(Object[] objects){
        Class<?> valueType = Class.class;
        if(objects[1] instanceof Field){
            valueType = ((Field) objects[1]).getType();
        }else if(objects[1] instanceof Method){
            Method method = (Method) objects[1];
            String methodName = method.getName();
            String prefixMethod = methodName.substring(0, 3);
            //get方法获取返回类型
            if(ExcelConstant.GTE.equals(prefixMethod)){
                valueType = method.getReturnType();
            }
            //set方法获取形参类型
            else if(ExcelConstant.SET.equals(prefixMethod)){
                valueType = ((Method) objects[1]).getParameterTypes()[0];
            }
        }
        return valueType;
    }

    /**
     * 类型转换
     */
    private Object conversionVal(Class<?> valueType, Object val, ExcelField excelField) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, ParseException, InvocationTargetException {
        StrategyConversion strategyConversion = new StrategyConversion();
        return strategyConversion.conversion(valueType, val, excelField);
    }

    /**
     * 自定义对象赋值
     * @param objects ExcelFiled数组  [ExcelFiled, Method/Field ]
     * @param object 赋值对象
     * @param val 字段值
     * @param valueType 字段类型
     */
    private void setEntityValue(Object[] objects, Object object, Object val, Class<?> valueType){
        if(val instanceof String && StringUtils.isBlank(val.toString())){
            val = null;
        }
        Object fieldOrMethod = objects[1];
        if(fieldOrMethod instanceof  Field){
            Reflections.invokeSetter(object, ((Field) fieldOrMethod).getName(), val);
        }else if(fieldOrMethod instanceof Method){
            String methodName = ((Method) fieldOrMethod).getName();
            if(ExcelConstant.SET.equals(methodName.substring(0, 3))){
                Reflections.invokeMethod(object, methodName, new Class[]{valueType}, new Object[]{val});
            }
        }
    }

    /**
     * 判断一个对象的所有字段值是否为空
     */
    public static boolean checkFieldAllNull(Object object) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())){
                continue;
            }
            if(!ObjectUtil.isEmpty(field.get(object))){
                return false;
            }
            field.setAccessible(false);
        }
        //super
        for (Field field : object.getClass().getFields()) {
            field.setAccessible(true);
            if(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())){
                continue;
            }
            if(!ObjectUtil.isEmpty(field.get(object))){
                return false;
            }
            field.setAccessible(false);
        }
        return true;
    }

    /**
     * 获取要转换的自定义对象的excel数据，并校验表头格式
     * 校验多行头
     * @param cls 自定义对象
     * @param headList 头数据
     * @param <E> 集合类型
     * @return 结果
     */
    public <E> List<E> getDataListAndValidHead(Class<E> cls, List<List<String>> headList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(headList.size() != headNum + 1){
            throw new CommonException(ErrorEnum.PARAM_ERROR.getCode(), ErrorEnum.PARAM_ERROR.getErrorMessage());
        }
        for (int i = 0; i< headNum + 1; i++){
            List<String> heads = headList.get(i);
            if(heads == null){
                break;
            }
            Row row = sheet.getRow(i);
            for (int column = 0; column < heads.size(); column++) {
                if(heads.get(column) == null){
                    break;
                }
                Object cellValue = this.getCellValue(row, column);
                if(!ObjectUtil.equals(cellValue, heads.get(column))){
                    throw new CommonException(ErrorEnum.EXCEL_TEMPLATE_ERROR.getCode(), ErrorEnum.EXCEL_TEMPLATE_ERROR.getErrorMessage());
                }
            }
        }
        return getDataList(cls);
    }

    /**
     * 获取要转换的自定义对象的excel数据，并校验表头格式
     * 只校验单行头
     * @param cls 自定义对象类型
     * @return 结果
     */
    public <E> List<E> getDataListAndValidHead(Class<E> cls) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object[]> annotationList = getAnnotationList(cls.getDeclaredFields());
        for (int i = 0; i < headNum + 1; i++) {
            Row row = this.getRow(i);
            int column = 0;
            for (Object[] objects : annotationList) {
                ExcelField excelField = (ExcelField) objects[0];
                Object cellValue = this.getCellValue(row, column);
                if(cellValue != null && excelField != null && StringUtils.isNotBlank(excelField.title())){
                    if(!ObjectUtil.equals(cellValue, excelField.title())){
                        throw new CommonException(ErrorEnum.EXCEL_TEMPLATE_ERROR.getCode(), ErrorEnum.EXCEL_TEMPLATE_ERROR.getErrorMessage());
                    }
                }
                column++;
            }
        }
        return getDataList(cls);
    }

    /**
     * 获取要转换为自定义对象的excel数据
     * @param cls 自定义对象类型
     * @param <E> 结果类型
     * @return 结果
     */
    public <E> List<E> getDataList(Class<E> cls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object[]> annotationList = getAnnotationList(cls);
        List<E> dataList = new ArrayList<>();
        for (int i = this.getHeadNum() + 1; i < this.getLastRowNum() + 1; i++) {
            //利用反射创造自定义对象
            E e = cls.getDeclaredConstructor().newInstance();
            Row row = this.getRow(i);
            //列索引
            int column = 0;
            if(row != null){
                for (Object[] objects : annotationList) {
                    ExcelField excelField = (ExcelField) objects[0];
                    Object cellValue = this.getCellValue(row, column);
                    if(cellValue != null){
                        Class<?> valueType = getValueType(objects);
                        try {
                            cellValue = conversionVal(valueType, cellValue, excelField);
                        }catch (Exception exception){
                            LOG.info("Get cell value [" + i + "," + column + "] error: " + exception.toString());
                            cellValue = null;
                        }
                        this.setEntityValue(objects, e, cellValue, valueType);
                    }
                    column++;
                }
                if(checkFieldAllNull(e)){
                    break;
                }
                dataList.add(e);
            }
        }
        return dataList;
    }


    public Integer getHeadNum() {
        return headNum;
    }

    public ImportUtil(File file, Integer headNum, Integer sheetNum) throws  IOException{
        this(file.getName(), new FileInputStream(file), headNum, sheetNum);
    }

    public ImportUtil(MultipartFile multipartFile, Integer headNum, Integer sheetNum) throws  IOException{
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headNum, sheetNum);
    }

    public ImportUtil(String fileName, InputStream inputStream, Integer headNum, Integer sheetIndex) throws IOException {
        if(StringUtils.isBlank(fileName)){
            throw new CommonException(ErrorEnum.FILE_NOT_EXIST.getCode(), ErrorEnum.FILE_NOT_EXIST.getErrorMessage());
        }else if(fileName.toLowerCase().endsWith(ExcelConstant.XLS)){
            this.workBook = new HSSFWorkbook(inputStream);
        }else if(fileName.toLowerCase().endsWith(ExcelConstant.XLSX)){
            this.workBook = new XSSFWorkbook(inputStream);
        }else{
            throw new CommonException(ErrorEnum.EXCEL_TYPE_FAILED.getCode(), ErrorEnum.EXCEL_TYPE_FAILED.getErrorMessage());
        }
        if(this.workBook.getNumberOfSheets() < sheetIndex){
            throw new CommonException(ErrorEnum.EXCEL_TYPE_FAILED.getCode(), ErrorEnum.EXCEL_TYPE_FAILED.getErrorMessage());
        }

        this.sheet = this.workBook.getSheetAt(sheetIndex);
        this.headNum = headNum;
        this.sheetNum = sheetIndex;
    }
}
