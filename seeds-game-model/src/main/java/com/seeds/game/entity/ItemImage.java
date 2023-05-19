package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author: hewei
 * @date 2023/4/12
 */
@TableName("ga_item_image")
@Data
public class ItemImage {
    private Long id;
    // 道具分类名称
    private String name;
    // 图片地址
    private String imgUrl;
    // 分类id
    private Long itemId;
    // 类别 1装备 2道具 3英雄
    private Integer type;
}
