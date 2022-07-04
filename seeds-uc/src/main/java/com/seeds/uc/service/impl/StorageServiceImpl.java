package com.seeds.uc.service.impl;

import com.seeds.uc.exceptions.StorageException;
import com.seeds.uc.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author allen
 * @email allen.hua.ai@gmail.com
 * @date 2020/8/25
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {
    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(@Value("${file.store.location}") String location) {
        this.rootLocation = Paths.get(location);
    }

    @PostConstruct
    public void init() throws IOException {
        FileUtils.forceMkdir(this.rootLocation.toFile());
    }

    @Override
    public void store(MultipartFile multipartFile) throws IOException {

    }

    @Override
    public void store(String filename, InputStream inputStream) throws IOException {
        String fileName = StringUtils.cleanPath(filename);
        try {
            if (fileName.contains("..")) {
                throw new StorageException("Cannot store file with a relative path outside current directory: " + fileName);
            }
            Files.copy(inputStream, this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            inputStream.close();
        }
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = this.rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read this file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read this file: " + filename, e);
        }
    }
}
