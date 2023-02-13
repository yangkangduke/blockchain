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
 * 游戏服产生图纸记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-13
 */
@TableName("ga_server_produce_drawing_record")
@ApiModel(value = "ServerProduceDrawingRecordEntity对象", description = "游戏服产生图纸记录")
@Data
public class ServerProduceDrawingRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "图纸id")
    private Long drawingId;

    @ApiModelProperty("等级")
    private Integer level;

    @ApiModelProperty(value = "nft全局id（仅nft道具有该字段）")
    private Long autoId;

    @ApiModelProperty("耐久")
    private Integer durability;

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
