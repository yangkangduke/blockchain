package com.seeds.admin.dto;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/2/20
 */
@Data
public class SysNFTAttrDto {

    /**
     * 游戏方confId
     */
    private Long confId;
    /**
     * 图片名字
     */
    private String pictureName;
    /**
     * 稀有度
     */
    private String rarity;
    /**
     * 图片特征-主题
     */
    private String feature;
    /**
     * 图片特征-主要配饰
     */
    private String accessories;

    /**
     * 图片特征-肤色
     */
    private String color;

    /**
     * 图片特征-装饰物
     */
    private String decorate;
    /**
     * 对应英雄名字
     */
    private String hero;
    /**
     * 对应皮肤名字
     */
    private String skin;

    /**
     * other
     */
    private String other;
}
