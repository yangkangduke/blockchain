package com.seeds.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.seeds.admin.dto.response.GameFileResp;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.service.GameFileService;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: hewei
 * @date 2022/12/17
 */

@Service
@Slf4j
public class GameFileServiceImpl implements GameFileService {

    @Autowired
    private FileProperties properties;

    @Autowired
    private FileTemplate template;

    @Override
    public GameFileResp upload(MultipartFile file) {

        String bucketName = properties.getBucketName();
        String endpoint = properties.getOss().getEndpoint();
        String originalFilename = file.getOriginalFilename();
        String objectName = "game/" + IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(originalFilename);
        // SysFileEntity sysFile = new SysFileEntity();

        try {
            template.uploadMultipartFileByPart(file, bucketName, objectName);
            // 记录文件信息
//            sysFile.setFileSize(file.getSize());
//            sysFile.setObjectName(objectName);
//            sysFile.setBucketName(bucketName);
//            sysFile.setFileName(originalFilename);
//            sysFile.setType(type);
//            save(sysFile);
        } catch (Exception e) {
            log.error("文件上传失败，fileName={}", originalFilename);
            throw new GenericException("File upload failed");
        }

        GameFileResp res = new GameFileResp();
        // res.setFileId(sysFile.getId());
        res.setObjectName(objectName);
        res.setBucketName(properties.getBucketName());
        res.setUrl(endpoint + "/" + bucketName + "/" + objectName);
        return res;
    }
}
