package com.seeds.game.service.impl;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.config.SeedsApiConfig;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.common.dto.PageReq;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.external.EnglishAuctionReqDto;
import com.seeds.game.dto.response.NftActivityResp;
import com.seeds.game.dto.response.NftMarketPlaceDetailResp;
import com.seeds.game.dto.response.NftMarketPlaceDetailViewResp;
import com.seeds.game.dto.response.NftMarketPlaceEqiupmentResp;
import com.seeds.game.dto.response.NftMarketPlaceSkinResp;
import com.seeds.game.entity.NftEquipment;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.service.INftAttributeService;
import com.seeds.game.service.INftMarketOrderService;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.enums.*;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.*;
import com.seeds.game.dto.response.NftOfferResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private INftMarketOrderService nftMarketOrderService;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private INftAttributeService nftAttributeService;

    @Autowired
    private INftEquipmentService nftEquipmentService;

    @Autowired
    private SeedsApiConfig seedsApiConfig;

    @Override
    public NftMarketPlaceDetailResp detail(Long id) {
        return null;
    }

    @Override
    public void fixedPriceShelf(NftFixedPriceShelfReq req) {
        // 上架前的校验
        preShelfValidation(req.getMintAddress());
        // 调用/api/chainOp/placeOrder通知，链上上架成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getPlaceOrderApi() + "?" + params;
        log.info("一口价上架成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpRequest.get(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
        } catch (Exception e) {
            log.error("一口价上架成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void britishAuctionShelf(NftBritishAuctionShelfReq req) {
        // 上架前的校验
        String publicAddress = preShelfValidation(req.getMintAddress());
        // 调用/api/auction/english通知，链上英式拍卖上架成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getEnglishOrderApi();
        EnglishAuctionReqDto dto  = new EnglishAuctionReqDto();
        BeanUtils.copyProperties(req, dto);
        dto.setOwnerAddress(publicAddress);
        String param = JSONUtil.toJsonStr(dto);
        log.info("英式拍卖上架成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpRequest.post(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            log.error("英式拍卖上架成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void shelved(NftShelvedReq req) {

    }

    @Override
    public void makeOffer(NftMakeOfferReq req) {

    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {

    }

    @Override
    public IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {
        return null;
    }

    @Override
    public IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery) {
        return null;
    }

    @Override
    public NftMarketPlaceDetailViewResp view(NftMarketPlaceDetailViewReq req) {
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForMintAddress(req.getMintAddress());
        Long userId = backpackEntity.getUserId();

        // 获取当前登录的用户的id
        Long currentUserId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<NftPublicBackpackEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(req.getMintAddress()),NftPublicBackpackEntity::getContractAddress,req.getMintAddress());
        NftPublicBackpackEntity one = nftPublicBackpackService.getOne(queryWrapper);

        // 第一视角，浏览量不变；第三视角，则浏览量+1
        if (one != null && !userId.equals(currentUserId)){
            // 属性表更新NFT浏览量
            LambdaUpdateWrapper<NftPublicBackpackEntity> updateWrap = new UpdateWrapper<NftPublicBackpackEntity>().lambda()
                    .setSql("`views`=`views`+1")
                    .eq(NftPublicBackpackEntity::getContractAddress,req.getMintAddress());
            nftPublicBackpackService.update(updateWrap);
        }
        NftMarketPlaceDetailViewResp resp = new NftMarketPlaceDetailViewResp();
        resp.setViews(backpackEntity.getViews());
        return resp;
    }

    @Override
    public NftOfferResp offerPage(PageReq req) {
        return null;
    }

    @Override
    public NftActivityResp activityPage(PageReq req) {
        return null;
    }

    @Override
    public void acceptOffer(NftAcceptOfferReq req) {

    }

    @Override
    public void cancelOffer(NftCancelOfferReq req) {

    }

    private String preShelfValidation(String mintAddress) {
        NftEquipment nftEquipment = nftEquipmentService.queryByMintAddress(mintAddress);
        if (nftEquipment == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // NFT装备还未生成
        if (WhetherEnum.NO.value() == nftEquipment.getNftGenerated()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10009_NFT_ITEM_HAS_NOT_BEEN_GENERATED);
        }
        // 不能重复上架
        if (WhetherEnum.YES.value() == nftEquipment.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10007_NFT_ITEM_IS_ALREADY_ON_SALE);
        }
        // 已托管不能上架
        if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10008_NFT_ITEM_IS_DEPOSIT);
        }
        // 归属人才能上架
        Long currentUserId = UserContext.getCurrentUserId();
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(currentUserId);
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (!nftEquipment.getOwner().equals(publicAddress)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        return publicAddress;
    }

}
