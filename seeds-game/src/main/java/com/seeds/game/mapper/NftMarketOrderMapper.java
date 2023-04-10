package com.seeds.game.mapper;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlacePropsPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlacePropsResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftMarketOrderEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NftMarketOrderMapper extends BaseMapper<NftMarketOrderEntity> {

    /**
     * 获取皮肤分页信息
     * @param skinQuery
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    IPage<NftMarketPlaceSkinResp> getSkinPage(Page page,@Param("params") NftMarketPlaceSkinPageReq skinQuery);

    /**
     * 获取装备分页信息
     * @param equipQuery
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    IPage<NftMarketPlaceEqiupmentResp> getEquipPage(Page page,@Param("params") NftMarketPlaceEquipPageReq equipQuery);

    /**
     * 获取道具分页信息
     * @param propsQuery
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    IPage<NftMarketPlacePropsResp> getPropsPage(Page page,@Param("params") NftMarketPlacePropsPageReq propsQuery);
}
