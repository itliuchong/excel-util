/*
* Personal Learning Use
 */
package com.ryan.excel.conversion;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ryan.excel.annotation.ExcelField;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;

/**
 * <P><B>Description: </B>日期类型转换</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/23 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class DateConversion implements Conversion{
    @Override
    public Object conversion(Class<?> cls, Object val, ExcelField excelField) throws ParseException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(StringUtils.isNotBlank(excelField.dateFormat())){
            val = DateUtil.parse(val.toString(), excelField.dateFormat());
        }else{
            val = new Date((long) val);
        }
        return val;
    }
}
