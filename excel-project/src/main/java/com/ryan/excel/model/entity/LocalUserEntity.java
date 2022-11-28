/*
* Personal Learning Use
 */
package com.ryan.excel.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/21 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
@TableName("LOCAL_USER")
public class LocalUserEntity extends BaseEntity{

    /**
     * 姓名
     */
    @TableField(value = "USER_NAME")
    private String userName;

    /**
     * 手机
     */
    @TableField(value = "USER_PHONE")
    private String userPhone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
