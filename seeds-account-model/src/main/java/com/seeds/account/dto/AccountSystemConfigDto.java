package com.seeds.admin.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 账户系统配置
 * @author yang.deng
 * @data 2022/11/09
 */
@Data
@ApiModel(value = "AccountSystemConfigResp", description = "账户系统配置")
public class AccountSystemConfigResp {

    @ApiModelProperty(value = "配置id")
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
    private List<AccountSystemConfigValue> valueList;

    @Data
    @ApiModel(value = "AccountSystemConfigValue", description = "账户系统配置值")
    public static class AccountSystemConfigValue {

        @ApiModelProperty("需要变更的NFT唯一标识")
        private Map<String, Object> nftId;

    }

}
