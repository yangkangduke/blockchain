package com.seeds.uc.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

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
public class UcUserAccountInfoResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("primary key")
    private Long id;

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

    private UcUserAddressInfoResp userAddressResp;

}
