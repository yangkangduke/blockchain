package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.response.SysFileResp;
import com.seeds.admin.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;


/**
 * 系统文件
 *
 * @author hang.yu
 * @date 2022/8/02
 */
public interface SysFileService extends IService<SysFileEntity> {


    /**
     * 上传文件
     * @param inputStream 文件流
     * @param fileName 文件名
     * @param type 类型
     * @param size 文件大小
     * @return 文件信息
     */
    SysFileResp upload(InputStream inputStream, String fileName, String type, Long size);

    /**
     * 上传文件
     * @param file 文件
     * @param type 类型
     * @return 文件信息
     */
    SysFileResp upload(MultipartFile file, String type);

    /**
     * 下载文件
     * @param response 响应头
     * @param bucketName 桶的名称
     * @param objectName 对象名称
     */
    void download(HttpServletResponse response, String bucketName, String objectName);

    /**
     * 根据id删除文件信息
     * @param fileId 文件id
     */
    void delete(Long fileId);

    /**
     * 获取文件url
     * @param objectName 对象名称
     * @return 文件url
     */
    String getFileUrl(String objectName);

}
