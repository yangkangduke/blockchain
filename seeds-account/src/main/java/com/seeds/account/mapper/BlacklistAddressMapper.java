package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.model.BlacklistAddress;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Ethereum黑地址 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface BlacklistAddressMapper extends BaseMapper<BlacklistAddress> {

    int updateByPrimaryKey(BlacklistAddress record);

    BlacklistAddress getByTypeUserIdAndAddress(@Param("type") int type,
                                                 // @Param("userId") Long userId,
                                                  @Param("address") String address);

    int deleteByPrimaryKey(Long id);

}
