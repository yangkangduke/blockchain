package com.seeds.uc.controller;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/7/31
 */
@Slf4j
@RestController
@RequestMapping("/uc-public/test")
public class TestController {
    @Autowired
    CacheService cacheService;

    @GetMapping("/echo")
    public GenericDto<String> createTokenWithUserId() {
        return GenericDto.success("echo");
    }

}
