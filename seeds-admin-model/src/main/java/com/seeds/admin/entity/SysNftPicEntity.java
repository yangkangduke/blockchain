package com.seeds.admin.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.admin.enums.SkinNftEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_nft_pic")
public class SysNftPicEntity extends BaseEntity {

    /**
     * 图片名字
     */
    private String picName;
    /**
     * 图片地址
     */
    @TableField("url")
    private String url;

    /**
     * json文件地址
     */
    @TableField("json_url")
    private String jsonUrl;

    /**
     * 稀有度
     */
    @TableField("rarity")
    private String rarity;

    /**
     * 图片特征-主题
     */
    @TableField("feature")
    private String feature;

    /**
     * 图片特征-主要配饰
     */
    @TableField("accessories")
    private String accessories;

    /**
     * 图片特征-肤色
     */
    @TableField("color")
    private String color;

    /**
     * 图片特征-装饰物
     */
    @TableField("decorate")
    private String decorate;

    /**
     * 对应英雄名字
     */
    @TableField("hero")
    private String hero;

    @TableField("other")
    private String other;
    /**
     * 对应皮肤名字
     */
    @TableField("skin")
    private String skin;

    /**
     * 游戏内物品的唯一id
     */
    @TableField("auto_id")
    private Long autoId;

    /**
     * 游戏那边的conf_id
     */
    @TableField("conf_id")
    private Long confId;

    /**
     * mint 后的nft名字
     */
    @TableField("name")
    private String name;

    @TableField("symbol")
    private String symbol;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * NFT链上的地址
     */
    @TableField("token_address")
    private String tokenAddress;

    @TableField("token_id")
    private Long tokenId;


    /**
     * 未来NFT 发行的平台， 1,Magic Eden  2,Seeds
     */
    @TableField("platform")
    private Integer platform;

    /**
     * NFT的类型：1:skin , 2:equip
     */
    @TableField("nft_type")
    private Integer nftType;

    /**
     * 上传记录id
     */
    @TableField("his_id")
    private Long hisId;

    @TableField("apply_state")
    /**
     * @see SkinNftEnums.AutoIdApplyStateEnum
     *
     */
    private Integer applyState;

    @TableField("mint_state")
    /**
     * @see  SkinNftEnums.SkinMintStateEnum
     *
     */
    private Integer mintState;

    /**
     * @see  SkinNftEnums.SkinNftListStateEnum
     *
     */
    @TableField("list_state")
    private Integer listState;


    @TableField("mint_time")
    private Long mintTime;
}
