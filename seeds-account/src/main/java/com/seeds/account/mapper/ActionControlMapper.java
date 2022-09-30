package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.ActionControl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 操作控制 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
public interface ActionControlMapper extends BaseMapper<ActionControl> {

    /**
     * 插入新的
     *
     * @param record
     * @return
     */
    int insert(ActionControl record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ActionControl record);

    /**
     * 获取所有
     * @return
     */
    List<ActionControl> selectAll();

    /**
     * 获取一个
     * @param type
     * @param key
     * @return
     */
    ActionControl getByTypeAndKey(@Param("type") String type, @Param("key") String key);
}
