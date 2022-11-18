package com.seeds.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.web3j.abi.datatypes.Int;

import java.io.Serializable;

/**
 * 系统使用的钱包
 *
 * @author yk
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemWalletAddressUpdateDto implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "地址", required = true)
    private String address;
    @ApiModelProperty(value = "1 eth , 3 tron", required = true)
    private Integer chain;
    @ApiModelProperty(value = "类型", required = true)
    private Integer type;
    @ApiModelProperty(value = "标记", required = true)
    private String tag;
    @ApiModelProperty(value = "备注", required = true)
    private String comments;
    @ApiModelProperty(value = "1 启用 , 2 停用", required = true)
    private Integer status;

}
