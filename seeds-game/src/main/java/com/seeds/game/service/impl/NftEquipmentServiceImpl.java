package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftEquipment;
import com.seeds.game.mapper.NftEquipmentMapper;
import com.seeds.game.service.INftEquipmentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * nft装备
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Service
public class NftEquipmentServiceImpl extends ServiceImpl<NftEquipmentMapper, NftEquipment> implements INftEquipmentService {

    @Override
    public NftEquipment queryByMintAddress(String mintAddress) {
        return getOne(new LambdaQueryWrapper<NftEquipment>()
                .eq(NftEquipment::getMintAddress, mintAddress));
    }
}
