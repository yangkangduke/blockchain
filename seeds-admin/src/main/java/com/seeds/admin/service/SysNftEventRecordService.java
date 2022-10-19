package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysNftEventRecordEntity;


/**
 * NFT事件记录
 *
 * @author hang.yu
 * @date 2022/10/8
 */
public interface SysNftEventRecordService extends IService<SysNftEventRecordEntity> {

    /**
     * NFT的事件记录继承
     * @param oldNftId 旧的NFT的id
     * @param newNftId 新的NFT的id
     */
    void successionByNftId(Long oldNftId, Long newNftId);

}