package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Admin
 * @date 2023/4/28 13:54
 * @description transactionMessageReqDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionMessageReqDto {

    @ApiModelProperty(value = "签名")
    private String signature;
}
