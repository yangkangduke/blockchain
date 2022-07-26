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
@TableName("uc_user_account")
@ApiModel(value = "UcUserAccount对象", description = "用户账户表")
@Data
@Builder
public class UcUserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改时间")
    private Long updateTime;

    @ApiModelProperty("版本控制")
    private Long version;

    @ApiModelProperty("账号类型 1 现货 ")
    private Integer accountType;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("账户余额")
    private BigDecimal balance;

    @ApiModelProperty("冻结金额")
    private BigDecimal freeze;

}
