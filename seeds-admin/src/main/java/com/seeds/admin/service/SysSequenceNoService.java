package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.entity.SysSequenceNoEntity;

/**
 * 系统序列号
 *
 * @author hang.yu
 * @date 2022/7/25
 */
public interface SysSequenceNoService extends IService<SysSequenceNoEntity> {

    /**
     * 通过类型查询序列号
     * @param type 类型
     * @return 序列号
     */
    SysSequenceNoEntity queryByType(String type);

    /**
     * 生成NFT编号
     * @return NFT编号
     */
    String generateNftNo();

    /**
     * 生成游戏认证编号
     * @return 游戏认证编号
     */
    Long generateGameAccessNo();

    /**
     * 生成随机码编号
     * @return 随机码编号
     */
    String generateRandomCodeNo();

}
