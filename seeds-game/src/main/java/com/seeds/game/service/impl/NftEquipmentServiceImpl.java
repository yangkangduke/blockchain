package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftEquipment;
import com.seeds.game.mapper.NftEquipmentMapper;
import com.seeds.game.service.INftEquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public Map<String, NftEquipment> getOwnerByMintAddress(List<String> mintAddress) {
       return this.list(new LambdaQueryWrapper<NftEquipment>().in(NftEquipment::getMintAddress, mintAddress))
                .stream().collect(Collectors.toMap(NftEquipment::getMintAddress, p -> p));
    }
}
