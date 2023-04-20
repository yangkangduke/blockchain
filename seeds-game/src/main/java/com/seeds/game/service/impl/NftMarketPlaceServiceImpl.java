package com.seeds.game.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
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
import com.seeds.game.config.SolanaConfig;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.external.EndAuctionMessageDto;
import com.seeds.game.dto.request.external.EnglishAuctionReqDto;
import com.seeds.game.dto.request.external.TransferSolMessageDto;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.*;
import com.seeds.game.enums.*;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftEquipmentMapper;
import com.seeds.game.mapper.NftMarketOrderMapper;
import com.seeds.game.service.*;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.dto.response.UserInfoResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

    private  static final Integer maxDurability = 20;

    @Autowired
    private INftAuctionHouseSettingService nftAuctionHouseSettingService;

    @Autowired
    private INftAuctionHouseListingService nftAuctionHouseListingService;

    @Autowired
    private INftAuctionHouseBidingService nftAuctionHouseBidingService;

    @Autowired
    @Lazy
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private INftMarketOrderService nftMarketOrderService;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private INftAttributeService nftAttributeService;

    @Autowired
    private INftFeeRecordService nftFeeRecordService;

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
    private SolanaConfig solanaConfig;

    @Autowired
    private NftMarketOrderMapper nftMarketOrderMapper;

    @Override
    public NftMarketPlaceDetailResp detail(Long nftId) {
        NftMarketPlaceDetailResp resp = new NftMarketPlaceDetailResp();
        // 查询NFT
        NftEquipment nftEquipment = nftEquipmentMapper.getById(nftId);
        if (nftEquipment == null) {
            return resp;
        }
        NftPublicBackpackEntity publicBackpack = nftPublicBackpackService.queryByEqNftId(nftId);
        if (publicBackpack != null) {
            BeanUtils.copyProperties(publicBackpack, resp);
            resp.setAttributes(JSONUtil.parseObj(publicBackpack.getAttributes()));
            resp.setMetadata(JSONUtil.toList(publicBackpack.getMetadata(), cn.hutool.json.JSONObject.class));
            resp.setReferencePrice(publicBackpack.getProposedPrice());
            resp.setServerRoleId(publicBackpack.getServerRoleId());
            resp.setAutoId(publicBackpack.getAutoId());
            resp.setEquipmentName(publicBackpack.getName());
            if (NftTypeEnum.hero.getCode() == resp.getType()) {
                resp.setContractAddress(solanaConfig.getSkinContractAddress());
            } else {
                resp.setContractAddress(solanaConfig.getEquipmentContractAddress());
            }
        }
        NftAttributeEntity nftAttribute = nftAttributeService.queryByNftId(nftId);
        if (nftAttribute != null) {
            BeanUtils.copyProperties(nftAttribute, resp);
        }
        UcUserResp ucUserResp = null;
        try {
            GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftEquipment.getOwner());
            ucUserResp = result.getData();
            resp.setIsOwner(UserContext.getCurrentUserId().equals(ucUserResp.getId()) ? 1 : 0);
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (ucUserResp != null) {
            resp.setOwnerId(ucUserResp.getId());
            resp.setOwnerName(ucUserResp.getNickname());
        }
        NftAuctionHouseSetting auctionSetting = null;
        // 拍卖中
        if (NftOrderTypeEnum.ON_AUCTION.getCode() == nftEquipment.getOnSale()) {
            auctionSetting = nftAuctionHouseSettingService.getById(nftEquipment.getAuctionId());
            if (auctionSetting != null) {
                BeanUtils.copyProperties(auctionSetting, resp);
                long time = (auctionSetting.getStartTime() + auctionSetting.getDuration() * 60 * 60 * 1000) - System.currentTimeMillis();
                resp.setTimeLeft(RelativeDateFormat.formatTime(time));
                NftAuctionHouseListing auctionListing = nftAuctionHouseListingService.getById(auctionSetting.getListingId());
                if (auctionListing != null) {
                    resp.setListReceipt(auctionListing.getReceipt());
                }
                resp.setCurrentPrice(auctionSetting.getStartPrice());

                BigDecimal currentPrice = nftAuctionHouseBidingService.queryAuctionCurrentPrice(nftEquipment.getAuctionId());
                if (currentPrice != null) {
                    resp.setCurrentPrice(currentPrice);
                }

                BigDecimal difference = resp.getCurrentPrice().subtract(auctionSetting.getStartPrice())
                        .divide(auctionSetting.getStartPrice(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(0, RoundingMode.HALF_UP);
                if (resp.getCurrentPrice().compareTo(auctionSetting.getStartPrice()) > 0) {
                    resp.setPriceDifference(difference + "% above");
                } else {
                    resp.setPriceDifference(difference.abs() + "% below");
                }
            }
        } else if (NftOrderTypeEnum.BUY_NOW.getCode() == nftEquipment.getOnSale()) {
            // 固定上架
            NftMarketOrderEntity order = nftMarketOrderService.getById(nftEquipment.getOrderId());
            if (order != null) {
                resp.setId(order.getId());
                resp.setCurrentPrice(order.getPrice());
                resp.setListReceipt(order.getListReceipt());
            }
        }
        BeanUtils.copyProperties(nftEquipment, resp);
        resp.setOwnerAddress(nftEquipment.getOwner());
        resp.setNftId(nftEquipment.getId());
        resp.setNumber("#" + nftEquipment.getTokenId());
        resp.setState(convertOrderState(nftEquipment, auctionSetting));
        Long lastUpdated = nftActivityService.queryLastUpdateTime(nftEquipment.getMintAddress());
        if (lastUpdated != null) {
            resp.setLastUpdated(RelativeDateFormat.convert(new Date(lastUpdated), new Date()));
        } else {
            resp.setLastUpdated(RelativeDateFormat.convert(new Date(nftEquipment.getUpdateTime()), new Date()));
        }
        BeanUtils.copyProperties(solanaConfig, resp);
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

        //更新背包状态(undeposited => on shelf)
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.ON_SHELF.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/chainOp/placeOrder通知，链上上架成功
        String params = String.format("feeHash=%s&receipt=%s&sig=%s", req.getFeeHash(), req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getPlaceOrderApi() + "?" + params;
        log.info("NFT一口价上架成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT一口价上架成功通知返回，result:{}", response.body());
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

        //更新背包状态(undeposited => on auction)
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.ON_ACTION.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/auction/english通知，链上英式拍卖上架成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getEnglishOrderApi();
        EnglishAuctionReqDto dto  = new EnglishAuctionReqDto();
        BeanUtils.copyProperties(req, dto);
        dto.setOwnerAddress(nftEquipment.getOwner());
        dto.setMintAddress(nftEquipment.getMintAddress());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT英式拍卖上架成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("NFT英式拍卖上架成功通知返回，result:{}", response.body());
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

        //更新背包状态 undeposited
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/chainOp/cancelOrder通知，下架成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getCancelOrderApi() + "?" + params;
        log.info("NFT下架成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT下架成功通知返回，result:{}", response.body());
        } catch (Exception e) {
            log.error("NFT下架成功通知失败，message：{}", e.getMessage());
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setOrderId(Long.valueOf(nftEquipment.getOrderId()));
        refundFee(feeReq);
    }

    @Override
    public void cancelAuction(NftCancelAuctionReq req) {
        // 取消拍卖前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        shelfValidation(nftEquipment);
        // 没有进行中的拍卖
        if (nftEquipment.getAuctionId() <= 0) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
        }
        NftAuctionHouseSetting auction = nftAuctionHouseSettingService.getById(nftEquipment.getAuctionId());
        if (auction == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
        }
        // 拍卖已结束或已取消
        if (auction.getIsFinished() != null && WhetherEnum.NO.value() != auction.getIsFinished()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10012_NFT_ITEM_AUCTION_HAS_ENDED);
        }

        // 更新背包状态 undeposited
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/chainOp/cancelOrder通知，取消拍卖成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionCancel();
        EndAuctionMessageDto dto  = new EndAuctionMessageDto();
        BeanUtils.copyProperties(req, dto);
        dto.setAuctionId(nftEquipment.getAuctionId());
        dto.setMintAddress(nftEquipment.getMintAddress());
        dto.setNftId(nftEquipment.getId());
        dto.setToAddress(nftEquipment.getOwner());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT取消拍卖成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("NFT取消拍卖成功通知返回，result:{}", response.body());
        } catch (Exception e) {
            log.error("NFT取消拍卖成功通知失败，message：{}", e.getMessage());
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setAuctionId(nftEquipment.getAuctionId());
        refundFee(feeReq);
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
        // 拍卖到期不能出价
        if (System.currentTimeMillis() > auction.getStartTime() + auction.getDuration() * 60 * 60 * 1000) {
            throw new GenericException(GameErrorCodeEnum.ERR_10017_NFT_ITEM_IN_SETTLEMENT);
        }
        if (auction.getIsFinished() != null && WhetherEnum.NO.value() != auction.getIsFinished()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10012_NFT_ITEM_AUCTION_HAS_ENDED);
        }
        // 调用/api/auction/bid通知，出价成功
        String params = String.format("auctionId=%s&receipt=%s&sig=%s", req.getAuctionId(), req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionBid() + "?" + params;
        log.info("NFT拍卖出价成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT拍卖出价成功通知返回，result:{}", response.body());
        } catch (Exception e) {
            log.error("NFT拍卖出价成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {
        // 购买成功前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        buyValidation(nftEquipment);

        //更新背包状态 undeposited  userId,owner
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        Long userId = UserContext.getCurrentUserId();
        backpackEntity.setUserId(userId);
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(userId);
            String owner = result.getData();
            backpackEntity.setOwner(owner);
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        backpackEntity.setUserId(userId);
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/chainOp/buySuccess通知，购买成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getBuySuccess() + "?" + params;
        log.info("NFT购买成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT购买成功通知返回，result:{}", response.body());
        } catch (Exception e) {
            log.error("NFT购买成功通知失败，message：{}", e.getMessage());
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setOrderId(Long.valueOf(nftEquipment.getOrderId()));
        refundFee(feeReq);
    }

    @Override
    public IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {

        Page<NftMarketPlaceSkinResp> page = new Page<>();
        page.setCurrent(skinQuery.getCurrent());
        page.setSize(skinQuery.getSize());
        IPage<NftMarketPlaceSkinResp> skinPage = nftMarketOrderMapper.getSkinPage(page, skinQuery);
        List<NftMarketPlaceSkinResp> records = skinPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NftMarketPlaceSkinResp resp = new NftMarketPlaceSkinResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber("#"+ p.getTokenId());
            if (p.getAuctionId() == 0){
                resp.setState(NftStateEnum.ON_SHELF.getCode());
            }else {
                resp.setState(NftStateEnum.ON_AUCTION.getCode());
            }

            // 查询NFT
            NftEquipment nftEquipment = nftEquipmentMapper.getById(p.getNftId());
            // 获取当前nftId 下的mintAddress
            String mintAddress = nftEquipment.getMintAddress();
            LambdaQueryWrapper<NftMarketOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(NftMarketOrderEntity::getStatus,2)
                    .eq(NftMarketOrderEntity::getMintAddress,mintAddress)
                    .orderByDesc(NftMarketOrderEntity::getFulfillTime)
                    .last("limit 1");

            NftMarketOrderEntity one = nftMarketOrderService.getOne(queryWrapper);
            if (one != null) {
                resp.setLastSale(one.getPrice());
            }
            UcUserResp ucUserResp = null;
            try {
                GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftEquipment.getOwner());
                ucUserResp = result.getData();
                resp.setIsOwner(UserContext.getCurrentUserId().equals(ucUserResp.getId()) ? 1 : 0);
            } catch (Exception e) {
                log.error("内部请求uc获取用户公共地址失败");
            }
            return resp;
        });

    }

    @Override
    public IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery) {
        log.info("NftMarketPlaceEquipPageReq= {}",equipQuery);
        Page<NftMarketPlaceEqiupmentResp> page = new Page<>();
        page.setCurrent(equipQuery.getCurrent());
        page.setSize(equipQuery.getSize());
        IPage<NftMarketPlaceEqiupmentResp> equipPage = nftMarketOrderMapper.getEquipPage(page,equipQuery);
        List<NftMarketPlaceEqiupmentResp> records = equipPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NftMarketPlaceEqiupmentResp resp = new NftMarketPlaceEqiupmentResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber("#"+ p.getTokenId());
            if (p.getAuctionId() == 0){
                resp.setState(NftStateEnum.ON_SHELF.getCode());
            }else {
                resp.setState(NftStateEnum.ON_AUCTION.getCode());
            }

            // 查询NFT
            NftEquipment nftEquipment = nftEquipmentMapper.getById(p.getNftId());
            // 获取当前nftId 下的mintAddress
            String mintAddress = nftEquipment.getMintAddress();
            LambdaQueryWrapper<NftMarketOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(NftMarketOrderEntity::getStatus,2)
                    .eq(NftMarketOrderEntity::getMintAddress,mintAddress)
                    .orderByDesc(NftMarketOrderEntity::getFulfillTime)
                    .last("limit 1");

            NftMarketOrderEntity one = nftMarketOrderService.getOne(queryWrapper);
            if (one != null) {
                resp.setLastSale(one.getPrice());
            }
            UcUserResp ucUserResp = null;
            try {
                GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftEquipment.getOwner());
                ucUserResp = result.getData();
                resp.setIsOwner(UserContext.getCurrentUserId().equals(ucUserResp.getId()) ? 1 : 0);
            } catch (Exception e) {
                log.error("内部请求uc获取用户公共地址失败");
            }
            resp.setMaxDurability(maxDurability);
            return resp;
        });

    }

    @Override
    public void view(NftMarketPlaceDetailViewReq req, HttpServletRequest request) {
        NftPublicBackpackEntity publicBackpack = nftPublicBackpackService.queryByEqNftId(req.getNftId());
        try {
            GenericDto<UserInfoResp> respGenericDto = userCenterFeignClient.getInfo(request);
            Long userId = respGenericDto.getData().getId();
            if (userId == null || !userId.equals(publicBackpack.getUserId())){
                // 属性表更新NFT浏览量
                LambdaUpdateWrapper<NftPublicBackpackEntity> updateWrap = new UpdateWrapper<NftPublicBackpackEntity>().lambda()
                        .setSql("`views`=`views`+1")
                        .eq(NftPublicBackpackEntity::getEqNftId,req.getNftId());
                nftPublicBackpackService.update(updateWrap);
            }
        } catch (Exception e) {
            log.error("内部请求uc获取用户信息失败");
        }
    }

    @Override
    public IPage<NftMarketPlacePropsResp> propsQueryPage(NftMarketPlacePropsPageReq propsQuery) {
        log.info("NftMarketPlacePropsPageReq---> {}",propsQuery);
        Page<NftMarketPlacePropsResp> page = new Page<>();
        page.setCurrent(propsQuery.getCurrent());
        page.setSize(propsQuery.getSize());
        IPage<NftMarketPlacePropsResp> propPage= nftMarketOrderMapper.getPropsPage(page,propsQuery);
        List<NftMarketPlacePropsResp> records = propPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NftMarketPlacePropsResp resp = new NftMarketPlacePropsResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber("#"+ p.getTokenId());
            if (p.getAuctionId() == 0){
                resp.setState(NftStateEnum.ON_SHELF.getCode());
            }else {
                resp.setState(NftStateEnum.ON_AUCTION.getCode());
            }

            // 查询NFT
            NftEquipment nftEquipment = nftEquipmentMapper.getById(p.getNftId());
            // 获取当前nftId 下的mintAddress
            String mintAddress = nftEquipment.getMintAddress();
            LambdaQueryWrapper<NftMarketOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(NftMarketOrderEntity::getStatus,2)
                    .eq(NftMarketOrderEntity::getMintAddress,mintAddress)
                    .orderByDesc(NftMarketOrderEntity::getFulfillTime)
                    .last("limit 1");

            NftMarketOrderEntity one = nftMarketOrderService.getOne(queryWrapper);
            if (one != null) {
                resp.setLastSale(one.getPrice());
            }
            UcUserResp ucUserResp = null;
            try {
                GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftEquipment.getOwner());
                ucUserResp = result.getData();
                resp.setIsOwner(UserContext.getCurrentUserId().equals(ucUserResp.getId()) ? 1 : 0);
            } catch (Exception e) {
                log.error("内部请求uc获取用户公共地址失败");
            }
            resp.setMaxDurability(maxDurability);
            return resp;
        });
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
                            .timeout(8 * 1000)
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
    public String chainNonce() {
        Long currentUserId = UserContext.getCurrentUserId();
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(currentUserId);
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (publicAddress == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_40001_FAILED_TO_GET_USER_INFORMATION);
        }
        // 调用/api/chainOp/nonce获取随机码
        String params = String.format("address=%s", publicAddress);
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getNonce() + "?" + params;
        log.info("NFT获取交易顺序， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException("Failed to get nonce, code:" + code);
            }
            return jsonObject.getString("data");
        } catch (Exception e) {
            log.error("NFT获取交易顺序失败，message：{}", e.getMessage());
            throw new GenericException(e.getMessage());
        }
    }

    @Override
    public void refundFee(NftRefundFeeReq req) {
        BigDecimal price;
        long duration;
        long placeTime;
        String sellerAddress;
        NftFeeRecordEntity nftFeeRecord;
        if (req.getAuctionId() != null) {
            nftFeeRecord = nftFeeRecordService.queryByAuctionId(req.getAuctionId());
            if (nftFeeRecord != null && nftFeeRecord.getRefundFee() !=null && WhetherEnum.YES.value() == nftFeeRecord.getStatus()) {
                log.info("托管费已退还， auctionId:{}", req.getAuctionId());
                return;
            }
            NftAuctionHouseSetting auctionSetting = nftAuctionHouseSettingService.getById(req.getAuctionId());
            if (auctionSetting == null) {
                throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
            }
            price = auctionSetting.getStartPrice();
            duration = auctionSetting.getDuration();
            placeTime = auctionSetting.getStartTime();
            NftAuctionHouseListing auctionListing = nftAuctionHouseListingService.getById(auctionSetting.getListingId());
            if (auctionListing == null) {
                throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
            }
            sellerAddress = auctionListing.getSeller();
        } else if (req.getOrderId() != null) {
            nftFeeRecord = nftFeeRecordService.queryByOrderId(req.getOrderId());
            if (nftFeeRecord != null && nftFeeRecord.getRefundFee() !=null && WhetherEnum.YES.value() == nftFeeRecord.getStatus()) {
                log.info("托管费已退还， orderId:{}", req.getOrderId());
                return;
            }
            NftMarketOrderEntity order = nftMarketOrderService.getById(req.getOrderId());
            if (order == null) {
                throw new GenericException(GameErrorCodeEnum.ERR_10018_NFT_ITEM_ORDER_NOT_EXIST);
            }
            price = order.getPrice();
            duration = 72L;
            placeTime = order.getPlaceTime();
            sellerAddress = order.getSellerAddress();
        } else {
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
        BigDecimal baseFee = price.multiply(new BigDecimal("0.005"));
        BigDecimal receivableFee = new BigDecimal(duration / 12L).multiply(baseFee);
        long consumedHours = RelativeDateFormat.toHours(System.currentTimeMillis() - placeTime);
        long base = consumedHours % 12L > 0 ? consumedHours / 12L + 1 : consumedHours / 12L;
        BigDecimal refundFee = receivableFee.subtract(new BigDecimal(base).multiply(baseFee));
        // 调用/api/admin/refundFee，请求NFT托管费退还
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getRefundFee();
        TransferSolMessageDto dto = new TransferSolMessageDto();
        dto.setAmount(refundFee);
        dto.setToAddress(sellerAddress);
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT托管费退还开始请求， url:{}， params:{}", url, param);
        if (nftFeeRecord == null) {
            nftFeeRecord = new NftFeeRecordEntity();
        }
        BeanUtils.copyProperties(req, nftFeeRecord);
        nftFeeRecord.setReceivableFee(receivableFee);
        nftFeeRecord.setRefundFee(refundFee);
        nftFeeRecord.setRefundTime(System.currentTimeMillis());
        nftFeeRecord.setCurrency(CurrencyEnum.SOL.getCode());
        nftFeeRecord.setToAddress(sellerAddress);
        nftFeeRecord.setStatus(WhetherEnum.YES.value());
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            String body = response.body();
            log.info("请求NFT托管费退还接口返回，  result:{}", body);
            JSONObject jsonObject = JSONObject.parseObject(body);
            Integer code = jsonObject.getInteger("code");
            if (code == null || code != 200) {
                throw new GenericException("Failed to refund Fee, message:" + jsonObject.getString("message"));
            }
            nftFeeRecordService.save(nftFeeRecord);
        } catch (Exception e) {
            log.error("请求NFT托管费退还接口失败，message：{}", e.getMessage());
            nftFeeRecord.setStatus(WhetherEnum.NO.value());
            nftFeeRecordService.saveOrUpdate(nftFeeRecord);
        }
    }

    @Override
    public NftOfferResp offerPage(NftOfferPageReq req) {
        NftOfferResp resp = new NftOfferResp();
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        if (nftEquipment == null) {
            return resp;
        }
        NftAuctionHouseSetting auctionSetting = nftAuctionHouseSettingService.getById(nftEquipment.getAuctionId());
        if (auctionSetting == null) {
            return resp;
        }
        if (auctionSetting.getIsFinished() != null && WhetherEnum.YES.value() == auctionSetting.getIsFinished()) {
            return resp;
        }
        req.setAuctionId(auctionSetting.getId());
        req.setPrice(auctionSetting.getStartPrice());
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
            for (NftOfferResp.NftOffer nftOffer : records) {
                if (nftOffer.getCancelTime() == null) {
                    resp.setHighestOffer(nftOffer.getPrice());
                    break;
                }
            }
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
    public NftOfferDetailResp acceptOffer(NftAcceptOfferReq req) {
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
        //更新背包状态 undeposited  userId,owner
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        try {
            GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(auctionBiding.getBuyer());
            backpackEntity.setUserId(result.getData().getId());
            backpackEntity.setOwner(auctionBiding.getBuyer());
        } catch (Exception e) {
            log.error("内部请求uc获取用户信息失败");
        }
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/auction/endAuction通知，接受报价成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getEndAuction();
        EndAuctionMessageDto dto = new EndAuctionMessageDto();
        dto.setNftId(nftEquipment.getId());
        dto.setToAddress(nftEquipment.getOwner());
        BeanUtils.copyProperties(req, dto);
        dto.setAuctionId(auctionBiding.getAuctionId());
        dto.setMintAddress(auctionBiding.getMintAddress());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT接受报价成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            String body = response.body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            String data = jsonObject.getString("data");
            log.info("请求NFT接受报价成功接口返回，  result:{}", body);
            return JSONUtil.toBean(data, NftOfferDetailResp.class);
        } catch (Exception e) {
            log.error("NFT接受报价成功通知失败，message：{}", e.getMessage());
            throw new GenericException("Accept offer failed");
        }
    }

    @Override
    public void auctionSuccess(NftSaleSuccessReq req) {
        NftAuctionHouseBiding auctionBiding = nftAuctionHouseBidingService.getById(req.getBidingId());
        if (auctionBiding == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
        }
        // 调用/api/auction/saleSuccess通知，拍卖达成交易成功
        String params = String.format("auctionId=%s&bidingId=%s&receipt=%s&sig=%s", auctionBiding.getAuctionId(), req.getBidingId(), req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getSaleSuccess() + "?" + params;
        log.info("NFT拍卖达成交易成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT拍卖达成交易成功通知返回，result:{}", response.body());
        } catch (Exception e) {
            log.error("NFT拍卖达成交易成功通知失败，message：{}", e.getMessage());
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setAuctionId(auctionBiding.getAuctionId());
        refundFee(feeReq);
    }

    @Override
    public void cancelOffer(NftCancelOfferReq req) {
        NftAuctionHouseBiding auctionBiding = nftAuctionHouseBidingService.getById(req.getBidingId());
        if (auctionBiding == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);}
        // offer归属人验证
        ucUserService.ownerValidation(auctionBiding.getBuyer());
        // 调用/api/auction/cancelBid通知，取消出价成功
        String params = String.format("bidingId=%s&receipt=%s&signature=%s", req.getBidingId(), auctionBiding.getReceipt(), req.getSignature());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionCancelBid() + "?" + params;
        log.info("NFT取消出价成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(8 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT取消出价成功通知返回，result:{}", response.body());
        } catch (Exception e) {
            log.error("NFT取消出价成功通知失败，message：{}", e.getMessage());
        }
    }

    private void shelfValidation(NftEquipment nftEquipment) {
        if (nftEquipment == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 归属人才能上/下架
        ucUserService.ownerValidation(nftEquipment.getOwner());
        // NFT装备还未生成
        if (WhetherEnum.NO.value() == nftEquipment.getNftGenerated()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10009_NFT_ITEM_HAS_NOT_BEEN_GENERATED);
        }
        // 已托管不能上/下架
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

    private Integer convertOrderState(NftEquipment nftEquipment, NftAuctionHouseSetting auction) {
        int state;
        if (WhetherEnum.YES.value() == nftEquipment.getIsDelete()) {
            return NftStateEnum.BURNED.getCode();
        }
        if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            state = NftStateEnum.DEPOSITED.getCode();
        } else {
            if (NftOrderTypeEnum.ON_AUCTION.getCode() == nftEquipment.getOnSale()) {
                // 拍卖时间到期为结算中
                if (System.currentTimeMillis() > auction.getStartTime() + auction.getDuration() * 60 * 60 * 1000) {
                    state = NftStateEnum.IN_SETTLEMENT.getCode();
                } else {
                    // 拍卖中
                    state = NftStateEnum.ON_AUCTION.getCode();
                }
            } else if (NftOrderTypeEnum.BUY_NOW.getCode() == nftEquipment.getOnSale()) {
                // 固定上架
                state = NftStateEnum.ON_SHELF.getCode();
            } else {
                // 未上架
                state = NftStateEnum.UNDEPOSITED.getCode();
            }
        }
        return state;
    }

}
