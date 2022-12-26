package com.seeds.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 游戏资源表
 * @TableName sys_game_source
 */
@TableName(value ="sys_game_source")
@Data
public class SysGameSourceEntity implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源类型 1 主页视频；2 安装包；3 补丁包
     */
    private Integer srcType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 操作系统 1 Windows; 2 mac
     */
    private Integer os;

    /**
     * 资源大小
     */
    private String srcSize;

    /**
     * 日本下载链接
     */
    private String japanUrl;

    /**
     * 欧洲下载链接
     */
    private String euUrl;

    /**
     * 美国下载链接
     */
    private String usUrl;

    // 文件在S3上的路径
    private String s3Path;

    /**
     * 备注
     */
    private String remark;

    /**
     * 上传时间
     */
    private Long createdAt;

    /**
     * 上传人
     */
    private Long createdBy;

    /**
     * 更新时间
     */
    private Long updatedAt;

    private Long updatedBy;

    /**
     * 删除标记  1：启动   0：禁用
     */
    private Integer status;
}
