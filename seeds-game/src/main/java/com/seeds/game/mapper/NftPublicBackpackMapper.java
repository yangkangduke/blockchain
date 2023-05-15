package com.seeds.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.game.dto.request.internal.NftBackpackWebPageReq;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.NftPublicBackpackEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * NFT公共背包 Mapper 接口
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
public interface NftPublicBackpackMapper extends BaseMapper<NftPublicBackpackEntity> {

    List<NftTypeNum> selectTypeNum(Long userId);

    List<NftType> getNftTypeList(@Param("userId") Long userId, @Param("type") Integer type);

    List<NftPublicBackpackWebResp> getPageForWeb(NftBackpackWebPageReq req);

    List<SkinNftTypeResp> getSkinNftTypeList(@Param("userId") Long userId, @Param("profession")String profession);

    List<NftPublicBackpackSkinWebResp> getSkinPageForWeb(NftBackpackWebPageReq req);
}
