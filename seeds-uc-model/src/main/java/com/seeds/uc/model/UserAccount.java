package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账户表
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@TableName("user_account")
@ApiModel(value = "UserAccount对象", description = "用户账户表")
@Data
@Builder
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("create time")
    private Long createTime;

    @ApiModelProperty("update time")
    private Long updateTime;

    @ApiModelProperty("version")
    private Long version;

    private Integer accountType;

    @ApiModelProperty("userId")
    private Long userId;

    private String currency;

    private BigDecimal balance;

    private BigDecimal freeze;

}
