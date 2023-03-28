package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenNftOwnershipTransferReq;
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
 * nft通知 前端控制器
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */

@Api(tags = "game调用,nft物品转移")
@RestController
@RequestMapping("/public/game/nft-event")
public class PublicNftEventController {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @PostMapping("/ownership-transfer")
    @ApiOperation("物品所有权转移")
    public GenericDto<Object> ownerTransfer(@RequestBody @Valid OpenNftOwnershipTransferReq req) {
        nftPublicBackpackService.ownerTransfer(req);
        return GenericDto.success(null);
    }
}
