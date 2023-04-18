package com.seeds.game.service.impl;

import cn.hutool.core.io.IoUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.game.service.GameFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;


/**
 * 系统文件
 *
 * @author hang.yu
 * @date 2022/8/02
 */
@Slf4j
@Service
public class GameFileServiceImpl implements GameFileService {

    @Value("${service.url.gateway:127.0.0.1:9999}")
    private String baseUrl;

    @Autowired
    private FileProperties properties;

    @Autowired
    private FileTemplate fileTemplate;

    @Override
    public void download(HttpServletResponse response, String bucketName, String objectName) {
        try (S3Object s3Object = fileTemplate.getObject(bucketName, objectName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), "UTF-8"));
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        }
        catch (Exception e) {
            log.error("文件下载失败，objectName={}", objectName);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        return baseUrl + String.format("/game/public/file/download/%s?objectName=%s", properties.getBucketName(), objectName);
    }

}
