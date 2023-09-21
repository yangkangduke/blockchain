package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysGameEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 系统游戏
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Mapper
public interface SysGameMapper extends BaseMapper<SysGameEntity> {

    SysGameEntity queryById(@Param("id") Long id);

    List<SysGameEntity> queryListByIds(@Param("ids") Collection<Long> ids);

}