package com.seeds.uc.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 币种信息
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@TableName("uc_currency")
@ApiModel(value = "UcCurrency对象", description = "币种信息")
@Data
@Builder
public class UcCurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("1 上线， 0 已经下线")
    private Integer status;

    @ApiModelProperty("1 可以交易， 0 停止交易")
    private Integer exchange;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

}
