package com.seeds.uc.dto.response;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

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
public class UcUserAddressInfoResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    private Long id;

    private String currency;

    private String chain;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("备注")
    private String comments;


}
