package com.seeds.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.account.model.SystemWalletAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统使用的钱包地址 Mapper 接口
 * </p>
 *
 * @author yk
 * @since 2022-09-28
 */
public interface SystemWalletAddressMapper extends BaseMapper<SystemWalletAddress> {

    /**
     * 获取所有
     * @return
     */
    List<SystemWalletAddress> selectAll();

    /**
     * 获取一个合约
     * @param chain
     * @param type
     * @param address
     * @return
     */
    SystemWalletAddress getByChainAndTypeAndAddress(@Param("chain") int chain, @Param("type") int type, @Param("address") String address);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(SystemWalletAddress record);
}
