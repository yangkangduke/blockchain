package com.seeds.admin.mapper;

import com.seeds.admin.entity.SysNftPicEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysNftPicMapper extends BaseMapper<SysNftPicEntity> {
    String getListReceiptByMintAddress(String tokenAddress);
}
