/*
* Personal Learning Use
 */
package com.ryan.excel.model;

import com.ryan.excel.validate.Dict;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/8/26 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class ThisBean {

    @Dict
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
