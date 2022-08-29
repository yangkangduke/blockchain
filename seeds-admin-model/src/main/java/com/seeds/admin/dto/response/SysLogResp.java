package com.seeds.admin.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class SysLogResp {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    @ApiModelProperty("操作人")
    private Long operator;

    @ApiModelProperty("操作人名字")
    private String operatorName;

    @ApiModelProperty("具体操作")
    private String operation;

    @ApiModelProperty("执行的方法")
    private String method;

    @ApiModelProperty("请求参数")
    private String params;


    @ApiModelProperty("真实IP地址")
    private String ip;

    @ApiModelProperty("操作时间")
    private Long createdAt;
}