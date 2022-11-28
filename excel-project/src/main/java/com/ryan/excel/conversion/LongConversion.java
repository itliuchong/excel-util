/*
 * Copyright (c) 2005, 2022, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ryan.excel.conversion;

import com.ryan.excel.annotation.ExcelField;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

/**
 * <P><B>Description: </B>Long类型转换</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/23 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class LongConversion implements Conversion{
    @Override
    public Object conversion(Class<?> cls, Object val, ExcelField excelField) throws ParseException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Long.valueOf(val.toString());
    }
}
