package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysNftHonorEntity;
import com.seeds.admin.mapper.SysNftHonorMapper;
import com.seeds.admin.service.SysNftHonorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**
 * NFT战绩
 *
 * @author hang.yu
 * @date 2022/10/8
 */
@Slf4j
@Service
public class SysNftHonorServiceImpl extends ServiceImpl<SysNftHonorMapper, SysNftHonorEntity> implements SysNftHonorService {

    @Override
    public void removeByNftIds(Collection<Long> nftIds) {
        LambdaQueryWrapper<SysNftHonorEntity> queryWrap = new QueryWrapper<SysNftHonorEntity>().lambda()
                .in(SysNftHonorEntity::getNftId, nftIds);
        remove(queryWrap);
    }

    @Override
    public void successionByNftId(Long oldNftId, Long newNftId) {
        LambdaUpdateWrapper<SysNftHonorEntity> queryWrap = new UpdateWrapper<SysNftHonorEntity>().lambda()
                .set(SysNftHonorEntity::getNftId, newNftId)
                .eq(SysNftHonorEntity::getNftId, oldNftId);
        update(queryWrap);
    }
}