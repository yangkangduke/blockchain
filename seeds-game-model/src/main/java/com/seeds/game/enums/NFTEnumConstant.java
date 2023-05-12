package com.seeds.game.enums;

import lombok.Getter;

import java.util.Arrays;

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
        MINTED(3, "Minted"),
        MINTING(4, "Minting");

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

        BACKPACK(0, "My Package");
        private final int code;
        private final String desc;

        NFTTransEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    @Getter
    public enum NFTDescEnum {

        SEEDS_EQUIP("This is the NFT of the Seeds equipment.");

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

    @Getter
    public enum NftEventOptEnum {

        SUCCESS(1, "SUCCESS"),
        CANCEL(2, "CANCEL");

        private int code;
        private String desc;

        NftEventOptEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    @Getter
    public enum EquMetadataAttrEnum {

        CONFIGID("ConfigId"),
        AUTOID("AutoId"),
        DURABILITY("Durability"),
        RAREATTRIBUTE("RareAttribute"),
        QUALITY("Quality");

        private String name;

        EquMetadataAttrEnum(String name) {
            this.name = name;
        }
    }

    @Getter
    public enum NftDepositCheckEnum {
        AUTO_ID_ERROR(1122, "autoId invalid"),
        CONF_ID_ERROR(1123, "confId invalid"),
        TOKEN_ID_ERROR(1124, "tokenId invalid"),
        NFT_NOT_FOUND(1104, "NFT not exist"),
        CONF_ID_NOT_MATCH(1108, "confId does not match"),
        TOKEN_ID_NOT_MATCH(1109, "tokenId does not match"),
        NFT_NOT_AVAILABLE(1120, "The NFT is unavailable"),
        OPERATE_DATA_NOT_FOUND(1105, "NFT last operation record does not exist"),
        NFT_ATT_NOT_FOUND(1107, "The NFT attribute does not exist"),
        NFT_ALREADY_IN_GAME(1708, "The NFT is already in the game");
        private int code;
        private String message;

        NftDepositCheckEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }
        public static String getMessage(int code) {
            return Arrays.stream(values())
                    .filter(i -> i.getCode() == code)
                    .findFirst()
                    .map(a -> a.getMessage())
                    .orElse(null);
        }
    }

    @Getter
    public enum TokenNamePreEnum {

        SEQN("Seqn #");
        private String name;

        TokenNamePreEnum(String name) {
            this.name = name;
        }
    }


}
