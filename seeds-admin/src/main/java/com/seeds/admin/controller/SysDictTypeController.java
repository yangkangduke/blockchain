package com.seeds.admin.controller;

import com.seeds.admin.service.SysDictTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典类型
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@Api("字典类型")
@RestController
@RequestMapping("/dictType")
public class SysDictTypeController {

    @Autowired
    private SysDictTypeService sysDictTypeService;



}
