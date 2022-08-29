package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysSequenceNoEntity;
import com.seeds.admin.enums.SequenceNoTypeEnum;
import com.seeds.admin.mapper.SysSequenceNoMapper;
import com.seeds.admin.service.SysSequenceNoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 系统序列号
 *
 * @author hang.yu
 * @date 2022/7/25
 */
@Service
public class SysSequenceNoServiceImpl extends ServiceImpl<SysSequenceNoMapper, SysSequenceNoEntity> implements SysSequenceNoService {

    @Value("${admin.generateNo.nft.prefix:#}")
    private String nftPrefix;

    @Value("${admin.generateNo.nft.format:%07d}")
    private String nftFormat;


    @Override
    public SysSequenceNoEntity queryByType(String type) {
        LambdaQueryWrapper<SysSequenceNoEntity> query = new QueryWrapper<SysSequenceNoEntity>().lambda()
                .eq(SysSequenceNoEntity::getType, type);
        return getOne(query);
    }

    @Override
    public String generateNftNo() {
        SysSequenceNoEntity sysSequenceNo = queryByType(SequenceNoTypeEnum.NFT.name());
        if (sysSequenceNo != null) {
            sysSequenceNo.setNumber(sysSequenceNo.getNumber() + 1);
            updateById(sysSequenceNo);
        } else {
            sysSequenceNo = new SysSequenceNoEntity();
            sysSequenceNo.setType(SequenceNoTypeEnum.NFT.name());
            sysSequenceNo.setNumber(1L);
            sysSequenceNo.setPrefix(nftPrefix);
            save(sysSequenceNo);
        }
        return sysSequenceNo.getPrefix() + String.format(nftFormat, sysSequenceNo.getNumber());
    }
}