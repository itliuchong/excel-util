/*
* Personal Learning Use
 */
package com.ryan.excel.exception;

import java.io.Serializable;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class CommonException extends RuntimeException implements Serializable {

    /**
     * 序列号
     */
    private static final long serialVersionUID = -422452041489241546L;

    /**
     * 异常代码。
     */
    private final String code;

    /**
     * 异常信息。
     */
    private final String errorMessage;

    public CommonException(String code, String msg, Object... data) {
        this.code = code;
        if (data != null && data.length > 0) {
            this.errorMessage = String.format(msg, data);
        } else {
            this.errorMessage = msg;
        }
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
