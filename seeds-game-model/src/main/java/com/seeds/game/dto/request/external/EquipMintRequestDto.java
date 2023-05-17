package com.seeds.game.dto.request.external;

import lombok.Data;

/**
 * @author: hewei
 * @date 2023/5/17
 */
@Data
public class EquipMintRequestDto {
    private Long eventId;
    // 是否托管： 1 托管 0 不托管
    private Integer isDeposit;
    //转账成功后的签名信息：校验充值是否正确
    private String feeHash;
    // 用户钱包地址：当不需要托管的时候，新mint出来的token需要转移给他
    private String toUserAddress;

    private Integer type =12;
}
