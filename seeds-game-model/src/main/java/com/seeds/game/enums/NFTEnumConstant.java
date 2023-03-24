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

}
