package com.seeds.account.model;

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
 * 钱包账户
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Data
@Builder
@TableName("ac_user_account")
@ApiModel(value = "UserAccount对象", description = "钱包账户")
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

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty("currency")
    private String currency;

    @ApiModelProperty("available")
    private BigDecimal available;

    @ApiModelProperty("freeze amount")
    private BigDecimal freeze;

    private BigDecimal locked;
}
