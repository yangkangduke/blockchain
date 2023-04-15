package com.seeds.game.dto.request;

import lombok.Data;

@Data
public class NftAttributesDto {

    /**NFT皮肤------------ ---------**/
    /**
     * 获胜次数
     */
    private Integer win;
    /**
     * 失败次数
     */
    private Integer defeat;
    /**
     * 最大连胜次数
     */
    private Integer seqWin;
    /**
     * 最大连败次数
     */
    private Integer seqDefeat;
    /**
     * 最大连杀次数
     */
    private Integer seqKill;
    /**
     * 击杀玩家次数
     */
    private Integer killPlayer;
    /**
     * 击杀NPC次数
     */
    private Integer killNpc;
    /**
     * 被玩家击杀次数
     */
    private Integer killedByPlayer;
    /**
     * 被NPC击杀次数
     */
    private Integer killedByNpc;
    /**
     * 稀有度
     */
    private String rarity;

    /**NFT图纸------------ ---------**/
    /**
     * 稀有属性
     */
    private Integer rarityAttr;
    /**
     * 耐久
     */
    private Integer durability;
}
