package com.seeds.account.model;

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
 * 系统使用的钱包地址
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
@TableName("system_wallet_address")
@ApiModel(value = "SystemWalletAddress对象", description = "系统使用的钱包地址")
@Data
@Builder
public class SystemWalletAddress implements Serializable {

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

    private Integer type;

    @ApiModelProperty("name")
    private String address;

    @ApiModelProperty("key")
    private String tag;

    @ApiModelProperty("comments")
    private String comments;

    private Integer status;

}
