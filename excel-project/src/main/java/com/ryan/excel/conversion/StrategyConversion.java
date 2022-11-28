/*
 * Personal Learning Use
 */
package com.ryan.excel.conversion;

import com.ryan.excel.annotation.ExcelField;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <P><B>Description: </B>类型转换</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/23 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class StrategyConversion implements Conversion{

    /**
     * 转换对象Map
     */
    private static Map<Class<?>, Conversion> conversionMap = new HashMap<>();

    public StrategyConversion() {
        conversionMap.put(String.class, new StringConversion());
        conversionMap.put(Long.class, new LongConversion());
        conversionMap.put(Date.class, new DateConversion());
        conversionMap.put(Double.class, new DoubleConversion());
        conversionMap.put(Float.class, new FloatConversion());
        conversionMap.put(Integer.class, new IntegerConversion());
    }

    @Override
    public Object conversion(Class<?> cls, Object val, ExcelField excelField) throws ParseException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Conversion conversion = conversionMap.get(cls);
        return conversion.conversion(cls, val, excelField);
    }
}
