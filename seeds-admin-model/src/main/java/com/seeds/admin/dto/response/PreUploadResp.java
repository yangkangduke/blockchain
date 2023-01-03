package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hewei
 * @date 2022/12/29
 */
@ApiModel(value = "PreUploadResp", description = "游戏资源信息")
@Data
@Accessors(chain = true)
public class PreUploadResp {

    @ApiModelProperty(value = "文件的全路径名称", example = "game/video/test.mp4")
    private String key;

    @ApiModelProperty(value = "预签名URL")
    private String preSignedUrl;

    @ApiModelProperty(value = "上传id")
    private String uploadId;

    @ApiModelProperty(value = "文件访问地址")
    private String cndUrl;
}
