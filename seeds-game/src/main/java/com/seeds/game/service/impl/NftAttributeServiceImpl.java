package com.seeds.game.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.response.GameRankNftSkinResp;
import com.seeds.game.entity.NftAttributeEntity;
import com.seeds.game.enums.NftHeroTypeEnum;
import com.seeds.game.mapper.NftAttributeMapper;
import com.seeds.game.service.INftAttributeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public NftAttributeEntity queryByNftId(Long nftId) {
        return this.getOne(new LambdaQueryWrapper<NftAttributeEntity>().eq(NftAttributeEntity::getEqNftId, nftId));
    }

    @Override
    public Map<Long, NftAttributeEntity> queryMapByNftIds(Collection<Long> nftIds) {
        if (CollectionUtils.isEmpty(nftIds)) {
            return Collections.emptyMap();
        }
        List<NftAttributeEntity> list = this.list(new LambdaQueryWrapper<NftAttributeEntity>().in(NftAttributeEntity::getEqNftId, nftIds));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(NftAttributeEntity::getEqNftId, p -> p));
    }

    @Override
    public List<NftAttributeEntity> querySkinRankVictory(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getVictory);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankLose(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getLose);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankMaxStreak(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getMaxStreak);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankCapture(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getCapture);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankKillingSpree(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getKillingSpree);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankGoblinKill(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getGoblinKill);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankSlaying(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getSlaying);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public List<NftAttributeEntity> querySkinRankGoblin(Integer number) {
        LambdaQueryWrapper<NftAttributeEntity> queryWrap = new QueryWrapper<NftAttributeEntity>().lambda()
                .ne(NftAttributeEntity::getHeroType, 0)
                .ne(NftAttributeEntity::getVictory, 0).or(p -> p.ne(NftAttributeEntity::getLose, 0))
                .orderByDesc(NftAttributeEntity::getGoblin);
        queryWrap.last(" limit " + number);
        return list(queryWrap);
    }

    @Override
    public void calculateSkinRankScore(List<NftAttributeEntity> rankList, Map<Long, GameRankNftSkinResp.GameRankNftSkin> map, double factor, int change) {
        if (CollectionUtils.isEmpty(rankList)) {
            return;
        }
        double score = 200;
        for (NftAttributeEntity lose : rankList) {
            GameRankNftSkinResp.GameRankNftSkin resp = new GameRankNftSkinResp.GameRankNftSkin();
            resp.setOccupation(NftHeroTypeEnum.getProfessionByCode(lose.getHeroType()));
            resp.setNftId(lose.getEqNftId());
            double realScore = score * factor;
            resp.setScore(realScore);
            GameRankNftSkinResp.GameRankNftSkin rank = map.get(resp.getNftId());
            if (rank != null) {
                resp.setScore(rank.getScore() + realScore);
            }
            map.put(resp.getNftId(), resp);
            score = score + change;
        }
    }

}
