package com.seeds.game.service;

import com.seeds.game.dto.request.NftMintSuccessReq;

/**
 * @author: hewei
 * @date 2023/4/13
 */
public interface IAsyncNotifyGameService {
    /**
     * nft event  notify game
     *
     * @param serverRoleId
     * @param autoDeposite
     * @param optType
     * @param tokenAddress
     * @param itemType
     * @param autoId
     * @param configId
     * @param accId
     */
    void callGameNotify(Long serverRoleId, Integer autoDeposite, Integer optType, String tokenAddress, Integer itemType, Long autoId, Long configId, Long accId);

    void mintSuccess(NftMintSuccessReq req);
}
