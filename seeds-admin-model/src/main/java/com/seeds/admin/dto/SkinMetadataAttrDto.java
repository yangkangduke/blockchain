package com.seeds.admin.dto;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/27
 */
@Data
public class SkinMetadataAttrDto {
    private Integer tokenId;
    private Long configId;
    private Long autoId;
    private String rarity;// 稀有度
    private String feature;//图片特征主题
    private String color;// 图片特征肤色
    private String accessories;//图片特征主要配饰
    private String decorate;//图片特征装饰物
    private String other;//图片特征其他
    private String hero;//对应英雄
    private String skin;//对应皮肤
    private Integer win;
    private Integer defeat;
    private Integer seqWin;
    private Integer seqDefeat;
    private Integer seqKill;
    private Integer killPlayer;
    private Integer killNpc;
    private Integer killedByPlayer;
    private Integer killedByNpc;
}
