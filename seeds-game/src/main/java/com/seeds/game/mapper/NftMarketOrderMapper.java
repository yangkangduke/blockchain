package com.seeds.game.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlacePropsPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlacePropsResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftMarketOrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NftMarketOrderMapper extends BaseMapper<NftMarketOrderEntity> {

    /**
     * 获取皮肤分页信息
     * @param skinQuery
     * @return
     */
    List<NftMarketPlaceSkinResp> getSkinPage(NftMarketPlaceSkinPageReq skinQuery);

    /**
     * 获取装备分页信息
     * @param equipQuery
     * @return
     */
    List<NftMarketPlaceEqiupmentResp> getEquipPage(NftMarketPlaceEquipPageReq equipQuery);

    /**
     * 获取道具分页信息
     * @param propsQuery
     * @return
     */
    List<NftMarketPlacePropsResp> getPropsPage(NftMarketPlacePropsPageReq propsQuery);
}
