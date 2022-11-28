/*
 * Copyright (c) 2005, 2022, EVECOM Technology Co.,Ltd. All rights reserved.
 * EVECOM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ryan.excel.model.dto;

import com.ryan.excel.annotation.ExcelField;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/25 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public class LocalUserImportDTO {

    @ExcelField(title = "姓名")
    private String userName;

    @ExcelField(title = "手机")
    private Integer userPhone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Integer userPhone) {
        this.userPhone = userPhone;
    }
}
