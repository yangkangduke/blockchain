package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.entity.NftAttributeEntity;
import com.seeds.game.mapper.NftAttributeMapper;
import com.seeds.game.service.INftAttributeService;
import org.springframework.stereotype.Service;



@Service
public class NftAttributeServiceImpl extends ServiceImpl<NftAttributeMapper, NftAttributeEntity>implements INftAttributeService {

}
