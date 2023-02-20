package com.seeds.admin.dto.response;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yang.deng
 * @date 2023/2/20
 */
@Data
@ApiModel(value = "SysNftPicResp", description = "NFT图片管理")
public class SysNftPicResp {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("文件名")
    private String url;

    @ApiModelProperty("预览")
    private String description;

    @ApiModelProperty("属性")
    private String jsonUrl;

    @ApiModelProperty("confId")
    private Long confId;

    @ApiModelProperty("autoId")
    private Long autoId;

    @ApiModelProperty("tokenAddress")
    private String tokenAddress;

    @ApiModelProperty("上传时间")
    private Long createdAt;
}
