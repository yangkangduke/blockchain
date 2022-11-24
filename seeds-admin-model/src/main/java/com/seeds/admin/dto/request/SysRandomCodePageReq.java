package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRandomCodePageReq", description = "随机码分页请求入参")
public class SysRandomCodePageReq extends PageReq {

    @ApiModelProperty(value = "类型 1：邀请码")
    private Integer type;

    @ApiModelProperty(value = "长度")
    private Integer length;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @ApiModelProperty(value = "状态  0：正常  1：生成中  2：生成失败  3：导出中  6：导出失败")
    private Integer status;

}
