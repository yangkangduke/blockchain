package com.seeds.admin.web.merchant.service;

import com.seeds.admin.entity.merchant.SysMerchantGameEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 系统商家与游戏关联
 *
 * @author hang.yu
 * @date 2022/7/21
 */
public interface SysMerchantGameService {

    /**
     * 保存
     * @param merchantId  商家id
     * @param gameIds  游戏id列表
     */
    void add(Long merchantId, Collection<Long> gameIds);


    /**
     * 通过商家id查询系统商家与游戏关联
     * @param  merchantId  商家id
     * @return  系统商家与游戏关联
     */
    List<SysMerchantGameEntity> queryByMerchantId(Long merchantId);

    /**
     * 通过商家id查询与系统商家关联的游戏id列表
     * @param  merchantId  商家id
     * @return  系统商家关联的游戏id列表
     */
    Set<Long> queryGameIdByMerchantId(Long merchantId);

    /**
     * 通过游戏id列表删除系统商家与游戏关联
     * @param  merchantId  商家id
     * @param  gameIds  游戏id列表
     */
    void deleteByMerchantIdAndGameIds(Long merchantId, Collection<Long> gameIds);

    /**
     * 删除商户下所有与游戏的关联
     * @param  merchantId  商家id
     */
    void deleteByMerchantId(Long merchantId);


}