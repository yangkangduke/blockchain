package com.seeds.uc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.request.UcSwitchReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.enums.NftStatusEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.enums.UcErrorCodeEnum;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * NFT交易 前端控制器
 * </p>
 *
 * @author yk
 * @since 2022-08-19
 */
@RestController
@RequestMapping("/nft")
@Api(tags = "NFT交易")
@Slf4j
public class OpenNFTController {

    @Autowired
    private IUcUserAccountService ucUserAccountService;
    @Autowired
    private RemoteNftService remoteNftService;


    @PostMapping("/buy")
    @ApiOperation(value = "购买", notes = "购买")
    public GenericDto<Object> buyNFT(@Valid @RequestBody NFTBuyReq buyReq) {
        Long currentUserId = UserContext.getCurrentUserId();
        BigDecimal price;
        SysNftDetailResp sysNftDetailResp;
        try {
            GenericDto<SysNftDetailResp> sysNftDetailRespGenericDto = remoteNftService.ucDetail(buyReq.getNftId());
            sysNftDetailResp = sysNftDetailRespGenericDto.getData();
            price = sysNftDetailResp.getPrice();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        //  判断nft是否是上架状态、nft是否已经购买过了
        if (!Objects.isNull(sysNftDetailResp)) {
            if (sysNftDetailResp.getStatus() != NftStatusEnum.ON_SALE.getCode()) {
                throw new GenericException(UcErrorCodeEnum.ERR_18006_ACCOUNT_BUY_FAIL_INVALID_NFT_STATUS);
            }
        }


        // 检查账户里面的金额是否足够支付
        if (!ucUserAccountService.checkBalance(currentUserId, price)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        ucUserAccountService.buyNFTFreeze(sysNftDetailResp);

        return GenericDto.success(null);
    }

    @PostMapping("/owner-page")
    @ApiOperation(value = "用户拥有分页查询", notes = "用户拥有分页查询")
    public GenericDto<Page<SysNftResp>> ownerPage(@Valid @RequestBody SysNftPageReq query) {
        Long currentUserId = UserContext.getCurrentUserId();
        query.setUserId(currentUserId);
        return remoteNftService.ucPage(query);
    }

    @PostMapping("/uc-switch")
    @ApiOperation("uc上架/下架")
    public GenericDto<Object> ucUpOrDown(@Valid @RequestBody UcSwitchReq req) {
        Long currentUserId = UserContext.getCurrentUserId();
        req.setUcUserId(currentUserId);
        return remoteNftService.ucUpOrDown(req);
    }

}
