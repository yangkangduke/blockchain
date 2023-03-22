package com.seeds.notification.controller;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author hewei
 */

public class GeneratorCodeController {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.1.101:3306/seeds_notification", "root", "Seeds123!")
                .globalConfig(builder -> {
                    builder.author("hewei") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\weihe\\Desktop"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.seeds") // 设置父包名
                            .moduleName("notification") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\weihe\\Desktop")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_notification_user_relation") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_","ga_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
