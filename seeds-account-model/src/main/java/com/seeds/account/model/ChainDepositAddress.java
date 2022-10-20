package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Ethereum地址 充币地址（交易所分配）
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
@TableName("ac_chain_deposit_address")
@ApiModel(value = "ChainDepositAddress对象", description = "Ethereum地址 充币地址（交易所分配）")
@Data
@Builder
public class ChainDepositAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("version")
    private Long version;

    @ApiModelProperty("chain")
    private String chain;

    @ApiModelProperty("address")
    private String address;

    @ApiModelProperty("user id")
    private Long userId;

    private Integer status;

}
