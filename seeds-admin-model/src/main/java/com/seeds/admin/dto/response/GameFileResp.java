package com.seeds.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2022/12/17
 */
@Data
@ApiModel(value = "GameFileResp", description = "S3资源列表信息")
public class GameFileResp {

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "key,不需要展示")
    private String key;

    @ApiModelProperty(value = "桶，不需要展示")
    private String bucketName;

    @ApiModelProperty(value = "文件大小")
    private String size;

    @ApiModelProperty(value = "上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String uploadTime;

    @ApiModelProperty(value = "url")
    private String japanURL;

    @ApiModelProperty(value = "euURL")
    private String euURL;

    @ApiModelProperty(value = "usURL")
    private String usURL;
}
