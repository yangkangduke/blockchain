package com.seeds.uc.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import com.seeds.uc.dto.request.UcFileResp;
import com.seeds.uc.mapper.UcFileMapper;
import com.seeds.uc.model.UcFile;
import com.seeds.uc.service.IUcFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

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

    @Autowired
    private FileProperties properties;
    @Autowired
    private FileTemplate fileTemplate;

    /**
     * 读取文件
     * @param bucket
     * @param objectName
     * @param response
     */
    @Override
    public void getFile(String bucket, String objectName, HttpServletResponse response) {
        try (S3Object s3Object = fileTemplate.getObject(bucket, objectName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        }
        catch (Exception e) {
            log.error("文件读取异常: {}", e.getLocalizedMessage());
        }
    }


    /**
     * 根据id删除文件信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Boolean deleteFileById(Long id) throws Exception {
        UcFile file = this.getById(id);
        fileTemplate.removeObject(properties.getBucketName(), file.getObjectName());
        return this.removeById(id);
    }

    /**
     * 根据名字删除文件信息
     */
    @Override
    public Boolean deleteFileByName(String objectName) throws Exception {
        UcFile file = this.getOne(new QueryWrapper<UcFile>().lambda()
                .eq(UcFile::getObjectName, objectName));
        fileTemplate.removeObject(properties.getBucketName(), file.getObjectName());
        return this.removeById(file.getId());
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
