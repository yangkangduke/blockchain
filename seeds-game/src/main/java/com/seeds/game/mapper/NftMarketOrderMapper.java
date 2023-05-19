package com.seeds.game.mapper;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.game.dto.request.GameRankNftPageReq;
import com.seeds.game.dto.response.GameRankEquipResp;
import com.seeds.game.dto.response.GameRankHeroResp;
import com.seeds.game.dto.response.GameRankItemResp;
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

    /**
     * 获取装备分页信息
     * @param page 分页
     * @param query 查询条件
     * @return 装备分页信息
     */
    IPage<GameRankEquipResp> equipPage(Page<GameRankEquipResp> page, @Param("params") GameRankNftPageReq query);

    /**
     * 获取道具分页信息
     * @param page 分页
     * @param query 查询条件
     * @return 道具分页信息
     */
    IPage<GameRankItemResp> itemPage(Page<GameRankItemResp> page, @Param("params") GameRankNftPageReq query);

    /**
     * 获取英雄分页信息
     * @param page 分页
     * @param query 查询条件
     * @return 英雄分页信息
     */
    IPage<GameRankHeroResp> heroPage(Page<GameRankHeroResp> page, @Param("params") GameRankNftPageReq query);


}
