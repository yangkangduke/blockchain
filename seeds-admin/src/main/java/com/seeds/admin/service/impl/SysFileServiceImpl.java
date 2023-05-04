package com.seeds.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.response.SysFileResp;
import com.seeds.admin.entity.SysFileEntity;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysFileMapper;
import com.seeds.admin.service.SysFileService;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 系统文件
 *
 * @author hang.yu
 * @date 2022/8/02
 */
@Slf4j
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFileEntity> implements SysFileService {

    @Value("${service.url.gateway:127.0.0.1:9999}")
    private String baseUrl;

    @Autowired
    private FileProperties properties;

    @Autowired
    private FileTemplate template;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileResp upload(InputStream inputStream, String fileName, String type, Long size) {
        // 上传文件到oss
        String bucketName = properties.getBucketName();
        String objectName = type + "/" + IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(fileName);
        SysFileEntity sysFile = new SysFileEntity();
        try {
            template.putObject(bucketName, objectName, inputStream);
            // 记录文件信息
            sysFile.setFileSize(size);
            sysFile.setObjectName(objectName);
            sysFile.setBucketName(bucketName);
            sysFile.setFileName(fileName);
            sysFile.setType(type);
            save(sysFile);
        }
        catch (Exception e) {
            log.error("文件上传失败，fileName={}, message={}", fileName, e.getMessage());
            throw new GenericException("File upload failed");
        }
        SysFileResp res = new SysFileResp();
        res.setFileId(sysFile.getId());
        res.setObjectName(objectName);
        res.setBucketName(properties.getBucketName());
        res.setUrl(baseUrl + String.format("/admin/public/file/download/%s?objectName=%s", properties.getBucketName(), objectName));
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileResp upload(MultipartFile file, String type) {
        // 上传文件到oss
        String bucketName = properties.getBucketName();
        String originalFilename = file.getOriginalFilename();
        String objectName = type + "/" + IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(originalFilename);
        SysFileEntity sysFile = new SysFileEntity();
        try (InputStream inputStream = file.getInputStream()) {
            template.putObject(bucketName, objectName, inputStream, file.getContentType());
            // 记录文件信息
            sysFile.setFileSize(file.getSize());
            sysFile.setObjectName(objectName);
            sysFile.setBucketName(bucketName);
            sysFile.setFileName(originalFilename);
            sysFile.setType(type);
            save(sysFile);
        }
        catch (Exception e) {
            log.error("文件上传失败，fileName={}", originalFilename);
            throw new GenericException("File upload failed");
        }
        SysFileResp res = new SysFileResp();
        res.setFileId(sysFile.getId());
        res.setObjectName(objectName);
        res.setBucketName(properties.getBucketName());
        res.setUrl(getFileUrl(objectName));
        return res;
    }

    @Override
    public void download(HttpServletResponse response, String bucketName, String objectName) {
        try (S3Object s3Object = template.getObject(bucketName, objectName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), "UTF-8"));
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件下载失败，objectName={}", objectName);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        return baseUrl + String.format("/admin/public/file/download/%s?objectName=%s", properties.getBucketName(), objectName);
    }

    @Override
    public String getNftFileUrl(String bucketName, String objectName) {
        return baseUrl + String.format("/admin/public/file/download/%s?objectName=%s", bucketName, objectName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long fileId) {
        SysFileEntity sysFile = getById(fileId);
        if (sysFile == null) {
            return;
        }
        try {
            template.removeObject(properties.getBucketName(), sysFile.getObjectName());
            removeById(fileId);
        }
        catch (Exception e) {
            log.error("删除文件失败，objectName={}", sysFile.getObjectName());
            throw new GenericException("File delete failed");
        }
    }

}
