/*
 * Personal Learning Use
 */
package com.ryan.excel.controller;

import com.ryan.excel.model.dto.LocalUserImportDTO;
import com.ryan.excel.service.LocalUserService;
import com.ryan.excel.utils.ExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <P><B>Description: </B>TODO 添加描述</P>
 * Revision Trail: (Date/Author/Description)
 * 2022/11/23 Ryan Huang CREATE
 *
 * @author Ryan Huang
 * @version 1.0
 */
@Api(tags = {"excel测试"})
@Controller
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private LocalUserService localUserService;

    @ApiOperation("导入")
    @PostMapping("/import")
    @ResponseBody
    public List<LocalUserImportDTO> importExcel(@RequestParam("file") MultipartFile multipartFile) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        return localUserService.importLocalUser(multipartFile);
    }

    @ApiOperation("导出")
    @PostMapping("/export")
    @ResponseBody
    public void exportExcel() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        ArrayList<LocalUserImportDTO> list = new ArrayList<>();
        LocalUserImportDTO one = new LocalUserImportDTO();
        one.setUserName("hzc");
        one.setUserPhone(123);
        list.add(one);
        ExportUtil.writeExcel("文件.xlsx", list, LocalUserImportDTO.class, true);
    }


}
