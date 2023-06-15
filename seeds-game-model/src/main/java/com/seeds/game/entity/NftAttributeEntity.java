package com.seeds.game.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.seeds.game.enums.NftHeroTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * NFT属性
 * </p>
 *
 * @author dengyang
 * @since 2023-03-22
 */
@TableName("ga_nft_attribute")
@ApiModel(value = "NftAttributeEntity对象", description = "NFT属性")
@Data
public class NftAttributeEntity extends BaseEntity {

    @ApiModelProperty("NFT等级")
    private Integer grade;

    @ApiModelProperty("耐久度")
    private Integer durability;

    @ApiModelProperty("当前装备最大耐久度")
    private Integer durabilityConfig;

    @ApiModelProperty("稀有度：1，Normal 2，Rare 3，Epic")
    private Integer rarity;
    /**
     * @see NftHeroTypeEnum
     */
    @ApiModelProperty("英雄类型5种：1,Assassin2,Tank3,Archer4,Warrior5,Support")
    private Integer heroType;

    @ApiModelProperty("token id")
    private Long tokenId;

    @ApiModelProperty("mintAddress")
    private String mintAddress;

    @ApiModelProperty("获胜次数")
    private int victory;

    @ApiModelProperty("失败次数")
    private int lose;

    @ApiModelProperty("最大连胜场数")
    private int maxStreak;

    @ApiModelProperty("最大连败场数")
    private int maxLose;

    @ApiModelProperty("击杀玩家数")
    private int capture;

    @ApiModelProperty("最大连杀数")
    private int killingSpree;

    @ApiModelProperty("击杀NPC数")
    private int goblinKill;

    @ApiModelProperty("被玩家击杀数")
    private int slaying;

    @ApiModelProperty("被NPC击杀数")
    private int goblin;

    @ApiModelProperty("稀有属性ID")
    private Integer rarityAttr;

    @ApiModelProperty("稀有属性值")
    private String rarityAttrValue;

    @ApiModelProperty("基础属性值")
    private String baseAttrValue;

    @ApiModelProperty("被动描述")
    private String passiveAttrDesc;

    @ApiModelProperty("特殊属性描述")
    private String specialAttrDesc;

    @ApiModelProperty("关联z_equipment_nft表")
    private Long eqNftId;

}
