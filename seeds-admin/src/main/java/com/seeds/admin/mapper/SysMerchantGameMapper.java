package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysMerchantGameEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统商家与游戏关联
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Mapper
public interface SysMerchantGameMapper extends BaseMapper<SysMerchantGameEntity> {

}