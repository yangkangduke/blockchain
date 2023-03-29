package com.seeds.game.mapper;

import com.seeds.game.dto.request.internal.NftBackpackWebPageReq;
import com.seeds.game.dto.response.NftPublicBackpackWebResp;
import com.seeds.game.dto.response.NftType;
import com.seeds.game.dto.response.NftTypeNum;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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

    List<NftType> getNftTypeList(Long userId);

    List<NftPublicBackpackWebResp> getPageForWeb(NftBackpackWebPageReq req);
}
