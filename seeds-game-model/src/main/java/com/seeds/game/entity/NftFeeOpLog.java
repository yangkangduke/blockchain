package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * nft手续费记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-05-09
 */
@TableName("z_fee_op_log")
@ApiModel(value = "NftFeeOpLog", description = "nft手续费记录")
@Data
public class NftFeeOpLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发送账号")
    private String fromAddress;

    @ApiModelProperty("接受账号")
    private String toAddress;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改时间")
    private Long updateTime;

    @ApiModelProperty("交易hash")
    private String txHash;

}
