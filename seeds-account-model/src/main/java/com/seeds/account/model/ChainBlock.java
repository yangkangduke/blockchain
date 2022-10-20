package com.seeds.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.account.enums.CommonStatus;
import com.seeds.common.enums.Chain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Ethereum块跟踪
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@TableName("ac_chain_block")
@ApiModel(value = "ChainBlock对象", description = "Ethereum块跟踪")
@Data
@Builder
public class ChainBlock implements Serializable {

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
    private Chain chain;

    @ApiModelProperty("block number")
    private Long blockNumber;

    @ApiModelProperty("block hash")
    private String blockHash;

    @ApiModelProperty("block timestamp")
    private Long blockTime;

    @ApiModelProperty("parent block hash")
    private String parentHash;

    private CommonStatus status;

}
