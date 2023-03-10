package com.seeds.game.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.enums.GameConditionEnum;
import com.seeds.admin.enums.GameEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.OpenNftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.INftPublicBackpackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * NFT公共背包  web端调用
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */

@Api(tags = "NFT公共背包接口，web调用")
@RestController
@RequestMapping("/web/nft-backpack")
public class NftPublicBackpackController {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;
    @Autowired
    private RemoteGameService remoteGameService;

    @PostMapping("page")
    @ApiOperation("获取分页信息")
    public GenericDto<IPage<NftPublicBackpackResp>> create(@Valid @RequestBody NftPublicBackpackPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.queryPage(req));
    }

    @PostMapping("list")
    @ApiOperation("获取列表信息，不分页")
    public GenericDto<List<NftPublicBackpackResp>> queryList(@Valid @RequestBody NftPublicBackpackPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.queryList(req));
    }


    @GetMapping("detail/{autoId}")
    @ApiOperation("详细信息")
    public GenericDto<NftPublicBackpackResp> detail(@PathVariable Integer autoId) {
        return GenericDto.success(nftPublicBackpackService.detail(autoId));
    }

    @PostMapping("create")
    @ApiOperation("新增")
    public GenericDto<Object> create(@RequestBody @Valid NftPublicBackpackReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        nftPublicBackpackService.create(req);
        return GenericDto.success(null);
    }

    @PostMapping("update")
    @ApiOperation("修改")
    public GenericDto<Object> update(@RequestBody @Valid NftPublicBackpackReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        nftPublicBackpackService.update(req);
        return GenericDto.success(null);
    }

    @PostMapping("distribute")
    @ApiOperation("分配")
    public GenericDto<OpenNftPublicBackpackDisResp> distribute(@RequestBody @Valid NftPublicBackpackDisReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<SysGameResp> gameDetail = remoteGameService.ucDetail(GameEnum.BLADERITE.getCode());
        // 游戏正在维护中，web端无法操作
        if (!Objects.isNull(gameDetail) && gameDetail.getData().getUpkeep().equals(GameConditionEnum.UNDER_MAINTENANCE.getValue())) {
            throw new GenericException(GameErrorCodeEnum.ERR_30001_GAME_IS_UNDER_MAINTENANCE);
        }
        return GenericDto.success(nftPublicBackpackService.distribute(req));
    }

    @PostMapping("take-back")
    @ApiOperation("收回")
    public GenericDto<Object> takeBack(@RequestBody @Valid NftPublicBackpackTakeBackReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<SysGameResp> gameDetail = remoteGameService.ucDetail(GameEnum.BLADERITE.getCode());
        // 游戏正在维护中，web端无法操作
        if (!Objects.isNull(gameDetail) && gameDetail.getData().getUpkeep().equals(GameConditionEnum.UNDER_MAINTENANCE.getValue())) {
            throw new GenericException(GameErrorCodeEnum.ERR_30001_GAME_IS_UNDER_MAINTENANCE);
        }
        nftPublicBackpackService.takeBack(req);
        return GenericDto.success(null);
    }

    @PostMapping("transfer")
    @ApiOperation("转移")
    public GenericDto<OpenNftPublicBackpackDisResp> transfer(@RequestBody @Valid NftPublicBackpackDisReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        GenericDto<SysGameResp> gameDetail = remoteGameService.ucDetail(GameEnum.BLADERITE.getCode());
        // 游戏正在维护中，web端无法操作
        if (!Objects.isNull(gameDetail) && gameDetail.getData().getUpkeep().equals(GameConditionEnum.UNDER_MAINTENANCE.getValue())) {
            throw new GenericException(GameErrorCodeEnum.ERR_30001_GAME_IS_UNDER_MAINTENANCE);
        }
        return GenericDto.success(nftPublicBackpackService.transfer(req));
    }

}
