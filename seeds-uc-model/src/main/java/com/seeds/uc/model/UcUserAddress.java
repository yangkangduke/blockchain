package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 用户地址
 * </p>
 *
 * @author yk
 * @since 2022-07-29
 */
@TableName("uc_user_address")
@ApiModel(value = "UcUserAddress对象", description = "用户地址")
@Data
@Builder
public class UcUserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("版本")
    private Long version;

    private String currency;

    private String chain;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("备注")
    private String comments;


}
