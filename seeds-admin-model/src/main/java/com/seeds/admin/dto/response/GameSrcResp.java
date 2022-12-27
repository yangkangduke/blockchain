package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: hewei
 * @date 2022/12/17
 */
@ApiModel(value = "GameSrcResp", description = "游戏资源信息")
@Data
public class GameSrcResp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty("资源类型 1 主页视频；2 安装包；3 补丁包")
    private Integer srcType;

    @ApiModelProperty("资源类型名称")
    private String srcTypeName;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("path")
    private String filePath;

    @ApiModelProperty("操作系统 1 Windows; 2 mac")
    private Integer os;

    @ApiModelProperty("操作系统名称")
    private String osName;

    @ApiModelProperty("资源大小")
    private String srcSize;

    @ApiModelProperty("日本下载链接")
    private String japanUrl;


    @ApiModelProperty("欧洲下载链接")
    private String euUrl;


    @ApiModelProperty("美国下载链接")
    private String usUrl;


    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("上传时间")
    private Long createdAt;


    @ApiModelProperty("上传人")
    private String uploader;


    @ApiModelProperty("更新时间")
    private Long updatedAt;


    @ApiModelProperty("更新人")
    private String updatedBy;


    @ApiModelProperty("删除标记  1：启动   0：禁用")
    private Integer status;

    @ApiModelProperty(value = "为了便于构造树形结构虚拟的id,不是数据库记录的id")
    private Integer vId;

    @ApiModelProperty(value = "为了便于构造树形结构虚拟的id")
    private Integer vParentId;

    private List<GameSrcResp> childList;
}
