package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysAdminUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户
 *
 * @author hang.yu
 * @date 2022/7/13
 */
@Mapper
public interface SysAdminUserMapper extends BaseMapper<SysAdminUserEntity> {

}