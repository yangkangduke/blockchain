package com.seeds.game.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Admin
 * @date 2023/4/28 13:54
 * @description transactionMessageReqDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionMessageRespDto {

    @ApiModelProperty(value = "接受者")
    private String recipient;

    @ApiModelProperty(value = "发送者")
    private String sender;

    @ApiModelProperty(value = "数量")
    private BigInteger amount;

    @ApiModelProperty(value = "feeMemo")
    private FeeMemoMessageRespDto feeMemo;
}
