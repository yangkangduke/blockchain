package com.seeds.admin.mapper;

import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.game.entity.NftMarketOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysNftPicMapper extends BaseMapper<SysNftPicEntity> {
    List<NftMarketOrderEntity> getListReceiptByMintAddress(@Param("tokenAddresses") List<String> tokenAddresses);

    List<NftMarketOrderEntity> getAuctionIdByMintAddress(@Param("tokenAddresses") List<String> tokenAddresses);
}
