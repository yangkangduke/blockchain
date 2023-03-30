package com.seeds.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.game.entity.NftEquipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * nft装备
 * </p>
 *
 * @author hang.yu
 * @since 2023-03-28
 */
@Mapper
public interface NftEquipmentMapper extends BaseMapper<NftEquipment> {

    NftEquipment getByMintAddress(@Param("mintAddress") String mintAddress);

}
