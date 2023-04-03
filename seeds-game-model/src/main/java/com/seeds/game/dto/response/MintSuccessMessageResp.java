package com.seeds.game.dto.response;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/3
 */
@Data
public class MintSuccessMessageResp {
    private Long id;

    @ApiModelProperty("token id")
    private Long tokenId;
    private String name;

    @ApiModelProperty("NFT address")
    private String mintAddress;

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

}
