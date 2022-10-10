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

    /**
     * 更新available
     *
     * @param userId
     * @param currency
     * @param amount
     * @param geZero
     * @return
     */
    int updateAvailable(@Param("userId") long userId, @Param("currency") String currency, @Param("amount") BigDecimal amount, @Param("geZero") boolean geZero);

    /**
     * 查看用户的账户是否存在
     * @param userId
     * @param currency
     * @return
     */
    int countUserAccount(@Param("userId") long userId, @Param("currency") String currency);
}
