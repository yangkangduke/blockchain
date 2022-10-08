package com.seeds.account.mapper;

import com.seeds.account.model.ChainDepositWithdrawSigHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChainDepositWithdrawSigHisMapper {

    int insert(ChainDepositWithdrawSigHis record);

    ChainDepositWithdrawSigHis selectByPrimaryKey(long id);

    ChainDepositWithdrawSigHis getByUserId(@Param("id") long id, @Param("userId") long userId);
}