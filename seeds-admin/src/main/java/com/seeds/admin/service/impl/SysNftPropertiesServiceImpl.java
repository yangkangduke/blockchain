package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysNftPropertiesEntity;
import com.seeds.admin.mapper.SysNftPropertiesMapper;
import com.seeds.admin.service.SysNftPropertiesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;


/**
 * NFT属性
 *
 * @author hang.yu
 * @date 2022/7/22
 */
@Service
public class SysNftPropertiesServiceImpl extends ServiceImpl<SysNftPropertiesMapper, SysNftPropertiesEntity> implements SysNftPropertiesService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long nftId, List<SysNftPropertiesEntity> nftPropertiesList) {
        // 先删除NFT和NFT类别关系
        deleteByNftId(nftId);
        if (CollectionUtils.isEmpty(nftPropertiesList)) {
            return;
        }
        saveBatch(nftPropertiesList);
    }

    @Override
    public List<SysNftPropertiesEntity> queryByNftId(Long nftId) {
        QueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<>();
        query.eq("nft_id", nftId);
        return list(query);
    }

    @Override
    public void deleteByNftId(Long nftId) {
        QueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<>();
        query.eq("nft_id", nftId);
        remove(query);
    }

    @Override
    public void deleteByNftIs(Collection<Long> nftIds) {
        QueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<>();
        query.in("nft_id", nftIds);
        remove(query);
    }
}

