package com.seeds.uc.dto.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 用户地址
 * </p>
 *
 * @author yk
 * @since 2022-07-29
 */
@Data
@Builder
public class UcUserAddressReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String currency;

    @NotBlank
    private String chain;

    @ApiModelProperty("地址")
    @NotBlank
    private String address;

    @ApiModelProperty("备注")
    private String comments;

}