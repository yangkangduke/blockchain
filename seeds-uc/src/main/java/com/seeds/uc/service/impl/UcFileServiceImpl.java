package com.seeds.uc.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.common.web.oss.service.OssTemplate;
import com.seeds.uc.dto.request.UcFileResp;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.mapper.UcFileMapper;
import com.seeds.uc.model.UcFile;
import com.seeds.uc.service.IUcFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 文件管理表 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-08-05
 */
@Service
@Slf4j
@Transactional
public class UcFileServiceImpl extends ServiceImpl<UcFileMapper, UcFile> implements IUcFileService {

    @Value("${uc.oss.file.expires:1}")
    private Integer expires;

    @Value("${uc.oss.bucket.name:admin}")
    private String bucketName;

    @Autowired
    private OssTemplate template;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private FileProperties properties;
    @Autowired
    private FileTemplate fileTemplate;

    @Override
    public void download(HttpServletResponse response, String objectName) {
        try (S3Object s3Object = template.getObject(bucketName, objectName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(objectName, "UTF-8"));
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        }
        catch (Exception e) {
            log.error("文件下载失败，objectName={}", objectName);
        }
    }

    @Override
    public String getFile(String objectName) {
        // 先从Redis中获取
        String url = cacheService.getFileUrlByObjectName(objectName);
        if (StringUtils.isNotBlank(url)) {
            return url;
        }
        try {
            url = template.getObjectURL(bucketName, objectName, expires);
            if (StringUtils.isNotBlank(url)) {
                cacheService.putFileUrlByObjectName(objectName, url, expires);
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
        UcFile ucFile = getById(fileId);
        if (ucFile == null) {
            return;
        }
        try {
            template.removeObject(bucketName, ucFile.getObjectName());
            removeById(fileId);
        }
        catch (Exception e) {
            log.error("删除文件失败，objectName={}", ucFile.getObjectName());
            throw new GenericException("File delete failed");
        }
    }

    /**
     * 上传
     * @param file
     * @return
     */
    @Override
    public GenericDto<UcFileResp> upload(MultipartFile file) {
        String objectName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
        UcFileResp ucFileResp = UcFileResp.builder()
                .bucketName(properties.getBucketName())
                .objectName(objectName)
                .url(String.format("/uc/file/%s/%s", properties.getBucketName(), objectName))
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            fileTemplate.putObject(properties.getBucketName(), objectName, inputStream, file.getContentType());
            // 文件管理数据记录,收集管理追踪文件
            Long id = fileLog(file, objectName);
            ucFileResp.setFileId(id);
        }
        catch (Exception e) {
            log.error("上传失败", e);
            return GenericDto.failure(e.getLocalizedMessage(),500);
        }
        return GenericDto.success(ucFileResp);
    }

    /**
     * 文件管理数据记录,收集管理追踪文件
     * @param file 上传文件格式
     * @param objectName 对象名
     */
    private Long fileLog(MultipartFile file, String objectName) {
        long currentTimeMillis = System.currentTimeMillis();
        UcFile ucFile = UcFile.builder()
                .objectName(objectName)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .createdAt(currentTimeMillis)
                .updatedAt(currentTimeMillis)
                .type(FileUtil.extName(file.getOriginalFilename()))
                .bucketName(properties.getBucketName())
                .build();
        this.save(ucFile);
        return ucFile.getId();
    }
}
