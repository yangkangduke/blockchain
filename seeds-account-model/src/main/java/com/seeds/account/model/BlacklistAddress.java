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
 * Ethereum黑地址
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@TableName("blacklist_address")
@ApiModel(value = "BlacklistAddress对象", description = "Ethereum黑地址")
@Data
@Builder
public class BlacklistAddress implements Serializable {

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

    @ApiModelProperty("chain")
    private String chain;

    private Integer type;

    @ApiModelProperty("address")
    private String address;

    @ApiModelProperty("reason")
    private String reason;

    private Integer status;


}
