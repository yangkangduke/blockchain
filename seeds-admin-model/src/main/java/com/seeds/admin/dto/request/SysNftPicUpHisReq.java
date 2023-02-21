package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "SysNftPicUpHisReq")
@Data
public class SysNftPicUpHisReq {

    /**
     * NFT的类型：1:skin , 2:equip
     */
    @ApiModelProperty("NFT的类型：1:skin , 2:equip")
    @NotNull(message = "nftType cannot be empty")
    private Integer nftType;
    /**
     * 上传NFT图片时的备注
     */
    @ApiModelProperty("上传NFT图片时的备注")
    private String remark;

    /**
     * 未来NFT 发行的平台， 1,Magic Eden  2,Seeds
     */
    @ApiModelProperty("未来NFT 发行的平台， 1,Magic Eden  2,Seeds")
    @NotNull(message = "platform cannot be empty")
    private Integer platform;
}
