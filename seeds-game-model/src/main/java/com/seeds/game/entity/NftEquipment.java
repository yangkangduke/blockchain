package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * nft装备
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@TableName("z_equipment_nft")
@ApiModel(value = "NftEquipment", description = "nft装备")
@Data
public class NftEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("token id")
    private Long tokenId;

    @ApiModelProperty("nft 名称")
    private String name;

    @ApiModelProperty("NFT address")
    private String mintAddress;

    @ApiModelProperty("NFT 关联账号：托管时候不为空；")
    private String tokenAddress;

    @ApiModelProperty("NFT拥有者地址")
    private String owner;

    @ApiModelProperty("武器NFT是否已经生成 0：未生产  1：已生成")
    private Integer nftGenerated;

    @ApiModelProperty("mint时间")
    private Long mintTime;

    @ApiModelProperty("mint交易地址")
    private String mintTrx;

    @ApiModelProperty("更新时间")
    private Long updateTime;

    @ApiModelProperty("是否在售卖  0：下架 1：上架")
    private Integer onSale;

    @ApiModelProperty("订单ID")
    private String orderId;

    @ApiModelProperty("是否删除  0：未删除  1：已删除")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    @ApiModelProperty("删除时间")
    private Long deleteTime;

    @ApiModelProperty("是否托管状态  0：未托管  1:已托管")
    private Integer isDeposit;

    @ApiModelProperty("取消时间")
    private Long cancelTime;

    @ApiModelProperty("拍卖ID")
    private Long auctionId;

}
