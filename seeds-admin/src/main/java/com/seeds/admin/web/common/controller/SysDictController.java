package com.seeds.admin.web.common.controller;

import com.seeds.admin.web.common.service.SysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hang.yu
 * @date 2022/7/13
 */
@Slf4j
@RestController
@RequestMapping("/dict")
public class SysDictController {

    @Autowired
    private SysDictTypeService sysDictTypeService;



}
