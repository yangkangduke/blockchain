package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
        LambdaQueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<SysNftPropertiesEntity>().lambda()
                .eq(SysNftPropertiesEntity::getNftId, nftId);
        return list(query);
    }

    @Override
    public SysNftPropertiesEntity queryByTypeAndNftId(Long typeId, Long nftId) {
        LambdaQueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<SysNftPropertiesEntity>().lambda()
                .eq(SysNftPropertiesEntity::getTypeId, typeId)
                .eq(SysNftPropertiesEntity::getNftId, nftId);
        return getOne(query);
    }

    @Override
    public void deleteByNftId(Long nftId) {
        LambdaQueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<SysNftPropertiesEntity>().lambda()
                .eq(SysNftPropertiesEntity::getNftId, nftId);
        remove(query);
    }

    @Override
    public void deleteByNftIs(Collection<Long> nftIds) {
        if (CollectionUtils.isEmpty(nftIds)) {
            return;
        }
        LambdaQueryWrapper<SysNftPropertiesEntity> query = new QueryWrapper<SysNftPropertiesEntity>().lambda()
                .in(SysNftPropertiesEntity::getNftId, nftIds);
        remove(query);
    }
}

