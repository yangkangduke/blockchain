package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.internal.NftEventAddReq;
import com.seeds.game.service.INftEventService;
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

@Api(tags = "game调用,to-nft")
@RestController
@RequestMapping("/web/game/nft-event")
public class OpenNftEventController {

    @Autowired
    private INftEventService nftEventService;

    @PostMapping("/to-nft")
    @ApiOperation("toNFT")
    public GenericDto<Object> toNft(@Valid @RequestBody NftEventAddReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        nftEventService.toNft(req);
        return GenericDto.success(null);
    }

}
