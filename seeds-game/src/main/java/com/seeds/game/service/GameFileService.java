package com.seeds.game.service;


import javax.servlet.http.HttpServletResponse;

/**
 * 系统文件
 *
 * @author hang.yu
 * @date 2022/8/02
 */
public interface GameFileService {

    /**
     * 下载文件
     * @param response 响应头
     * @param bucketName 桶的名称
     * @param objectName 对象名称
     */
    void download(HttpServletResponse response, String bucketName, String objectName);

    /**
     * 获取文件url
     * @param objectName 对象名称
     * @return 文件url
     */
    String getFileUrl(String objectName);

}
