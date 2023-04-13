package com.seeds.game.enums;

import lombok.Getter;

/**
 * NFT常量
 *
 * @author: hewei
 * @date 2023/3/23
 */
public class NFTEnumConstant {


    /**
     * nft event 类型
     * <p>
     * 1.mint 2.compound 3.other
     */
    @Getter
    public enum NFTEventType {
        MINT(1, "MINT"),
        COMPOUND(2, "COMPOUND"),
        OTHER(3, "OTHER");

        private final int code;
        private final String desc;

        NFTEventType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * nft event 状态枚举
     */
    @Getter
    public enum NFTEventStatus {
        PENDING(1, "Pending"),
        CANCELLED(2, "Cancelled"),
        MINTED(3, "Minted");

        private final int code;
        private final String desc;

        NFTEventStatus(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * nft 分配、转移目的地枚举
     */
    @Getter
    public enum NFTTransEnum {

        BACKPACK("backpack");

        private final String desc;

        NFTTransEnum(String desc) {
            this.desc = desc;
        }
    }

    @Getter
    public enum NFTDescEnum {

        SEEDS_EQUIP("This is the NFT of the seeds equipment.");

        private final String desc;

        NFTDescEnum(String desc) {
            this.desc = desc;
        }
    }

    /**
     * nft 状态 地枚举
     */
    @Getter
    public enum NFTStateEnum {
        BURNED(1, "BURNED"),
        LOCK(2, "LOCK"),   // 作为合成材料被临时锁定
        DEPOSITED(3, "DEPOSITED"), // 托管给平台
        UNDEPOSITED(4, "UNDEPOSITED"),// 解除托管
        ON_SHELF(5, "ON SHELF"), // 固定价格售卖中
        ON_ACTION(6, "ON AUCTION"), // 拍卖中
        IN_SETTLEMENT(7, "IN_SETTLEMENT"), // 结算中（拍卖到期后到成交之前的状态）
        ;
        private final int code;
        private final String desc;

        NFTStateEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

    /**
     * nft 类型枚举
     */
    @Getter
    public enum NftTypeEnum {

        EQUIP(1, "装备"),
        PROPS(2, "道具"),
        HERO(3, "英雄");

        private int code;
        private String desc;

        NftTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


}
