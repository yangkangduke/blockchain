package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysGameApiEntity;
import com.seeds.admin.mapper.SysGameApiMapper;
import com.seeds.admin.service.SysGameApiService;
import org.springframework.stereotype.Service;


/**
 * 游戏api
 *
 * @author hang.yu
 * @date 2022/10/10
 */
@Service
public class SysGameApiServiceImpl extends ServiceImpl<SysGameApiMapper, SysGameApiEntity> implements SysGameApiService {

    @Override
    public String queryApiByGameAndType(Long gameId, Integer type) {
        LambdaQueryWrapper<SysGameApiEntity> queryWrap = new QueryWrapper<SysGameApiEntity>().lambda()
                .eq(SysGameApiEntity::getGameId, gameId)
                .eq(SysGameApiEntity::getType, type);
        SysGameApiEntity gameApi = getOne(queryWrap);
        return gameApi.getApi();
    }
}

