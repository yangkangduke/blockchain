package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

}