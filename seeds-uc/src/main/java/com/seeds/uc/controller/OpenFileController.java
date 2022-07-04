package com.seeds.uc.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.mapper.FileMapper;
import com.seeds.uc.model.File;
import com.seeds.uc.service.StorageService;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */
@Slf4j
@RestController
@RequestMapping("/uc/file")
public class OpenFileController {
    @Autowired
    private StorageService storageService;

    @Autowired
    private FileMapper fileMapper;

    @PostMapping("upload")
    public GenericDto<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // TODO 检查文件的格式，大小

        Long uid = UserContext.getCurrentUserId();

        // 将文件持久化
        String uuid = RandomUtil.getUUID();
        storageService.store(uuid, multipartFile.getInputStream());

        // 存入DB
        fileMapper.insert(File.builder()
                .filename(multipartFile.getOriginalFilename())
                .uid(uid)
                .createdAt(System.currentTimeMillis())
                .uuid(uuid)
                .build());
        return GenericDto.success(uuid);
    }
}