package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.WithdrawWhitelist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 提币白名单 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Mapper
public interface WithdrawWhitelistMapper extends BaseMapper<WithdrawWhitelist> {
    WithdrawWhitelist getWithdrawWhitelistByUserIdAndCurrency(@Param("userId") long userId, @Param("currency") String currency);

    Integer updateByPrimaryKey(WithdrawWhitelist withdrawWhitelist);
}
