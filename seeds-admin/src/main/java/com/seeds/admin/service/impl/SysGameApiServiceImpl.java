package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysGameApiEntity;
import com.seeds.admin.mapper.SysGameApiMapper;
import com.seeds.admin.service.SysGameApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    @Override
    public SysGameApiEntity queryByGameAndType(Long gameId, Integer type) {
        LambdaQueryWrapper<SysGameApiEntity> queryWrap = new QueryWrapper<SysGameApiEntity>().lambda()
                .eq(SysGameApiEntity::getGameId, gameId)
                .eq(SysGameApiEntity::getType, type);
        return getOne(queryWrap);
    }

    @Override
    public List<String> queryUrlByGameAndType(Long gameId, Integer type) {
        SysGameApiEntity gameApi = queryByGameAndType(gameId, type);
        String baseUrl = gameApi.getBaseUrl();
        List<String> urls = new ArrayList<>();
        if (baseUrl.contains("|")) {
            urls = Arrays.stream(baseUrl.split("\\|")).map(p -> p + gameApi.getApi()).collect(Collectors.toList());
        } else {
            urls.add(baseUrl + gameApi.getApi());
        }
        return urls;
    }
}

