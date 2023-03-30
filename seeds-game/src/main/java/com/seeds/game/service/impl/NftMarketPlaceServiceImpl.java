package com.seeds.game.service.impl;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.common.utils.RelativeDateFormat;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.config.SeedsApiConfig;
import com.seeds.game.dto.request.NftBuySuccessReq;
import com.seeds.game.dto.request.NftMarketPlaceDetailViewReq;
import com.seeds.game.dto.request.NftMarketPlaceEquipPageReq;
import com.seeds.game.dto.request.NftMarketPlaceSkinPageReq;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.external.EndAuctionMessageDto;
import com.seeds.game.dto.request.external.EnglishAuctionReqDto;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.*;
import com.seeds.game.mapper.NftEquipmentMapper;
import com.seeds.game.mapper.NftMarketOrderMapper;
import com.seeds.game.service.INftAttributeService;
import com.seeds.game.service.INftMarketOrderService;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.enums.*;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.*;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

    @Autowired
    private INftAuctionHouseSettingService nftAuctionHouseSettingService;

    @Autowired
    private INftAuctionHouseBidingService nftAuctionHouseBidingService;

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
    private INftActivityService nftActivityService;

    @Autowired
    private NftEquipmentMapper nftEquipmentMapper;

    @Autowired
    private GameCacheService gameCacheService;

    @Autowired
    private SeedsApiConfig seedsApiConfig;

    @Autowired
    private UcUserService ucUserService;

    @Autowired
    private NftMarketOrderMapper nftMarketOrderMapper;

    @Override
    public NftMarketPlaceDetailResp detail(Long id) {
        NftMarketPlaceDetailResp resp = new NftMarketPlaceDetailResp();
        // 查询marker order表
        NftMarketOrderEntity order = nftMarketOrderService.getById(id);
        if (order == null) {
            return resp;
        }
        resp.setId(order.getId());
        resp.setCurrentPrice(order.getPrice());
        NftEquipment nftEquipment = nftEquipmentMapper.getByMintAddress(order.getMintAddress());
        if (nftEquipment == null) {
            return resp;
        }
        NftPublicBackpackEntity publicBackpack = nftPublicBackpackService.queryByEqNftId(nftEquipment.getId());
        if (publicBackpack != null) {
            BeanUtils.copyProperties(publicBackpack, resp);
        }
        resp.setNftId(nftEquipment.getId());
        resp.setTokenId(nftEquipment.getTokenId());
        resp.setName(nftEquipment.getName());
        resp.setNumber("#" + nftEquipment.getTokenId());
        resp.setLastUpdated(nftEquipment.getUpdateTime());
        UcUserResp ucUserResp = null;
        try {
            GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftEquipment.getOwner());
            ucUserResp = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (ucUserResp != null) {
            resp.setOwnerId(ucUserResp.getId());
            resp.setOwnerName(ucUserResp.getNickname());
        }
        resp.setState(convertOrderState(nftEquipment.getIsDelete(), nftEquipment.getIsDeposit(), nftEquipment.getOnSale(), order.getStatus(), order.getOrderType()));
        NftAuctionHouseSetting auctionSetting = nftAuctionHouseSettingService.queryByListingId(order.getListingId());
        if (auctionSetting != null) {
            long time = System.currentTimeMillis() - (auctionSetting.getStart() + auctionSetting.getDuration() * 60 * 60 * 1000);
            resp.setTimeLeft(RelativeDateFormat.formatTime(time));
        }
        return resp;
    }

    @Override
    public void fixedPriceShelf(NftFixedPriceShelfReq req) {
        // 上架前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        shelfValidation(nftEquipment);
        // 不能重复上架
        if (WhetherEnum.YES.value() == nftEquipment.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10007_NFT_ITEM_IS_ALREADY_ON_SALE);
        }
        // 调用/api/chainOp/placeOrder通知，链上上架成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getPlaceOrderApi() + "?" + params;
        log.info("NFT一口价上架成功，开始通知， url:{}， params:{}", url, params);
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
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        shelfValidation(nftEquipment);
        // 不能重复上架
        if (WhetherEnum.YES.value() == nftEquipment.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10007_NFT_ITEM_IS_ALREADY_ON_SALE);
        }
        // 调用/api/auction/english通知，链上英式拍卖上架成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getEnglishOrderApi();
        EnglishAuctionReqDto dto  = new EnglishAuctionReqDto();
        BeanUtils.copyProperties(req, dto);
        dto.setOwnerAddress(nftEquipment.getOwner());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT英式拍卖上架成功，开始通知， url:{}， params:{}", url, param);
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
        // 下架前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        shelfValidation(nftEquipment);
        // 不能重复下架
        if (WhetherEnum.NO.value() == nftEquipment.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10010_NFT_ITEM_HAS_BEEN_REMOVAL);
        }
        // 调用/api/chainOp/cancelOrder通知，下架成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getCancelOrderApi() + "?" + params;
        log.info("NFT下架成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpRequest.get(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
        } catch (Exception e) {
            log.error("NFT下架成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void makeOffer(NftMakeOfferReq req) {
        // 出价成功前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        buyValidation(nftEquipment);
        // 拍卖结束不能出价
        NftAuctionHouseSetting auction = nftAuctionHouseSettingService.getById(req.getAuctionId());
        if (auction == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
        }
        if (WhetherEnum.YES.value() == auction.getIsFinished()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10012_NFT_ITEM_AUCTION_HAS_ENDED);
        }
        // 调用/api/auction/bid通知，出价成功
        String params = String.format("auctionId=%s&receipt=%s&sig=%s", req.getAuctionId(), req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionBid() + "?" + params;
        log.info("NFT拍卖出价成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpRequest.get(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
        } catch (Exception e) {
            log.error("NFT拍卖出价成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {
        // 购买成功前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        buyValidation(nftEquipment);
        // 调用/api/chainOp/buySuccess通知，购买成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getBuySuccess() + "?" + params;
        log.info("NFT购买成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpRequest.get(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
        } catch (Exception e) {
            log.error("NFT购买成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public List<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {
        List<NftMarketPlaceSkinResp> skinList = nftMarketOrderMapper.getSkinPage(skinQuery);
        skinList = skinList.stream().map(p->{
            NftMarketPlaceSkinResp resp = new NftMarketPlaceSkinResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber("#"+p.getTokenId());
            return resp;
                }).collect(Collectors.toList());
        return skinList;
    }

    @Override
    public List<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery) {
        List<NftMarketPlaceEqiupmentResp> equipList = nftMarketOrderMapper.getEquipPage(equipQuery);
        equipList = equipList.stream().map(p->{
            NftMarketPlaceEqiupmentResp resp = new NftMarketPlaceEqiupmentResp();
            resp.setNumber("#" + p.getTokenId());
            return resp;
        }).collect(Collectors.toList());
        return equipList;
    }

    @Override
    public NftMarketPlaceDetailViewResp view(NftMarketPlaceDetailViewReq req) {
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.detailForTokenId(req.getTokenId());
        Long userId = backpackEntity.getUserId();

        // 获取当前登录的用户的id
        Long currentUserId = UserContext.getCurrentUserId();
        LambdaQueryWrapper<NftPublicBackpackEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(req.getTokenId()),NftPublicBackpackEntity::getTokenId,req.getTokenId());
        NftPublicBackpackEntity one = nftPublicBackpackService.getOne(queryWrapper);

        // 第一视角，浏览量不变；第三视角，则浏览量+1
        if (one != null && !userId.equals(currentUserId)){
            // 属性表更新NFT浏览量
            LambdaUpdateWrapper<NftPublicBackpackEntity> updateWrap = new UpdateWrapper<NftPublicBackpackEntity>().lambda()
                    .setSql("`views`=`views`+1")
                    .eq(NftPublicBackpackEntity::getTokenId,req.getTokenId());
            nftPublicBackpackService.update(updateWrap);
        }
        NftMarketPlaceDetailViewResp resp = new NftMarketPlaceDetailViewResp();
        resp.setViews(backpackEntity.getViews());
        return resp;
    }

    @Override
    public List<NftMarketPlacePropsResp> propsQueryPage(NftMarketPlacePropsPageReq propsQuery) {

        List<NftMarketPlacePropsResp> propsList = nftMarketOrderMapper.getPropsPage(propsQuery);
        propsList = propsList.stream().map(p->{
            NftMarketPlacePropsResp propsResp = new NftMarketPlacePropsResp();
            BeanUtils.copyProperties(p,propsResp);
            propsResp.setNumber(p.getTokenId());
            return propsResp;
        }).collect(Collectors.toList());
        return propsList;
    }

    @Override
    public BigDecimal usdRate(String currency) {
        BigDecimal rate = null;
        if (CurrencyEnum.SOL.getCode().equals(currency)) {
            // 先从Redis获取
            String usdRate = gameCacheService.getUsdRate(currency);
            if (StringUtils.isBlank(usdRate)) {
                // 调用/market/token获取美元汇率
                String url = seedsApiConfig.getSolToUsdApi() + seedsApiConfig.getSolTokenAddress();
                log.info("获取美元汇率， url:{}， params:{}", url, currency);
                try {
                    HttpResponse response = HttpRequest.get(url)
                            .timeout(5 * 1000)
                            .header("Content-Type", "application/json")
                            .header("token", seedsApiConfig.getSolToken())
                            .execute();
                    String body = response.body();
                    SolToUsdRateResp resp = JSONUtil.toBean(body, SolToUsdRateResp.class);
                    rate = resp.getPriceUsdt();
                    gameCacheService.putUsdRate(currency, rate.toString());
                } catch (Exception e) {
                    log.error("获取美元汇率失败，message：{}", e.getMessage());
                }
            } else {
                rate = new BigDecimal(usdRate);
            }
        }
        return rate;
    }

    @Override
    public NftOfferResp offerPage(NftOfferPageReq req) {
        NftOfferResp resp = new NftOfferResp();
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        if (nftEquipment == null) {
            return resp;
        }
        NftMarketOrderEntity marketOrder = nftMarketOrderService.getById(nftEquipment.getOrderId());
        if (marketOrder == null || marketOrder.getListingId() == null) {
            return resp;
        }
        NftAuctionHouseSetting auctionSetting = nftAuctionHouseSettingService.queryByListingId(marketOrder.getListingId());
        if (auctionSetting == null) {
            return resp;
        }
        req.setAuctionId(auctionSetting.getId());
        req.setPrice(marketOrder.getPrice());
        req.setUsdRate(usdRate(CurrencyEnum.SOL.getCode()));
        String publicAddress = null;
        try {
            Long currentUserId = UserContext.getCurrentUserId();
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(currentUserId);
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        req.setPublicAddress(publicAddress);
        IPage<NftOfferResp.NftOffer> nftOfferPage = nftAuctionHouseBidingService.queryPage(req);
        List<NftOfferResp.NftOffer> records = nftOfferPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            resp.setHighestOffer(records.get(0).getPrice());
        }
        resp.setNftOffers(nftOfferPage);
        return resp;
    }

    @Override
    public IPage<NftActivityResp> activityPage(NftActivityPageReq req) {
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        if (nftEquipment == null) {
            return new Page<>(req.getCurrent(), req.getSize());
        }
        req.setMintAddress(nftEquipment.getMintAddress());
        return nftActivityService.queryPage(req);
    }

    @Override
    public void acceptOffer(NftAcceptOfferReq req) {
        NftAuctionHouseBiding auctionBiding = nftAuctionHouseBidingService.getById(req.getBidingId());
        if (auctionBiding == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
        }
        NftEquipment nftEquipment = nftEquipmentService.queryByMintAddress(auctionBiding.getMintAddress());
        if (nftEquipment == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 归属人验证
        ucUserService.ownerValidation(nftEquipment.getOwner());
        // 已托管不能接受报价
        if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10008_NFT_ITEM_IS_DEPOSIT);
        }
        // 调用/api/auction/endAuction通知，接受报价成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getEndAuction();
        EndAuctionMessageDto dto  = new EndAuctionMessageDto();
        BeanUtils.copyProperties(req, dto);
        dto.setAuctionId(auctionBiding.getAuctionId());
        dto.setMintAddress(auctionBiding.getMintAddress());
        dto.setNftId(nftEquipment.getId());
        dto.setToAddress(auctionBiding.getBuyer());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT接受报价成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpRequest.post(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            log.error("NFT接受报价成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void cancelOffer(NftCancelOfferReq req) {
        NftAuctionHouseBiding auctionBiding = nftAuctionHouseBidingService.getById(req.getBidingId());
        if (auctionBiding == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);}
        // offer归属人验证
        ucUserService.ownerValidation(auctionBiding.getBuyer());
        // 调用/api/auction/cancelBid通知，取消出价成功
        String params = String.format("bidingId=%s&receipt=%s&signature=%s", req.getBidingId(), req.getReceipt(), req.getSignature());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionCancel() + "?" + params;
        log.info("NFT取消出价成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpRequest.get(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
        } catch (Exception e) {
            log.error("NFT取消出价成功通知失败，message：{}", e.getMessage());
        }
    }

    private void shelfValidation(NftEquipment nftEquipment) {
        if (nftEquipment == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 归属人才能上架
        ucUserService.ownerValidation(nftEquipment.getOwner());
        // NFT装备还未生成
        if (WhetherEnum.NO.value() == nftEquipment.getNftGenerated()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10009_NFT_ITEM_HAS_NOT_BEEN_GENERATED);
        }
        // 已托管不能上架
        if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10008_NFT_ITEM_IS_DEPOSIT);
        }
    }

    private void buyValidation(NftEquipment nftEquipment) {
        if (nftEquipment == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 已托管不能购买成功
        if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10008_NFT_ITEM_IS_DEPOSIT);
        }

        // 售卖中才能购买成功
        if (WhetherEnum.NO.value() == nftEquipment.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10010_NFT_ITEM_HAS_BEEN_REMOVAL);
        }

        // 归属人不能购买自己的NFT
        Long currentUserId = UserContext.getCurrentUserId();
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(currentUserId);
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (nftEquipment.getOwner().equals(publicAddress)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10013_NFT_ITEM_ALREADY_HAS);
        }

    }

    private Integer convertOrderState(Integer isBurned, Integer isDeposit, Integer onSale, Integer orderStatus, Integer orderType) {
        int state;
        if (WhetherEnum.YES.value() == isBurned) {
            return NftStateEnum.BURNED.getCode();
        }
        if (WhetherEnum.YES.value() == isDeposit) {
            state = NftStateEnum.DEPOSITED.getCode();
        } else {
            if (WhetherEnum.YES.value() == onSale) {
                if (NftOrderTypeEnum.BUY_NOW.getCode() == orderType) {
                    state = NftStateEnum.ON_SHELF.getCode();
                } else {
                    state = NftStateEnum.ON_AUCTION.getCode();
                }
            } else {
                // 下架但挂单中，结算中
                if (NftOrderStatusEnum.PENDING.getCode() == orderStatus) {
                    state = NftStateEnum.IN_SETTLEMENT.getCode();
                } else {
                    state = NftStateEnum.UNDEPOSITED.getCode();
                }
            }
        }
        return state;
    }

}
