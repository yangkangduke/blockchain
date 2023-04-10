package com.seeds.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.entity.NftEquipment;

/**
 * <p>
 * nft装备
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
public interface INftEquipmentService extends IService<NftEquipment> {

    /**
     * 通过nft地址查询nft装备
     * @param mintAddress nft地址
     * @return nft装备
     */
    NftEquipment queryByMintAddress(String mintAddress);

}
