package com.seeds.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author: hewei
 * @date 2023/4/27
 */
public class SkinNftEnums {

    // 皮肤初始参考价为100U
    @Getter
    public enum SkinNftPrice {

        SKIN_NFT_PRICE(100);

        private final int price;

        SkinNftPrice(int price) {
            this.price = price;
        }
    }

    @Getter
    public enum SkinNftListStateEnum {
        NO_LIST(0, "NO_LIST"),
        LISTED(1, "LISTED"),
        ;
        private final int code;
        private final String desc;

        SkinNftListStateEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

    @Getter
    public enum AutoIdApplyStateEnum {
        NO_APPLY(0, "NO_APPLY"),
        APPLYING(1, "APPLYING"),
        APPLIED(2, "APPLIED"),
        ;

        private final int code;
        private final String desc;

        AutoIdApplyStateEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

    @Getter
    public enum SkinMintStateEnum {
        PENDING(0, "PENDING"),
        MINTING(1, "MINTING"),
        MINT_FAILED(2, "MINT_FAILED"),
        MINTED(3, "MINTED");
        private final int code;
        private final String desc;

        SkinMintStateEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }


    @Getter
    public enum NftAttrEnum {
        CONFIGID("ConfigId"),
        AUTOID("AutoId"),
        //稀有度
        RARITY("Rarity"),
        // 图片特征-主题
        FEATURE("Feature"),
        //    图片特征-肤色
        COLOR("Color"),
        //    图片特征-主要配饰
        ACCESSORIES("Accessories"),
        //    图片特征-装饰物
        DECORATE("Decorate"),
        //    图片特征-其他
        OTHER("Other"),
        //    对应英雄
        HERO("Hero"),
        //    对应皮肤
        SKIN("Skin"),
        WIN("Win"),
        DEFEAT("Defeat"),
        SEQWIN("SeqWin"),
        SEQDEFEAT("SeqDefeat"),
        SEQKILL("SeqKill"),
        KILLPLAYER("KillPlayer"),
        KILLNPC("KillNpc"),
        KILLEDBYPLAYER("KilledByPlayer"),
        KILLEDBYNPC("killedByNpc"),
        ;

        @EnumValue
        private final String name;

        NftAttrEnum(String name) {
            this.name = name;
        }
    }

}
