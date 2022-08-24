package com.seeds.uc.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.common.web.inner.Inner;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.model.UcUserAccountActionHistory;
import com.seeds.uc.service.IUcUserAccountActionHistoryService;
import com.seeds.uc.service.IUcUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        String price;
        SysNftDetailResp sysNftDetailResp;
        try {
            GenericDto<SysNftDetailResp> sysNftDetailRespGenericDto = remoteNftService.ucDetail(buyReq.getNftId());
            sysNftDetailResp = sysNftDetailRespGenericDto.getData();
            price = sysNftDetailResp.getPrice();
        } catch (Exception e) {
            throw new GenericException(UcErrorCodeEnum.ERR_18005_ACCOUNT_BUY_FAIL);
        }
        // todo 判断nft是否是上架状态、nft是否已经购买过了
        // 检查账户里面的金额是否足够支付
        if (!ucUserAccountService.checkBalance(currentUserId, new BigDecimal(price))) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        ucUserAccountService.buyNFTFreeze(sysNftDetailResp);

        return GenericDto.success(null);
    }


}
