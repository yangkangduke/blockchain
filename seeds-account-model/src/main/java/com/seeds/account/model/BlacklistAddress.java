package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.account.enums.CommonStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Ethereum黑地址
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@TableName("ac_blacklist_address")
@ApiModel(value = "BlacklistAddress对象", description = "Ethereum黑地址")
@Data
@Builder
public class BlacklistAddress implements Serializable {

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

    @ApiModelProperty(value = "1：ETH 3：TRON")
    private Integer chain;

    @ApiModelProperty(value = "1；冲币 2：提币")
    private Integer type;

    @ApiModelProperty("address")
    private String address;

    @ApiModelProperty("reason")
    private String reason;

    @ApiModelProperty(value = "1:启用 2：停用")
    private Integer status;


}