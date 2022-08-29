package com.seeds.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.admin.entity.SysGameTypeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;


/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/8/25
 */
@Mapper
public interface SysGameTypeMapper extends BaseMapper<SysGameTypeEntity> {

    SysGameTypeEntity queryById(@Param("id") Long id);

    List<SysGameTypeEntity> queryListByIds(@Param("ids") Collection<Long> ids);

}