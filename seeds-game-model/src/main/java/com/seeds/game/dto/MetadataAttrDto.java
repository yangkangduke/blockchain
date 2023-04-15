package com.seeds.game.dto;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/15
 */
@Data
public class MetadataAttrDto {
    // "Seqn #"+tokenId
    private Integer tokenId;
    private String name;
    private String image;
    private Long configId;
    private Long autoId;
    private Integer durability;
    // 稀有属性id
    private Integer rareAttribute;
    // 装备等级
    private Integer quality;
}
