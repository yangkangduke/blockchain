package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.SystemConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 全局配置 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
    /**
     * 插入新的
     *
     * @param record
     * @return
     */
    @Override
    int insert(SystemConfig record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SystemConfig record);

    /**
     * 获取所有
     * @return
     */
    List<SystemConfig> selectAll();

    /**
     * 获取一个
     * @param type
     * @param key
     * @return
     */
    SystemConfig getByTypeAndKey(@Param("type") String type, @Param("key") String key);
}
