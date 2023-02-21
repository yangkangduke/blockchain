package com.seeds.admin.dto.response;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传记录
 *
 * @author yang.deng
 * @date 2023/2/20
 */
@Data
@ApiModel(value = "SysNftPicUpHisResp", description = "文件上传记录")
public class SysNftPicUpHisResp {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("说明")
    private String remark;

    @ApiModelProperty("平台：1,Magic Eden  2,Seeds")
    private Integer platform;

    @ApiModelProperty("NFT类型：1:skin , 2:equip")
    private Integer nftType;
}
