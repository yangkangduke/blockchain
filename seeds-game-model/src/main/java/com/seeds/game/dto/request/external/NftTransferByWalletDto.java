package com.seeds.game.dto.request.external;

import lombok.Data;

/**
 * @author: he.wei
 * @date 2023/5/30
 */
@Data
public class NftTransferByWalletDto {

    private String mintAddress;
    private String fromAddress;
    private String toAddress;

}