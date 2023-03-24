package com.seeds.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.game.dto.response.TypeNum;
import com.seeds.game.entity.NftEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * nft通知 Mapper 接口
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
public interface NftEventMapper extends BaseMapper<NftEvent> {

    List<TypeNum> getTypeCount(@Param("userId") Long userId);

    void updateClick(@Param("type") Integer type, @Param("userId") Long userId);
}
