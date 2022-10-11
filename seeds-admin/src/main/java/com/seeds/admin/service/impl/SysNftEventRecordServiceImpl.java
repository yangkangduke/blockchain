package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysNftEventRecordEntity;
import com.seeds.admin.mapper.SysNftEventRecordMapper;
import com.seeds.admin.service.SysNftEventRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * NFT事件记录
 *
 * @author hang.yu
 * @date 2022/10/8
 */
@Slf4j
@Service
public class SysNftEventRecordServiceImpl extends ServiceImpl<SysNftEventRecordMapper, SysNftEventRecordEntity> implements SysNftEventRecordService {

    @Override
    public void successionByNftId(Long oldNftId, Long newNftId) {
        LambdaUpdateWrapper<SysNftEventRecordEntity> queryWrap = new UpdateWrapper<SysNftEventRecordEntity>().lambda()
                .set(SysNftEventRecordEntity::getNftId, newNftId)
                .eq(SysNftEventRecordEntity::getNftId, oldNftId);
        update(queryWrap);
    }
}