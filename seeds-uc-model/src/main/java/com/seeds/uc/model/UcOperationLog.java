package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * uc操作日志表
 * </p>
 *
 * @author hewei
 * @since 2023-01-12
 */
@Data
@TableName("uc_operation_log")
@ApiModel(value = "UcOperationLog对象", description = "uc操作日志表")
public class UcOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户")
    private String user;

    @ApiModelProperty("用户IP")
    private String userIp;

    @ApiModelProperty("服务器编号")
    private String serverNo;

    @ApiModelProperty("操作描述")
    private String opDesc;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("请求类型")
    private String method;

    @ApiModelProperty("携带的参数")
    private String params;

    @ApiModelProperty("操作时间")
    private Long opTime;

    @ApiModelProperty("执行耗时")
    private Long spendTime;

    @ApiModelProperty("返回的结果")
    private String result;

    @ApiModelProperty("操作状态")
    private String opStatus;

    @ApiModelProperty("创建时间")
    private Long createdAt;
}
