package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftFeeOpLog;


/**
 * <p>
 * nft手续费记录
 * </p>
 *
 * @author hang.yu
 * @since 2023-05-09
 */
public interface INftFeeOpLogService extends IService<NftFeeOpLog> {

    /**
     * 获取NFT手续费记录
     * @param txHash 交易hash
     * @return NFT交易记录
     */
    NftFeeOpLog queryByTxHash(String txHash);

}
