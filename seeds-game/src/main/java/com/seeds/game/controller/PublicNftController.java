package com.seeds.game.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.dto.request.OpenNftHonorModifyReq;
import com.seeds.game.dto.request.OpenNftModifyReq;
import com.seeds.game.dto.request.OpenNftPageReq;
import com.seeds.game.dto.request.OpenNftSettlementReq;
import com.seeds.uc.dto.response.NFTAuctionResp;
import com.seeds.uc.dto.response.NFTOfferResp;
import com.seeds.uc.feign.RemoteNFTService;
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
    private RemoteNftService adminRemoteNftService;

    @Autowired
    private RemoteNFTService ucRemoteNftService;

    @PostMapping("modify")
    @ApiOperation("NFT修改")
    public GenericDto<Object> modify(@Valid @RequestBody OpenNftModifyReq req) {
        return adminRemoteNftService.modify(req);
    }

    @PostMapping("honor-modify")
    @ApiOperation("NFT战绩更新")
    public GenericDto<Object> honorModify(@Valid @RequestBody OpenNftHonorModifyReq req) {
        return adminRemoteNftService.honorModify(req.getHonorList());
    }

    @PostMapping("settlement")
    @ApiOperation("NFT结算")
    public GenericDto<Object> lock(@Valid @RequestBody OpenNftSettlementReq req) {
        return adminRemoteNftService.settlement(req);
    }

    @PostMapping("page")
    @ApiOperation("NFT列表")
    public GenericDto<Page<SysNftResp>> pageApi(@Valid @RequestBody OpenNftPageReq req) {
        Assert.notNull(req.getGameId(), "The game id can not be empty");
        return adminRemoteNftService.pageApi(req);
    }

    @GetMapping("/detail")
    @ApiOperation("NFT详情")
    public GenericDto<SysNftDetailResp> detailApi(@RequestParam Long id,
                                                  @RequestParam String accessKey,
                                                  @RequestParam String signature,
                                                  @RequestParam Long timestamp) {
        GenericDto<SysNftDetailResp> resp = adminRemoteNftService.detailApi(id);
        SysNftDetailResp nftDetailResp = resp.getData();
        GenericDto<NFTAuctionResp> actionInfo = ucRemoteNftService.actionInfo(id, nftDetailResp.getOwnerId());
        nftDetailResp.setAuctionFlag(actionInfo.getData().getAuctionFlag());
        return resp;
    }

    @GetMapping("/offer-list")
    @ApiOperation("NFT出价列表")
    public GenericDto<List<NFTOfferResp>> offerList(@RequestParam Long id,
                                                    @RequestParam String accessKey,
                                                    @RequestParam String signature,
                                                    @RequestParam Long timestamp) {
        return GenericDto.success(ucRemoteNftService.offerList(id));
    }
}
