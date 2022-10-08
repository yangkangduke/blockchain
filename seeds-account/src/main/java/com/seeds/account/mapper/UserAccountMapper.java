package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.UserAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包账户 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    /**
     * 冻结
     *
     * @param userId
     * @param currency
     * @param amount
     * @return
     */
    int freeze(@Param("userId") long userId, @Param("currency") String currency, @Param("amount") BigDecimal amount);
}
