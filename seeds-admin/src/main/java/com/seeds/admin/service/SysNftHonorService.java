package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysNftHonorEntity;

import java.util.Collection;


/**
 * NFT战绩
 *
 * @author hang.yu
 * @date 2022/10/8
 */
public interface SysNftHonorService extends IService<SysNftHonorEntity> {

    /**
     * 通过nft的id列表删除NFT战绩列表
     * @param nftIds nft的id列表
     */
    void removeByNftIds(Collection<Long> nftIds);

    /**
     * NFT的战绩记录继承
     * @param oldNftId 旧的NFT的id
     * @param newNftId 新的NFT的id
     */
    void successionByNftId(Long oldNftId, Long newNftId);

}
