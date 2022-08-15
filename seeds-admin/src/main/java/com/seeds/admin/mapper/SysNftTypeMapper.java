package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysNftTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Mapper
public interface SysNftTypeMapper extends BaseMapper<SysNftTypeEntity> {

    SysNftTypeEntity queryById(@Param("id") Long id);

    List<SysNftTypeEntity> queryListByIds(@Param("ids") Collection<Long> ids);

}