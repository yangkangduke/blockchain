package com.seeds.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.feign.RemoteAccountTradeService;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenNftHonorModifyReq;
import com.seeds.game.dto.request.OpenNftModifyReq;
import com.seeds.game.dto.request.OpenNftPageReq;
import com.seeds.game.dto.request.OpenNftSettlementReq;
import com.seeds.account.dto.resp.NftAuctionResp;
import com.seeds.account.dto.resp.NftOfferResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 提供外部调用的NFT相关接口
 * @author hang.yu
 * @date 2022/10/08
 */
@Slf4j
@Api(tags = "NFT外部调用开放")
@RestController
@RequestMapping("/public/nft")
public class PublicNftController {

    @Autowired
    private RemoteAccountTradeService remoteAccountTradeService;

    @Autowired
    private RemoteNftService adminRemoteNftService;

    @PostMapping("modify")
    @ApiOperation("NFT修改")
    public GenericDto<Object> modify(@Valid @RequestBody OpenNftModifyReq req) {
        GenericDto<Object> result = adminRemoteNftService.modify(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("honor-modify")
    @ApiOperation("NFT战绩更新")
    public GenericDto<Object> honorModify(@Valid @RequestBody OpenNftHonorModifyReq req) {
        GenericDto<Object> result = adminRemoteNftService.honorModify(req.getHonorList());
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("settlement")
    @ApiOperation("NFT结算")
    public GenericDto<Object> lock(@Valid @RequestBody OpenNftSettlementReq req) {
        GenericDto<Object> result = adminRemoteNftService.settlement(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @PostMapping("page")
    @ApiOperation("NFT列表")
    public GenericDto<Page<SysNftResp>> pageApi(@Valid @RequestBody OpenNftPageReq req) {
        Assert.notNull(req.getGameId(), "The game id can not be empty");
        GenericDto<Page<SysNftResp>> result = adminRemoteNftService.pageApi(req);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

    @GetMapping("/detail")
    @ApiOperation("NFT详情")
    public GenericDto<SysNftDetailResp> detailApi(@RequestParam Long id,
                                                  @RequestParam String accessKey,
                                                  @RequestParam String signature,
                                                  @RequestParam Long timestamp) {
        GenericDto<SysNftDetailResp> resp = adminRemoteNftService.detailApi(id);
        if (!resp.isSuccess()) {
            return GenericDto.failure(resp.getMessage(),
                    resp.getCode());
        }
        SysNftDetailResp nftDetailResp = resp.getData();
        GenericDto<NftAuctionResp> actionInfo = remoteAccountTradeService.actionInfo(id, nftDetailResp.getOwnerId());
        nftDetailResp.setAuctionFlag(actionInfo.getData().getAuctionFlag());
        return resp;
    }

    @GetMapping("/offer-list")
    @ApiOperation("NFT出价列表")
    public GenericDto<List<NftOfferResp>> offerList(@RequestParam Long id,
                                                    @RequestParam String accessKey,
                                                    @RequestParam String signature,
                                                    @RequestParam Long timestamp) {
        GenericDto<List<NftOfferResp>> result = remoteAccountTradeService.offerList(id);
        if (!result.isSuccess()) {
            return GenericDto.failure(result.getMessage(),
                    result.getCode());
        }
        return result;
    }

}