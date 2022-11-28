/*
* Personal Learning Use
 */
package com.ryan.excel.model.enums;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public enum ErrorEnum {

    EXCEL_TEMPLATE_ERROR("ERROR-0005", "Excel template error."),

    PARAM_ERROR("ERROR-0004", "The param is error."),

    /**
     * 参数类型匹配
     */
    PARAM_TYPE_MATCHING_FAILED("ERROR-0003", "The type is not Field or Method."),

    /**
     * Excel文件格式错误
     */
    EXCEL_TYPE_FAILED("ERROR-0002", "Excel file format error."),

    /**
     * 文件不存在
     */
    FILE_NOT_EXIST("ERROR-0001", "File is not exist");
    /**
     * 异常代码。
     */
    private  String code;

    /**
     * 异常信息。
     */
    private  String errorMessage;

    ErrorEnum(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
