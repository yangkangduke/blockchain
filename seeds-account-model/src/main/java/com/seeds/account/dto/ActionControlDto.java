package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import jnr.ffi.annotations.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 操作控制
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActionControlDto implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty("类型，系统默认account")
    private String type;

    @ApiModelProperty("Operation name")
    private String key;

    @ApiModelProperty("操作名称")
    private String name;

    @ApiModelProperty("true或者其他,用于操作校验的字段")
    private String value;

    @ApiModelProperty("评语")
    private String comments;

    @ApiModelProperty("状态 1：启用， 2：停用")
    private Integer status;
}
