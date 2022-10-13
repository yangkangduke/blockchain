package com.seeds.account.mapper;

import com.seeds.account.model.ChainContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChainContractMapper {
    /**
     * 插入新的
     *
     * @param record
     * @return
     */
    int insert(ChainContract record);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ChainContract record);

    /**
     * 获取所有
     * @return
     */
    List<ChainContract> selectAll();

    /**
     * 获取一个合约
     * @param chain
     * @param currency
     * @param address
     * @return
     */
    ChainContract getByCurrencyAndAddress(@Param("chain") int chain, @Param("currency") String currency, @Param("address") String address);
}