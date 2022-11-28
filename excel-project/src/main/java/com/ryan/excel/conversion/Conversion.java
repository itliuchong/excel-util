/*
* Personal Learning Use
 */
package com.ryan.excel.conversion;

import com.ryan.excel.annotation.ExcelField;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

/**
 * <P><B>Description: </B>类型转换</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/23 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public interface Conversion {

    Object conversion(Class<?> cls, Object val, ExcelField excelField) throws ParseException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
