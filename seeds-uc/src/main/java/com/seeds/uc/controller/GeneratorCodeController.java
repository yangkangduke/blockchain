package com.seeds.uc.controller;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author yk
 * @email 819628513@qq.com
 * @date 2022/07/09
 */

public class GeneratorCodeController {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.1.101:3306/seeds_uc", "root", "Seeds123!")
                .globalConfig(builder -> {
                    builder.author("yk") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/yk/generatorcode"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.baomidou.mybatisplus.samples.generator") // 设置父包名
                            .moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/yk/generatorcode")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("uc_currency", "uc_user_account_action_history", "uc_user_account") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
