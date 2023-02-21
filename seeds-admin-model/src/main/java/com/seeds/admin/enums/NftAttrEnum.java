package com.seeds.admin.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum NftAttrEnum {

    //稀有度
    RARITY("rarity"),
    // 图片特征-主题
    FEATURE("feature"),
    //    图片特征-肤色
    COLOR("color"),
    //    图片特征-主要配饰
    ACCESSORIES("accessories"),
    //    图片特征-装饰物
    DECORATE("decorate"),
    //    图片特征-其他
    OTHER("other"),
    //    对应英雄
    HERO("hero"),
    //    对应皮肤
    SKIN("skin");

    @EnumValue
    private final String name;

    NftAttrEnum(String name) {
        this.name = name;
    }
}
