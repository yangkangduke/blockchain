package com.seeds.game.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.game.dto.response.GameRankNftSkinResp;
import com.seeds.game.entity.NftAttributeEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface INftAttributeService extends IService<NftAttributeEntity> {

    NftAttributeEntity detailForMintAddress(String mintAddress);

    NftAttributeEntity queryByNftId(Long nftId);

    /**
     * 通过NFT的id获取NFT属性
     * @param nftIds NFT的id
     * @return NFT属性
     */
    Map<Long, NftAttributeEntity> queryMapByNftIds(Collection<Long> nftIds);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankVictory(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankLose(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankMaxStreak(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankCapture(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankKillingSpree(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankGoblinKill(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankSlaying(Integer number);

    /**
     * 获取NFT皮肤排行
     * @param number 查询数量
     * @return NFT皮肤排行
     */
    List<NftAttributeEntity> querySkinRankGoblin(Integer number);

    /**
     * 计算NFT皮肤排行积分
     * @param rankList 数据
     * @param map 结果集
     * @param score 初始积分
     * @param change 积分变化量
     */
    void calculateSkinRankScore(List<NftAttributeEntity> rankList, Map<Long, GameRankNftSkinResp.GameRankNftSkin> map, int score, int change);

}
