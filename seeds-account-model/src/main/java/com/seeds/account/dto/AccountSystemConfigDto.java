package com.seeds.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * 账户系统配置
 * @author yang.deng
 * @data 2022/11/09
 */
@Data
@ApiModel(value = "AccountSystemConfigResp", description = "账户系统配置")
public class AccountSystemConfigDto {

    @ApiModelProperty(value = "配置id")
    @NotNull(message = "Config id can not be empty!")
    private Long id;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "键")
    private String key;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "值列表")
    private List<Map> valueList;

}
