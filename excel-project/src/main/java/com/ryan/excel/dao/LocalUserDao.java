/*
* Personal Learning Use
 */
package com.ryan.excel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryan.excel.model.entity.LocalUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
@Mapper
public interface LocalUserDao extends BaseMapper<LocalUserEntity> {
}
