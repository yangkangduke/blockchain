package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.ChainDepositAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Ethereum地址 充币地址（交易所分配） Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
public interface ChainDepositAddressMapper extends BaseMapper<ChainDepositAddress> {

    /**
     * 获取用户的绑定地址
     *
     * @param chain
     * @param userId
     * @return
     */
    ChainDepositAddress getAssignedAddressByUserId(@Param("chain") int chain, @Param("userId") long userId);

    /**
     * 获取未分配地址列表
     *
     * @param chain
     * @return
     */
    List<ChainDepositAddress> getIdleAddresses(@Param("chain") int chain);

    /**
     * 分配地址
     *
     * @param chain
     * @param address
     * @param userId
     * @return
     */
    int assignAddress(@Param("chain") int chain, String address, @Param("userId") long userId);

    /**
     * 统计未分配空闲地址的数量
     *
     * @return
     */
    int countIdleAddresses(@Param("chain") int chain);

    /**
     * 根据地址获取
     * @param chain
     * @param address
     * @return
     */
    ChainDepositAddress getByAddress(@Param("chain") int chain, @Param("address") String address);

    /**
     * 获取已经分配地址列表
     *
     * @param chain
     * @return
     */
    List<ChainDepositAddress> getAssignedAddresses(@Param("chain") int chain);
}
