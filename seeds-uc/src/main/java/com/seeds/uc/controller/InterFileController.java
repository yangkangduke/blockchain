package com.seeds.uc.controller;

import com.seeds.uc.mapper.FileMapper;
import com.seeds.uc.model.File;
import com.seeds.uc.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/9/3
 */
@Slf4j
@RestController
@RequestMapping("/uc-internal/file/")
public class InterFileController {

    @Autowired
    private FileMapper fileMapper;
//    @Autowired
//    private StorageService storageService;

//    @GetMapping("download")
//    public ResponseEntity<Resource> downloadFile(@RequestParam("uuid") String uuid) {
//        File file = fileMapper.selectByUuid(uuid);
//
//        Resource resource = storageService.loadAsResource(uuid);
//
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
}
