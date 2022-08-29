package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.request.UcFileResp;
import com.seeds.uc.model.UcFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件管理表 服务类
 * </p>
 *
 * @author yk
 * @since 2022-08-05
 */
public interface IUcFileService extends IService<UcFile> {

    /**
     * 根据id删除文件信息
     */
    Boolean deleteFile(Long id) throws Exception;

    /**
     * 上传
     * @param file
     * @return
     */
    GenericDto<UcFileResp> upload(MultipartFile file);

    /**
     * 读取文件
     * @param bucket 桶名称
     * @param objectName 文件名称
     * @param response 输出流
     */
    void getFile(String bucket, String objectName, HttpServletResponse response);
}
