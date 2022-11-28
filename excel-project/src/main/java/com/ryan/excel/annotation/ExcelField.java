package com.ryan.excel.annotation;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.ryan.excel.constants.ExcelConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {


    /**
     * 字段值
     */
    String value() default ExcelConstant.EMPTY;

    /**
     * 标题名称
     */
    String title() default ExcelConstant.EMPTY;

    /**
     * 0：导入导出 1：导入 2：导出
     */
    int type() default 0;

    /**
     * 日期格式
     */
    String dateFormat() default ExcelConstant.EMPTY;



}
