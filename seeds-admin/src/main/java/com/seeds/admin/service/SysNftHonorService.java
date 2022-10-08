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

}
