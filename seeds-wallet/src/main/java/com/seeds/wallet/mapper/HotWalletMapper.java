package com.seeds.wallet.mapper;

import com.seeds.wallet.model.HotWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HotWalletMapper {

    int deleteByPrimaryKey(Long id);

    int insert(HotWallet record);

    HotWallet selectByPrimaryKey(Long id);

    List<HotWallet> selectAll();

    int updateByPrimaryKey(HotWallet record);

    HotWallet selectByAddress(@Param("chain") int chain, @Param("address") String address);
}