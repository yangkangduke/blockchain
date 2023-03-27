package com.seeds.game.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.response.NftMarketPlaceDetailViewResp;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.service.NftMarketPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NFT市场外部调用
 * @author dengyang
 * @date 2023/3/27
 */
@Slf4j
@Api(tags = "NFT市场外部调用开放")
@RestController
@RequestMapping("/public/web/nft-marketplace")
public class PublicNftMarketPlaceController {

    @Autowired
    private NftMarketPlaceService nftMarketPlaceService;

    @PostMapping("page-skin")
    @ApiOperation("获取皮肤分页信息")
    public GenericDto<IPage<NftMarketPlaceSkinResp>> skinQueryPage(@RequestBody NftMarketPlaceSkinPageReq skinQuery){
        return GenericDto.success(nftMarketPlaceService.skinQueryPage(skinQuery));
    }

    @PostMapping("page-equip")
    @ApiOperation("获取装备分页信息")
    public GenericDto<IPage<NftMarketPlaceEqiupmentResp>>equipQueryPage(@RequestBody NftMarketPlaceEquipPageReq equipQuery){
        return GenericDto.success(nftMarketPlaceService.equipQueryPage(equipQuery));
    }

    @PostMapping("view")
    @ApiOperation("浏览量")
    public GenericDto<NftMarketPlaceDetailViewResp>view(@RequestBody NftMarketPlaceDetailViewReq req){
        return GenericDto.success(nftMarketPlaceService.view(req));
    }
}
