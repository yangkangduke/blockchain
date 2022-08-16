package com.seeds.uc.dto.request;

import com.seeds.common.dto.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 用户账号交易
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Data
@Builder
public class AccountActionHistoryReq extends PageReq {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private Long userId;

}
