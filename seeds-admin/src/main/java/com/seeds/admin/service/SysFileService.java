package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.response.SysFileResp;
import com.seeds.admin.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 系统文件
 *
 * @author hang.yu
 * @date 2022/8/02
 */
public interface SysFileService extends IService<SysFileEntity> {

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

}
