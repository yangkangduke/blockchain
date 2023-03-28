package com.seeds.game.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * nft通知
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
@TableName("ga_nft_event")
@ApiModel(value = "NftEvent对象", description = "nft通知")
@Data
public class NftEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("通知类型 1.mint 2.compound 3.other")
    private Integer type;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("1.Pending 2.Cancelled 3.Minted")
    private Integer status;

    @ApiModelProperty("用户是否点击过对应的tab,点过就不再显示数字 0 未点击 1 已点击")
    private Integer click;

    private String transferFrom;

    private String transferTo;

    @ApiModelProperty(value = "游戏服务角色id", required = true)
    private Long serverRoleId;

    @ApiModelProperty("创建人")
    private Long createdAt;

    @ApiModelProperty("创建时间")
    private Long createdBy;

    @ApiModelProperty("修改时间")
    private Long updatedAt;

    @ApiModelProperty("修改人")
    private Long updatedBy;

}
