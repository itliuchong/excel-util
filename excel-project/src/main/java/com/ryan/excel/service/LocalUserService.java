/*
* Personal Learning Use
 */
package com.ryan.excel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.excel.dao.LocalUserDao;
import com.ryan.excel.model.dto.LocalUserImportDTO;
import com.ryan.excel.model.entity.LocalUserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
public interface LocalUserService extends IService<LocalUserEntity> {


    List<LocalUserImportDTO> importLocalUser(MultipartFile multipartFile) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
