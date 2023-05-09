package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.MintSuccessReq;
import com.seeds.game.dto.request.NftBackpakcUpdateStateReq;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * NFT公共背包  web端调用
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */

@Api(tags = "NFT公共背包接口，补偿调用")
@RestController
@RequestMapping("/public/web/nft-backpack")
public class PublicNftPublicBackpackController {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @PostMapping("update-state")
    @ApiOperation("更新背包状态，补偿时调用")
    public GenericDto<Object> updateState(@Valid @RequestBody NftBackpakcUpdateStateReq req) {
        nftPublicBackpackService.updateState(req);
        return GenericDto.success(null);
    }

    @PostMapping("insert-callback")
    @ApiOperation("新增回调，补偿时调用")
    public GenericDto<Object> insertCallback(@RequestBody @Valid MintSuccessReq req) {
        nftPublicBackpackService.insertCallback(req);
        return GenericDto.success(null);
    }
}
