package com.seeds.uc.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */
public interface StorageService {
    void store(MultipartFile multipartFile) throws IOException;

    void store(String filename, InputStream inputStream) throws IOException;

    Path load(String filename);

    Resource loadAsResource(String filename);
}
