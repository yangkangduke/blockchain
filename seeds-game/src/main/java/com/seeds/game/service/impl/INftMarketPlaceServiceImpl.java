package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.entity.NftMarketPlaceEntity;
import com.seeds.game.mapper.NftMarketPlaceMapper;
import com.seeds.game.service.INftMarketPlaceService;
import org.springframework.stereotype.Service;

@Service
public class INftMarketPlaceServiceImpl extends ServiceImpl<NftMarketPlaceMapper, NftMarketPlaceEntity>implements INftMarketPlaceService {

    @Override
    public NftMarketPlaceDetailResp detail(Long id) {
        return null;
    }
}
