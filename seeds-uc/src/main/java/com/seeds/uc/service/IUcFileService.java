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
     * 下载文件
     * @param response 响应头
     * @param objectName 对象名称
     */
    void download(HttpServletResponse response, String objectName);

    /**
     * 获取文件
     * @param objectName 对象名称
     * @return 文件链接
     */
    String getFile(String objectName);

    /**
     * 根据id删除文件信息
     * @param fileId 文件id
     */
    void delete(Long fileId);

    /**
     * 上传
     * @param file
     * @return
     */
    GenericDto<UcFileResp> upload(MultipartFile file);
}
