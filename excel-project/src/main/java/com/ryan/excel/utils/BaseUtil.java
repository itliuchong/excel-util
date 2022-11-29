/*
* Personal Learning Use
 */
package com.ryan.excel.utils;

import com.ryan.excel.annotation.ExcelField;
import com.ryan.excel.exception.CommonException;
import com.ryan.excel.model.enums.ErrorEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <P><B>Description: </B>基础工具</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/29 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class BaseUtil {

    /**
     * 获取ExcelField注解的方法或字段
     */
    public static List<Object[]> getAnnotationList(Class<?> cls){
        ArrayList<Object[]> annotationList = new ArrayList<>();
        //字段
        annotationList.addAll(getAnnotationList(cls.getDeclaredFields()));
        //方法
        annotationList.addAll(getAnnotationList(cls.getDeclaredMethods()));
        //字段排序
        annotationList.sort(Comparator.comparing(o -> ((ExcelField) o[0]).index()));
        return annotationList;
    }


    /**
     * 获取ExcelField注解的方法或字段
     */
    public static List<Object[]> getAnnotationList(Object[] objects){
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
     * 获取类的所有字段（包含父类）
     * @param cls 类型
     * @return 结果
     */
    public static Field[] getAllFields(Class<?> cls){
        List<Field> fieldList;
        for (fieldList = new ArrayList<>(); cls != null; cls = cls.getSuperclass()){
            fieldList.addAll(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }
}
