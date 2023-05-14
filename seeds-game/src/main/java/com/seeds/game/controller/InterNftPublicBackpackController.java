package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import com.seeds.game.dto.request.internal.NftPublicBackpackDto;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.service.NftMarketPlaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hewei
 * @date 2023/5/9
 */

@RestController
@RequestMapping("/inter-game/nft")
@Api(tags = "公共背包插入数据，内部调用")
public class InterNftPublicBackpackController {

    @Autowired
    private INftPublicBackpackService backpackService;
    @Autowired
    private NftMarketPlaceService nftMarketPlaceService;

    @PostMapping("/insert-backpack")
    @Inner
    public GenericDto<Object> insertBackpack(@RequestBody List<NftPublicBackpackDto> backpackEntities) {
        backpackService.insertBackpack(backpackEntities);
        return GenericDto.success(null);
    }

    @GetMapping("usd-rate/{currency}")
    @ApiOperation("获取美元汇率")
    @Inner
    public GenericDto<BigDecimal> usdRate(@PathVariable String currency) {
        return GenericDto.success(nftMarketPlaceService.usdRate(currency));
    }
}
