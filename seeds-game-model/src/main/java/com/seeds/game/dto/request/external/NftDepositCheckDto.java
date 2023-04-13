package com.seeds.game.dto.request.external;

import lombok.Data;

/**
 * 托管前 向游戏发起校验dto
 *
 * @author: hewei
 * @date 2023/4/13
 */
@Data
public class NftDepositCheckDto {
    /**
     * NFT序列号
     */
    private long autoId;
    /**
     * NFT模板ID
     */
    private int confId;
    /**
     * NFT链上地址
     */
    private String tokenAddress;
    //------NFT皮肤属性-------
    /**
     * 胜利总次数（皮肤时必传）
     */
    private int win;
    /**
     * 失败总次数（皮肤时必传）
     */
    private int defeat;

    /**
     * 最大连胜次数（皮肤时必传）
     */
    private int seqWin;
    /**
     * 最大连败次数（皮肤时必传）
     */
    private int seqDefeat;
    //------NFT装备属性-------
    /**
     * 稀有属性ID（装备时必传）
     */
    private int rarityAttr;
    /**
     * 装备耐久度（装备时必传）
     */
    private int durability;

}
