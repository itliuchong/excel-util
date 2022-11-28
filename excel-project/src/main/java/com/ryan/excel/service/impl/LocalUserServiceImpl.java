/*
* Personal Learning Use
 */
package com.ryan.excel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryan.excel.dao.LocalUserDao;
import com.ryan.excel.model.dto.LocalUserImportDTO;
import com.ryan.excel.model.entity.LocalUserEntity;
import com.ryan.excel.service.LocalUserService;
import com.ryan.excel.utils.ImportUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/22 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
@Service
public class LocalUserServiceImpl extends ServiceImpl<LocalUserDao, LocalUserEntity> implements LocalUserService  {

    @Override
    public List<LocalUserImportDTO> importLocalUser(MultipartFile multipartFile) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ImportUtil excelImportUtil = new ImportUtil(multipartFile, 1, 0);
        List<List<String>> headList = new ArrayList<>();
        List<String> oneHead = new ArrayList<>();
        oneHead.add("模板");
        List<String> twoHead = new ArrayList<>();
        twoHead.add("姓名");
        twoHead.add("手机");
        headList.add(oneHead);
        headList.add(twoHead);
        List<LocalUserImportDTO> dataList = excelImportUtil.getDataListAndValidHead(LocalUserImportDTO.class, headList);
        return dataList;
    }
}
