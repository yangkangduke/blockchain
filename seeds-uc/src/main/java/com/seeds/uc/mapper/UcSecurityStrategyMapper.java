package com.seeds.uc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.uc.dto.response.UcSecurityStrategyResp;
import com.seeds.uc.model.UcSecurityStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 安全策略表 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-07-15
 */
public interface UcSecurityStrategyMapper extends BaseMapper<UcSecurityStrategy> {

    List<UcSecurityStrategyResp> getByUserId(Long userId);
    List<UcSecurityStrategy> listByUid(@Param("uid") Long uid);

}
