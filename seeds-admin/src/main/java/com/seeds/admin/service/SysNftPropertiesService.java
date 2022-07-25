package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysNftPropertiesEntity;

import java.util.Collection;
import java.util.List;

/**
 * NFT属性
 *
 * @author hang.yu
 * @date 2022/7/22
 */
public interface SysNftPropertiesService extends IService<SysNftPropertiesEntity> {

    /**
     * 保存或修改
     * @param nftId      nft的id
     * @param nftPropertiesList  nft属性列表
     */
    void saveOrUpdate(Long nftId, List<SysNftPropertiesEntity> nftPropertiesList);

    /**
     * 通过NFT的id查询nft属性列表
     * @param nftId nft的id
     * @return  nft属性列表
     */
    List<SysNftPropertiesEntity> queryByNftId(Long nftId);

    /**
     * 通过nft的id删除nft属性
     * @param nftId   nft的id
     */
    void deleteByNftId(Long nftId);

    /**
     * 通过nft的id删除nft属性
     * @param nftIds  nft的id列表
     */
    void deleteByNftIs(Collection<Long> nftIds);

}
