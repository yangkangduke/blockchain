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
import com.seeds.admin.service.AdminCacheService;
import com.seeds.admin.service.SysFileService;
import com.seeds.common.web.oss.service.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * 系统文件
 *
 * @author hang.yu
 * @date 2022/8/02
 */
@Slf4j
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFileEntity> implements SysFileService {

    @Value("${admin.oss.file.expires:1}")
    private Integer expires;

    @Value("${admin.oss.bucket.name:admin}")
    private String bucketName;

    @Autowired
    private OssTemplate template;

    @Autowired
    private AdminCacheService adminCacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileResp upload(MultipartFile file, String type) {
        // 上传文件到oss
        String originalFilename = file.getOriginalFilename();
        String objectName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(originalFilename);
        SysFileEntity sysFile = new SysFileEntity();
        try (InputStream inputStream = file.getInputStream()) {
            template.putObject(bucketName, objectName, inputStream, file.getSize(), file.getContentType());
            // 记录文件信息
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
        return res;
    }

    @Override
    public void download(HttpServletResponse response, String objectName) {
        try (S3Object s3Object = template.getObject(bucketName, objectName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        }
        catch (Exception e) {
            log.error("文件下载失败，objectName={}", objectName);
        }
    }

    @Override
    public String getFile(String objectName) {
        // 先从Redis中获取
        String url = adminCacheService.getFileUrlByObjectName(objectName);
        if (StringUtils.isNotBlank(url)) {
            return url;
        }
        try {
            url = template.getObjectURL(bucketName, objectName, expires);
            if (StringUtils.isNotBlank(url)) {
                adminCacheService.putFileUrlByObjectName(objectName, url, expires);
            }
        }
        catch (Exception e) {
            log.error("获取文件失败，objectName={}", objectName);
        }
        return url;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long fileId) {
        SysFileEntity sysFile = getById(fileId);
        if (sysFile == null) {
            return;
        }
        try {
            template.removeObject(bucketName, sysFile.getObjectName());
            removeById(fileId);
        }
        catch (Exception e) {
            log.error("删除文件失败，objectName={}", sysFile.getObjectName());
            throw new GenericException("File delete failed");
        }
    }

}