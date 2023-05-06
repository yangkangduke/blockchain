package com.seeds.game.controller;

import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.enums.GameConditionEnum;
import com.seeds.admin.enums.GameEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.dto.request.NftDepositCheckReq;
import com.seeds.game.dto.request.NftDepositedReq;
import com.seeds.game.dto.request.NftUnDepositedReq;
import com.seeds.game.dto.request.internal.NftBackpackWebPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackWebResp;
import com.seeds.game.dto.response.NftType;
import com.seeds.game.dto.response.NftTypeNum;
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


    @GetMapping("type-num")
    @ApiOperation("统计各个类型的数量")
    public GenericDto<List<NftTypeNum>> typeNum() {
        return GenericDto.success(nftPublicBackpackService.typeNum());
    }

    @GetMapping("type-list")
    @ApiOperation("获取分类列表, type：1装备 2道具 3英雄 ")
    public GenericDto<List<NftType>> getNftTypeList(Integer type) {
        return GenericDto.success(nftPublicBackpackService.getNftTypeList(type));
    }

    @PostMapping("page")
    @ApiOperation("获取列表信息")
    public GenericDto<List<NftPublicBackpackWebResp>> getPage(@Valid @RequestBody NftBackpackWebPageReq req) {
        req.setUserId(UserContext.getCurrentUserId());
        return GenericDto.success(nftPublicBackpackService.getPageForWeb(req));
    }

//    @PostMapping("list")
//    @ApiOperation("获取列表信息，不分页")
//    public GenericDto<List<NftPublicBackpackResp>> queryList(@Valid @RequestBody NftPublicBackpackPageReq req) {
//        req.setUserId(UserContext.getCurrentUserId());
//        return GenericDto.success(nftPublicBackpackService.queryList(req));
//    }


//    @GetMapping("detail/{autoId}")
//    @ApiOperation("详细信息")
//    public GenericDto<NftPublicBackpackResp> detail(@PathVariable Integer autoId) {
//        return GenericDto.success(nftPublicBackpackService.detail(autoId));
//    }

//    @PostMapping("create")
//    @ApiOperation("新增")
//    public GenericDto<Object> create(@RequestBody @Valid NftPublicBackpackReq req) {
//        req.setUserId(UserContext.getCurrentUserId());
//        nftPublicBackpackService.create(req);
//        return GenericDto.success(null);
//    }
//
//    @PostMapping("update")
//    @ApiOperation("修改")
//    public GenericDto<Object> update(@RequestBody @Valid NftPublicBackpackReq req) {
//        req.setUserId(UserContext.getCurrentUserId());
//        nftPublicBackpackService.update(req);
//        return GenericDto.success(null);
//    }

    @PostMapping("distribute")
    @ApiOperation("分配")
    public GenericDto<Integer> distributeBatch(@RequestBody @Valid List<NftPublicBackpackDisReq> reqs) {

        GenericDto<SysGameResp> gameDetail = remoteGameService.ucDetail(GameEnum.BLADERITE.getCode());
        // 游戏正在维护中，web端无法操作
        if (!Objects.isNull(gameDetail) && gameDetail.getData().getUpkeep().equals(GameConditionEnum.UNDER_MAINTENANCE.getValue())) {
            throw new GenericException(GameErrorCodeEnum.ERR_30001_GAME_IS_UNDER_MAINTENANCE);
        }
        if (reqs.size() == 1) {
            NftPublicBackpackDisReq disReq = reqs.get(0);
            nftPublicBackpackService.distribute(disReq);
            return GenericDto.success(1);
        }
        return GenericDto.success(nftPublicBackpackService.distributeBatch(reqs));
    }

    @PostMapping("take-back")
    @ApiOperation("收回")
    public GenericDto<Integer> takeBackBatch(@RequestBody @Valid List<NftPublicBackpackTakeBackReq> reqs) {

        GenericDto<SysGameResp> gameDetail = remoteGameService.ucDetail(GameEnum.BLADERITE.getCode());
        // 游戏正在维护中，web端无法操作
        if (!Objects.isNull(gameDetail) && gameDetail.getData().getUpkeep().equals(GameConditionEnum.UNDER_MAINTENANCE.getValue())) {
            throw new GenericException(GameErrorCodeEnum.ERR_30001_GAME_IS_UNDER_MAINTENANCE);
        }
        if (reqs.size() == 1) {
            nftPublicBackpackService.takeBack(reqs.get(0));
            return GenericDto.success(1);
        }
        return GenericDto.success(nftPublicBackpackService.takeBackBatch(reqs));
    }

    @PostMapping("transfer")
    @ApiOperation("转移")
    public GenericDto<Integer> transferBatch(@RequestBody @Valid List<NftPublicBackpackDisReq> reqs) {
        GenericDto<SysGameResp> gameDetail = remoteGameService.ucDetail(GameEnum.BLADERITE.getCode());
        // 游戏正在维护中，web端无法操作
        if (!Objects.isNull(gameDetail) && gameDetail.getData().getUpkeep().equals(GameConditionEnum.UNDER_MAINTENANCE.getValue())) {
            throw new GenericException(GameErrorCodeEnum.ERR_30001_GAME_IS_UNDER_MAINTENANCE);
        }
        if (reqs.size() == 1) {
            nftPublicBackpackService.transfer(reqs.get(0));
            return GenericDto.success(1);
        }
        return GenericDto.success(nftPublicBackpackService.transferBatch(reqs));
    }

    @PostMapping("deposit-check")
    @ApiOperation("托管校验")
    public GenericDto<Object> depositCheck(@Valid @RequestBody NftDepositCheckReq req) {
        return nftPublicBackpackService.depositCheck(req);
    }

    @PostMapping("deposited")
    @ApiOperation("托管")
    public GenericDto<Object> deposited(@Valid @RequestBody NftDepositedReq req) {
        nftPublicBackpackService.deposited(req);
        return GenericDto.success(null);
    }

    @PostMapping("un-deposited")
    @ApiOperation("取消托管")
    public GenericDto<Object> unDeposited(@Valid @RequestBody NftUnDepositedReq req) {
        nftPublicBackpackService.unDeposited(req);
        return GenericDto.success(null);
    }

    @PostMapping("skin-un-deposited")
    @ApiOperation("皮肤取消托管")
    public GenericDto<Object> skinUnDeposited(@Valid @RequestBody NftUnDepositedReq req) {
        nftPublicBackpackService.skinUnDeposited(req);
        return GenericDto.success(null);
    }
}
