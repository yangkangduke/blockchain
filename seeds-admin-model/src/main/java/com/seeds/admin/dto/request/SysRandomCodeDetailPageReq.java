package com.seeds.admin.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * @author hang.yu
 * @date 2022/11/08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SysRandomCodeDetailPageReq", description = "随机码明细分页请求入参")
public class SysRandomCodeDetailPageReq extends PageReq {

    @ApiModelProperty(value = "批次号")
    @NotBlank(message = "Batch number can not be empty!")
    private String batchNo;

}
