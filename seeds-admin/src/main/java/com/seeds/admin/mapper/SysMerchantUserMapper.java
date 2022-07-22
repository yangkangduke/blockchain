package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysMerchantUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统商家与用户关联
 *
 * @author hang.yu
 * @date 2022/7/20
 */
@Mapper
public interface SysMerchantUserMapper extends BaseMapper<SysMerchantUserEntity> {

}