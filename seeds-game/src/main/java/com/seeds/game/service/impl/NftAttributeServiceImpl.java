package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftAttributeEntity;
import com.seeds.game.mapper.NftAttributeMapper;
import com.seeds.game.service.INftAttributeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NftAttributeServiceImpl extends ServiceImpl<NftAttributeMapper, NftAttributeEntity>implements INftAttributeService {

    @Autowired
    private NftAttributeMapper nftAttributeMapper;


    @Override
    public NftAttributeEntity detailForMintAddress(String mintAddress) {
        NftAttributeEntity nftAttributeEntity = new NftAttributeEntity();
        NftAttributeEntity entity = this.getOne(new LambdaQueryWrapper<NftAttributeEntity>().eq(NftAttributeEntity::getMintAddress, mintAddress));
        if (null != entity) {
            BeanUtils.copyProperties(entity, nftAttributeEntity);
        }
        return nftAttributeEntity;
    }

}
