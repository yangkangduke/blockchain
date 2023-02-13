package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 游戏服对局记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-13
 */
@TableName("ga_server_match_record")
@ApiModel(value = "ServerMatchRecordEntity对象", description = "游戏服对局记录")
@Data
public class ServerMatchRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("战斗模式：317 Free，402 Loot")
    private Integer mModeId;

    @ApiModelProperty("玩家单局中击杀其他玩家的数量")
    private Integer mKillNum;

    @ApiModelProperty("每局结束时玩家的生存时间")
    private Long mSurvivalTime;

    @ApiModelProperty("统计玩家当前的竞技场积分")
    private Long mTotalScore;

    @ApiModelProperty("玩家账号id")
    private String accId;

    @ApiModelProperty("游戏服id")
    private String gameServerId;

    @ApiModelProperty("创建人")
    private Long createdBy;

    @ApiModelProperty("修改人")
    private Long updatedBy;

    @ApiModelProperty("上传时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

}
