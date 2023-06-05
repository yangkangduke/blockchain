package com.seeds.game.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.admin.entity.SysFileEntity;
import com.seeds.admin.entity.SysNftPicEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.game.dto.request.internal.SkinNftFirstBuySuccessDto;
import com.seeds.game.dto.request.internal.SkinNftWithdrawDto;
import com.seeds.game.enums.NftAuctionStatusEnum;
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
import com.seeds.game.mq.producer.KafkaProducer;
import com.seeds.game.service.*;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class NftMarketPlaceServiceImpl implements NftMarketPlaceService {

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
    private INftFeeOpLogService nftFeeOpLogService;

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
    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public NftMarketPlaceDetailResp detail(Long nftId) {
        NftMarketPlaceDetailResp resp = new NftMarketPlaceDetailResp();
        // 查询NFT
        NftEquipment nftEquipment = nftEquipmentMapper.getById(nftId);
        if (nftEquipment == null) {
            return resp;
        }
        NftPublicBackpackEntity publicBackpack = nftPublicBackpackService.queryByEqNftId(nftId);
        Boolean isLock = Boolean.FALSE;
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
                SysNftPicEntity heroAndSkin = nftPublicBackpackService.getHeroAndSkin(publicBackpack.getNftPicId());
                if (null != heroAndSkin) {
                    resp.setHeroName(heroAndSkin.getHero());
                    resp.setSkinName(heroAndSkin.getSkin());
                }
            } else {
                resp.setContractAddress(solanaConfig.getEquipmentContractAddress());
            }
            isLock = publicBackpack.getState() == NFTEnumConstant.NFTStateEnum.LOCK.getCode() ? Boolean.TRUE : Boolean.FALSE;
        }
        NftAttributeEntity nftAttribute = nftAttributeService.queryByNftId(nftId);
        if (nftAttribute != null) {
            BeanUtils.copyProperties(nftAttribute, resp);
            resp.setDurability(nftAttribute.getDurability());
            resp.setMaxDurability(nftAttribute.getDurabilityConfig());
            if (NftTypeEnum.hero.getCode() == resp.getType()) {
                HashMap<String, Integer> skinAttr = handleSkinAttr(nftAttribute);
                resp.setAttributes(JSONUtil.parseObj(JSONUtil.toJsonStr(skinAttr)));
                resp.setRarity(nftAttribute.getRarity());
            }
        }
        UcUserResp ucUserResp = null;
        try {
            GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(nftEquipment.getOwner());
            ucUserResp = result.getData();
            log.info("getUserInfoByPublicAddress,param:{},result:{}", nftEquipment.getOwner(), result);
            if (null == ucUserResp) {
                resp.setIsOwner(0);
                resp.setOwnerName(NFTEnumConstant.SEEDS);
            } else {
                resp.setIsOwner(ucUserResp.getId().equals(UserContext.getCurrentUserIdNoThrow()) ? 1 : 0);
                resp.setOwnerId(ucUserResp.getId());
                resp.setOwnerName(ucUserResp.getNickname());
            }
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        NftMarketOrderEntity marketOrder = null;
        // 拍卖中
        if (NftOrderTypeEnum.ON_AUCTION.getCode() == nftEquipment.getOnSale()) {
            NftAuctionHouseSetting auctionSetting = nftAuctionHouseSettingService.getById(nftEquipment.getAuctionId());
            if (auctionSetting != null) {
                BeanUtils.copyProperties(auctionSetting, resp);
                long time = (auctionSetting.getStartTime() + auctionSetting.getDuration() * 60 * 60 * 1000) - System.currentTimeMillis();
                resp.setTimeLeft(RelativeDateFormat.formatTime(time));
                NftAuctionHouseListing auctionListing = nftAuctionHouseListingService.getById(auctionSetting.getListingId());
                if (auctionListing != null) {
                    resp.setListReceipt(auctionListing.getReceipt());
                }
                BigDecimal startPrice = auctionSetting.getStartPrice();
                resp.setCurrentPrice(startPrice);

                BigDecimal currentPrice = nftAuctionHouseBidingService.queryAuctionCurrentPrice(nftEquipment.getAuctionId());
                if (currentPrice != null) {
                    resp.setCurrentPrice(currentPrice);
                    resp.setHighPrice(currentPrice);
                }

                if (startPrice.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal difference = resp.getCurrentPrice().subtract(startPrice)
                            .divide(startPrice, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100))
                            .setScale(0, RoundingMode.HALF_UP);
                    if (resp.getCurrentPrice().compareTo(startPrice) > 0) {
                        resp.setPriceDifference(difference + "% above");
                    } else {
                        resp.setPriceDifference(difference.abs() + "% below");
                    }
                }

                marketOrder = nftMarketOrderService.queryByAuctionId(nftEquipment.getAuctionId());
                if (marketOrder != null) {
                    resp.setOrderId(marketOrder.getId());
                }
            }
        } else if (NftOrderTypeEnum.BUY_NOW.getCode() == nftEquipment.getOnSale()) {
            // 固定上架
            marketOrder = nftMarketOrderService.getById(nftEquipment.getOrderId());
            if (marketOrder != null) {
                resp.setOrderId(marketOrder.getId());
                resp.setCurrentPrice(marketOrder.getPrice());
                resp.setListReceipt(marketOrder.getListReceipt());
            }
        }
        BeanUtils.copyProperties(nftEquipment, resp);
        resp.setOwnerAddress(nftEquipment.getOwner());
        resp.setNftId(nftEquipment.getId());
        resp.setNumber("#" + nftEquipment.getTokenId());
        resp.setState(convertOrderState(nftEquipment, marketOrder, isLock));
        Long lastUpdated = nftActivityService.queryLastUpdateTime(nftEquipment.getMintAddress());
        if (lastUpdated != null) {
            resp.setLastUpdated(RelativeDateFormat.convert(new Date(lastUpdated), new Date()));
        } else {
            resp.setLastUpdated(RelativeDateFormat.convert(new Date(nftEquipment.getUpdateTime()), new Date()));
        }
        BeanUtils.copyProperties(solanaConfig, resp);
        return resp;
    }

    private HashMap<String, Integer> handleSkinAttr(NftAttributeEntity nftAttribute) {
        HashMap<String, Integer> attrMap = new HashMap<>();
        attrMap.put("victory", nftAttribute.getVictory());
        attrMap.put("lose", nftAttribute.getLose());
        attrMap.put("maxStreak", nftAttribute.getMaxStreak());
        attrMap.put("maxLose", nftAttribute.getMaxLose());
        attrMap.put("capture", nftAttribute.getCapture());
        attrMap.put("killingSpree", nftAttribute.getKillingSpree());
        attrMap.put("goblinKill", nftAttribute.getGoblinKill());
        attrMap.put("slaying", nftAttribute.getSlaying());
        attrMap.put("goblin", nftAttribute.getGoblin());
        return attrMap;
    }

    @Override
    public void fixedPriceShelf(NftFixedPriceShelfReq req) {
        // 上架前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        //shelfValidation(nftEquipment);
        // 不能重复上架
        if (nftEquipment == null
                || NftOrderTypeEnum.BUY_NOW.getCode() == nftEquipment.getOnSale()
                || NftOrderTypeEnum.ON_AUCTION.getCode() == nftEquipment.getOnSale()
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
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
                    .timeout(30 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT一口价上架成功通知返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("一口价上架成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT listing failure!");
        }
    }

    @Override
    public void britishAuctionShelf(NftBritishAuctionShelfReq req) {
        // 上架前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        //shelfValidation(nftEquipment);
        // 不能重复上架
        if (nftEquipment == null
                || NftOrderTypeEnum.BUY_NOW.getCode() == nftEquipment.getOnSale()
                || NftOrderTypeEnum.ON_AUCTION.getCode() == nftEquipment.getOnSale()
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
        }
        //更新背包状态(undeposited => on auction)
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.ON_ACTION.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        // 调用/api/auction/english通知，链上英式拍卖上架成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getEnglishOrderApi();
        EnglishAuctionReqDto dto = new EnglishAuctionReqDto();
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
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("英式拍卖上架成功通知失败，message：{}", e.getMessage());
            throw new GenericException("British auction shelf failure!");
        }
    }

    @Override
    public void shelved(NftShelvedReq req) {
        // 下架前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        //shelfValidation(nftEquipment);
        // 不能重复下架
        if (nftEquipment == null
                || WhetherEnum.NO.value() == nftEquipment.getOnSale()
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
        }
        //更新背包状态 undeposited
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));

        Long orderId = Long.valueOf(nftEquipment.getOrderId());
        // 调用/api/chainOp/cancelOrder通知，下架成功
        String params = String.format("receipt=%s&sig=%s", req.getReceipt(), req.getSig());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getCancelOrderApi() + "?" + params;
        log.info("NFT下架成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(30 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT下架成功通知返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT下架成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT downgrade failed!");
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setOrderId(orderId);
        refundFee(feeReq);
    }

    @Override
    public void cancelAuction(NftCancelAuctionReq req) {
        // 取消拍卖前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        // 没有进行中的拍卖
        if (nftEquipment == null
                || nftEquipment.getAuctionId() <= 0
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
        }
        //shelfValidation(nftEquipment);
        Long auctionId = nftEquipment.getAuctionId();
        NftAuctionHouseSetting auction = nftAuctionHouseSettingService.getById(nftEquipment.getAuctionId());
        if (auction == null) {
            return;
        }
        if (auction.getCancelTime() != null && auction.getCancelTime() != 0) {
            return;
        }
        // 更新背包状态 undeposited
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        nftPublicBackpackService.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nftEquipment.getId()));
        // 调用/api/chainOp/cancelOrder通知，取消拍卖成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionCancel();
        EndAuctionMessageDto dto = new EndAuctionMessageDto();
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
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT取消拍卖成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT cancel auction failed!");
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setAuctionId(auctionId);
        refundFee(feeReq);
    }

    @Override
    public void makeOffer(NftMakeOfferReq req) {
        // 出价成功前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        //buyValidation(nftEquipment);
        // 拍卖结束不能出价
        //NftAuctionHouseSetting auction = nftAuctionHouseSettingService.getById(req.getAuctionId());
        //if (auction == null) {
        //    throw new GenericException(GameErrorCodeEnum.ERR_10011_NFT_ITEM_AUCTION_NOT_EXIST);
        //}
        // 拍卖到期不能出价
        //if (System.currentTimeMillis() > auction.getStartTime() + auction.getDuration() * 60 * 60 * 1000) {
        //    throw new GenericException(GameErrorCodeEnum.ERR_10017_NFT_ITEM_IN_SETTLEMENT);
        //}
        //if (auction.getIsFinished() != null && WhetherEnum.NO.value() != auction.getIsFinished()) {
        //    throw new GenericException(GameErrorCodeEnum.ERR_10012_NFT_ITEM_AUCTION_HAS_ENDED);
        //}
        if (nftEquipment == null
                || NftOrderTypeEnum.ON_AUCTION.getCode() != nftEquipment.getOnSale()
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
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
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT拍卖出价成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT make offer failed!");
        }
    }

    @Override
    public boolean makeOfferValidate(String mintAddress) {
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(UserContext.getCurrentUserId());
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (StringUtils.isEmpty(publicAddress)) {
            return false;
        }
        List<NftAuctionHouseBiding> bids = nftAuctionHouseBidingService.queryByAddressAndMintAddress(publicAddress, mintAddress);
        if (CollectionUtils.isEmpty(bids)) {
            return true;
        }
        Set<Long> bidingIds = bids.stream().map(NftAuctionHouseBiding::getId).collect(Collectors.toSet());
        Set<Long> auctionIds = bids.stream().map(NftAuctionHouseBiding::getAuctionId).collect(Collectors.toSet());
        List<NftAuctionHouseSetting> list = nftAuctionHouseSettingService.listByIds(auctionIds);
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        list = list.stream().filter(p -> !bidingIds.contains(p.getBidingId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        return false;
    }

    @Override
    public IPage<NftMyOfferResp> myOfferPage(NftMyOfferPageReq req) {
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(UserContext.getCurrentUserId());
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        if (StringUtils.isEmpty(publicAddress)) {
            return new Page<>();
        }
        req.setPublicAddress(publicAddress);
        IPage<NftAuctionHouseBiding> page = nftAuctionHouseBidingService.queryMyPage(req);
        List<NftAuctionHouseBiding> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> auctionIds = records.stream().map(NftAuctionHouseBiding::getAuctionId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, NftAuctionHouseSetting> auctionMap = nftAuctionHouseSettingService.queryMapByIds(auctionIds);
        Set<String> mintAddresses = records.stream().map(NftAuctionHouseBiding::getMintAddress).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        Map<String, NftPublicBackpackEntity> backpackMap = nftPublicBackpackService.queryMapByMintAddress(mintAddresses);
        Map<Long, NftMarketOrderEntity> orderMap = nftMarketOrderService.queryMapByAuctionIds(auctionIds);
        Set<Long> nftIds = backpackMap.values().stream().map(NftPublicBackpackEntity::getEqNftId).collect(Collectors.toSet());
        Map<Long, NftAttributeEntity> attributeMap = nftAttributeService.queryMapByNftIds(nftIds);
        return page.convert(p -> {
            NftMyOfferResp resp = new NftMyOfferResp();
            if (p.getCancelTime() != null && p.getCancelTime() > 0) {
                resp.setCancelFlag(WhetherEnum.NO.value());
            }
            resp.setReceipt(p.getReceipt());
            resp.setBidingId(p.getId());
            resp.setOfferTime(p.getCreateTime());
            resp.setPrice(p.getPrice());
            NftAuctionHouseSetting auction = auctionMap.get(p.getAuctionId());
            if (auction != null) {
                BigDecimal difference = p.getPrice().subtract(auction.getStartPrice())
                        .divide(auction.getStartPrice(), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(0, RoundingMode.HALF_UP);
                if (p.getPrice().compareTo(auction.getStartPrice()) > 0) {
                    resp.setDifference(difference + "% above");
                } else {
                    resp.setDifference(difference.abs() + "% below");
                }
                resp.setStatus(NftAuctionStatusEnum.from(auction.getIsFinished()).getCode());
                if (NftAuctionStatusEnum.FINISHED.getCode() == auction.getIsFinished()) {
                    NftMarketOrderEntity order = orderMap.get(auction.getId());
                    if (order != null && NftStateEnum.IN_SETTLEMENT.getCode() == order.getStatus()) {
                        resp.setStatus(NftAuctionStatusEnum.SETTLEMENT.getCode());
                        resp.setCancelFlag(WhetherEnum.NO.value());
                    }
                }
                if (p.getId().equals(auction.getBidingId())) {
                    resp.setCancelFlag(WhetherEnum.NO.value());
                }
            }
            NftPublicBackpackEntity backpack = backpackMap.get(p.getMintAddress());
            if (backpack != null) {
                resp.setNftId(backpack.getEqNftId());
                resp.setNftImage(backpack.getImage());
                resp.setNftNo("#" + backpack.getTokenId());
                NftAttributeEntity nftAttribute = attributeMap.get(backpack.getEqNftId());
                if (nftAttribute != null) {
                    resp.setGrade(nftAttribute.getGrade());
                    resp.setRarityAttrValue(nftAttribute.getRarityAttrValue());
                }
            }
            return resp;
        });
    }

    @Override
    public void buySuccess(NftBuySuccessReq req) {
        // 购买成功前的校验
        NftEquipment nftEquipment = nftEquipmentService.getById(req.getNftId());
        //buyValidation(nftEquipment);
        if (nftEquipment == null
                || NftOrderTypeEnum.BUY_NOW.getCode() != nftEquipment.getOnSale()
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
        }
        Long orderId = Long.valueOf(nftEquipment.getOrderId());

        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.queryByEqNftId(nftEquipment.getId());
        if (backpackEntity.getType().equals(NFTEnumConstant.NftTypeEnum.HERO.getCode())
                && backpackEntity.getIsTraded().equals(WhetherEnum.NO.value())) {
            // 发送皮肤nft 首次购买 成功消息
            SkinNftFirstBuySuccessDto dto = new SkinNftFirstBuySuccessDto();
            dto.setNftPicId(backpackEntity.getNftPicId());
            kafkaProducer.sendAsync(KafkaTopic.SKIN_NFT_BUY_SUCCESS_FIRST, JSONUtil.toJsonStr(dto));
        }

        //更新背包状态 undeposited  userId,owner
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
        backpackEntity.setIsTraded(WhetherEnum.YES.value());
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
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT购买成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT purchase failure!");
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setOrderId(orderId);
        refundFee(feeReq);
    }

    @Override
    public IPage<NftMarketPlaceSkinResp> skinQueryPage(NftMarketPlaceSkinPageReq skinQuery) {
        log.info("NftMarketPlaceSkinPageReq= {}", skinQuery);
        Page<NftMarketPlaceSkinResp> page = new Page<>();
        page.setCurrent(skinQuery.getCurrent());
        page.setSize(skinQuery.getSize());
        if (Objects.nonNull(skinQuery.getSortType())) {
            skinQuery.setSortTypeStr(NftMarketPlaceSkinPageReq.convert(skinQuery.getSortType()));
        }
        IPage<NftMarketPlaceSkinResp> skinPage = nftMarketOrderMapper.getSkinPage(page, skinQuery);
        List<NftMarketPlaceSkinResp> records = skinPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        String publicAddress = getUserById(UserContext.getCurrentUserIdNoThrow());
        return page.convert(p -> {
            NftMarketPlaceSkinResp resp = new NftMarketPlaceSkinResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber("#" + p.getTokenId());
            if (p.getAuctionId() == 0) {
                resp.setState(NftStateEnum.ON_SHELF.getCode());
            } else {
                resp.setState(NftStateEnum.ON_AUCTION.getCode());
                if (NftStateEnum.IN_SETTLEMENT.getCode() == p.getState()) {
                    resp.setState(NftStateEnum.IN_SETTLEMENT.getCode());
                }
            }
            if (p.getIsBlindBox().equals(WhetherEnum.YES.value()) && p.getIsTread().equals(WhetherEnum.NO.value())) {
                p.setHeroName("");
                p.setImage("");
                p.setSkinName("");
                p.setRarity(0);
            }
            resp.setIsOwner(p.getOwner().equals(publicAddress) ? 1 : 0);
            return resp;
        });

    }

    @Override
    public IPage<NftMarketPlaceEqiupmentResp> equipQueryPage(NftMarketPlaceEquipPageReq equipQuery) {
        log.info("NftMarketPlaceEquipPageReq= {}", equipQuery);
        Page<NftMarketPlaceEqiupmentResp> page = new Page<>();
        page.setCurrent(equipQuery.getCurrent());
        page.setSize(equipQuery.getSize());
        if (Objects.nonNull(equipQuery.getSortType())) {
            equipQuery.setSortTypeStr(NftMarketPlaceEquipPageReq.convert(equipQuery.getSortType()));
        }
        IPage<NftMarketPlaceEqiupmentResp> equipPage = nftMarketOrderMapper.getEquipPage(page, equipQuery);
        List<NftMarketPlaceEqiupmentResp> records = equipPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        String publicAddress = getUserById(UserContext.getCurrentUserIdNoThrow());
        return page.convert(p -> {
            NftMarketPlaceEqiupmentResp resp = new NftMarketPlaceEqiupmentResp();
            BeanUtils.copyProperties(p, resp);
            resp.setIsOwner(p.getOwner().equals(publicAddress) ? 1 : 0);
            resp.setNumber("#" + p.getTokenId());
            if (p.getAuctionId() == 0) {
                resp.setState(NftStateEnum.ON_SHELF.getCode());
            } else {
                resp.setState(NftStateEnum.ON_AUCTION.getCode());
                if (NftStateEnum.IN_SETTLEMENT.getCode() == p.getState()) {
                    resp.setState(NftStateEnum.IN_SETTLEMENT.getCode());
                }
            }
            return resp;
        });

    }

    @Override
    public void view(NftMarketPlaceDetailViewReq req) {
        log.info("NftMarketPlaceDetailViewReq--->{}", req);
        NftPublicBackpackEntity publicBackpack = nftPublicBackpackService.queryByEqNftId(req.getNftId());
        Long userId = null;
        try {
            userId = UserContext.getCurrentUserId();
        } catch (Exception ex) {
            log.error("当前用户未登录");
        }
        if (userId == null || !userId.equals(publicBackpack.getUserId())) {
            // 属性表更新NFT浏览量
            LambdaUpdateWrapper<NftPublicBackpackEntity> updateWrap = new UpdateWrapper<NftPublicBackpackEntity>().lambda()
                    .setSql("`views`=`views`+1")
                    .eq(NftPublicBackpackEntity::getEqNftId, req.getNftId());
            nftPublicBackpackService.update(updateWrap);
        }
    }

    @Override
    public IPage<NftMarketPlacePropsResp> propsQueryPage(NftMarketPlacePropsPageReq propsQuery) {
        log.info("NftMarketPlacePropsPageReq---> {}", propsQuery);
        Page<NftMarketPlacePropsResp> page = new Page<>();
        page.setCurrent(propsQuery.getCurrent());
        page.setSize(propsQuery.getSize());
        if (Objects.nonNull(propsQuery.getSortType())) {
            propsQuery.setSortTypeStr(NftMarketPlacePropsPageReq.convert(propsQuery.getSortType()));
        }
        IPage<NftMarketPlacePropsResp> propPage = nftMarketOrderMapper.getPropsPage(page, propsQuery);
        List<NftMarketPlacePropsResp> records = propPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        String publicAddress = getUserById(UserContext.getCurrentUserIdNoThrow());
        return page.convert(p -> {
            NftMarketPlacePropsResp resp = new NftMarketPlacePropsResp();
            BeanUtils.copyProperties(p, resp);
            resp.setNumber("#" + p.getTokenId());
            resp.setIsOwner(p.getOwner().equals(publicAddress) ? 1 : 0);
            if (p.getAuctionId() == 0) {
                resp.setState(NftStateEnum.ON_SHELF.getCode());
            } else {
                resp.setState(NftStateEnum.ON_AUCTION.getCode());
                if (NftStateEnum.IN_SETTLEMENT.getCode() == p.getState()) {
                    resp.setState(NftStateEnum.IN_SETTLEMENT.getCode());
                }
            }

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
                String url = seedsApiConfig.getSolToUsdApi()
                        + "?inputMint=" + seedsApiConfig.getSolTokenAddress()
                        + "&outputMint=" + seedsApiConfig.getSolToken()
                        + "&amount=" + 1000000000;
                log.info("获取美元汇率， url:{}， params:{}", url, currency);
                try {
                    HttpResponse response = HttpRequest.get(url)
                            .timeout(8 * 1000)
                            .header("Content-Type", "application/json")
                            .header("token", seedsApiConfig.getSolToken())
                            .execute();
                    String body = response.body();
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    String data = jsonObject.getString("outAmount");
                    if (StringUtils.isNotBlank(data)) {
                        rate = new BigDecimal(data).divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP);
                        gameCacheService.putUsdRate(currency, rate.toString());
                    }
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
        long endTime;
        String sellerAddress;
        NftFeeRecordEntity nftFeeRecord;
        if (req.getAuctionId() != null) {
            nftFeeRecord = nftFeeRecordService.queryByAuctionId(req.getAuctionId());
            if (nftFeeRecord != null && nftFeeRecord.getRefundFee() != null && WhetherEnum.YES.value() == nftFeeRecord.getStatus()) {
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
            NftMarketOrderEntity marketOrder = nftMarketOrderService.queryByAuctionId(req.getAuctionId());
            if (marketOrder == null) {
                throw new GenericException(GameErrorCodeEnum.ERR_10018_NFT_ITEM_ORDER_NOT_EXIST);
            }
            if (seedsApiConfig.getAdminSkinAddress().equals(marketOrder.getSellerAddress())) {
                log.info("管理员账户不用退还， auctionId:{}", req.getAuctionId());
                return;
            }
            sellerAddress = marketOrder.getSellerAddress();
            if (marketOrder.getCancelTime() != null && marketOrder.getCancelTime() != 0) {
                endTime = marketOrder.getCancelTime();
            } else if (!marketOrder.getFulfillTime().equals(marketOrder.getCreateTime())) {
                endTime = marketOrder.getFulfillTime();
            } else {
                endTime = System.currentTimeMillis();
            }
        } else if (req.getOrderId() != null) {
            nftFeeRecord = nftFeeRecordService.queryByOrderId(req.getOrderId());
            if (nftFeeRecord != null && nftFeeRecord.getRefundFee() != null && WhetherEnum.YES.value() == nftFeeRecord.getStatus()) {
                log.info("托管费已退还， orderId:{}", req.getOrderId());
                return;
            }
            NftMarketOrderEntity order = nftMarketOrderService.getById(req.getOrderId());
            if (order == null) {
                throw new GenericException(GameErrorCodeEnum.ERR_10018_NFT_ITEM_ORDER_NOT_EXIST);
            }
            if (seedsApiConfig.getAdminSkinAddress().equals(order.getSellerAddress())) {
                log.info("管理员账户不用退还， orderId:{}", req.getOrderId());
                return;
            }
            if (order.getCancelTime() != null && order.getCancelTime() != 0) {
                endTime = order.getCancelTime();
            } else if (!order.getFulfillTime().equals(order.getCreateTime())) {
                endTime = order.getFulfillTime();
            } else {
                endTime = System.currentTimeMillis();
            }
            price = order.getPrice();
            duration = 72L;
            placeTime = order.getPlaceTime();
            sellerAddress = order.getSellerAddress();
        } else {
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
        // 不足12小时按12小时算
        duration = duration < 12 ? 12L : duration;
        BigDecimal baseFee = price.multiply(new BigDecimal("0.005"));
        BigDecimal receivableFee = new BigDecimal(duration / 12L).multiply(baseFee);
        long consumedHours = Math.max(RelativeDateFormat.toHours(endTime - placeTime), 12L);
        long base = consumedHours % 12L > 0 ? consumedHours / 12L + 1 : consumedHours / 12L;
        BigDecimal refundFee = receivableFee.subtract(new BigDecimal(base).multiply(baseFee));
        if (BigDecimal.ZERO.compareTo(refundFee) == 0) {
            log.info("无需退还托管费， orderId:{}， auctionId:{}", req.getOrderId(), req.getAuctionId());
            return;
        }
        if (nftFeeRecord == null) {
            nftFeeRecord = new NftFeeRecordEntity();
        }
        // 请求NFT托管费退还
        BeanUtils.copyProperties(req, nftFeeRecord);
        nftFeeRecord.setStatus(WhetherEnum.YES.value());
        initRefundFee(refundFee.multiply(new BigDecimal(1000000000L)), sellerAddress, nftFeeRecord);
        nftFeeRecord.setReceivableFee(receivableFee);
        nftFeeRecord.setRefundFee(refundFee);
        nftFeeRecord.setRefundTime(System.currentTimeMillis());
        nftFeeRecord.setCurrency(CurrencyEnum.SOL.getCode());
        nftFeeRecord.setToAddress(sellerAddress);
        nftFeeRecordService.saveOrUpdate(nftFeeRecord);
    }

    @Override
    public BigDecimal custodianFee(BigDecimal price, Long duration) {
        duration = duration == null ? 72L : duration;
        // 不足12小时按12小时算
        duration = duration < 12 ? 12L : duration;
        BigDecimal baseFee = price.multiply(new BigDecimal("0.005"));
        return new BigDecimal(duration / 12L).multiply(baseFee);
    }

    @Override
    public JSONObject listReceipt(Long orderId) {
        // 调用/api/market/getListReceipt获取订单收据
        String params = String.format("orderId=%s", orderId);
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getListReceipt() + "?" + params;
        log.info("NFT获取订单收据， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            log.info("请求NFT获取订单收据接口返回，  result:{}", jsonObject);
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException("Failed to get list receipt, code:" + code);
            }
            return JSONUtil.toBean(jsonObject.getString("data"), JSONObject.class);
        } catch (Exception e) {
            log.error("NFT获取订单收据失败，message：{}", e.getMessage());
            throw new GenericException(e.getMessage());
        }
    }

    @Override
    public void refundAllFee(NftRefundAllFeeReq req) {
        NftFeeOpLog nftFeeOpLog = nftFeeOpLogService.queryByTxHash(req.getFeeHash());
        if (nftFeeOpLog == null) {
            log.info("没有查询到托管费记录，feeHash:{}， mintAddress:{}", req.getFeeHash(), req.getMintAddress());
            return;
        }
        ucUserService.ownerValidation(nftFeeOpLog.getFromAddress());
        NftFeeRecordEntity nftFeeRecord = nftFeeRecordService.queryByMintAddressAndFeeHash(req.getMintAddress(), req.getFeeHash());
        if (nftFeeRecord != null && nftFeeRecord.getRefundFee() != null && WhetherEnum.YES.value() == nftFeeRecord.getStatus()) {
            log.info("托管费已退还，feeHash:{}， mintAddress:{}", req.getFeeHash(), req.getMintAddress());
            return;
        }
        if (nftFeeRecord == null) {
            nftFeeRecord = new NftFeeRecordEntity();
        }
        // 请求NFT托管费退还
        BeanUtils.copyProperties(req, nftFeeRecord);
        nftFeeRecord.setStatus(WhetherEnum.YES.value());
        initRefundFee(nftFeeOpLog.getAmount(), nftFeeOpLog.getFromAddress(), nftFeeRecord);
        nftFeeRecord.setReceivableFee(nftFeeOpLog.getAmount());
        nftFeeRecord.setRefundFee(nftFeeOpLog.getAmount());
        nftFeeRecord.setRefundTime(System.currentTimeMillis());
        nftFeeRecord.setCurrency(CurrencyEnum.SOL.getCode());
        nftFeeRecord.setToAddress(nftFeeOpLog.getFromAddress());
        nftFeeRecordService.saveOrUpdate(nftFeeRecord);
    }

    @Override
    public TransactionMessageRespDto getTransaction(String transaction) {

        TransactionMessageRespDto dto = new TransactionMessageRespDto();

        String params = String.format("transaction=%s", transaction);
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getGetTransaction() + "?" + params;
        log.info("transaction， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("获取transaction返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if ("200".equalsIgnoreCase(code)) {
                dto = JSONUtil.toBean(jsonObject.getString("data"), TransactionMessageRespDto.class);
            }
        } catch (Exception e) {
            log.error("获取transaction返回，message：{}", e.getMessage());
        }
        return dto;
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
        if (auctionSetting.getIsFinished() != null && NftAuctionStatusEnum.FINISHED.getCode() == auctionSetting.getIsFinished()) {
            NftMarketOrderEntity order = nftMarketOrderService.queryByAuctionId(nftEquipment.getAuctionId());
            if (order == null || NftStateEnum.IN_SETTLEMENT.getCode() != order.getStatus()) {
                return resp;
            }
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
                if (nftOffer.getCancelTime() == null || nftOffer.getCancelTime() == 0) {
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
        NftEquipment nftEquipment = nftEquipmentMapper.getById(req.getNftId());
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
            return null;
        }
        NftEquipment nftEquipment = nftEquipmentService.queryByMintAddress(auctionBiding.getMintAddress());
        if (nftEquipment == null
                || !auctionBiding.getAuctionId().equals(nftEquipment.getAuctionId())
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return null;
        }
        // 归属人验证
        //ucUserService.ownerValidation(nftEquipment.getOwner());
        // 已托管不能接受报价
        //if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
        //    throw new GenericException(GameErrorCodeEnum.ERR_10008_NFT_ITEM_IS_DEPOSIT);
        //}

        //更新背包状态 undeposited  userId,owner
        NftPublicBackpackEntity backpackEntity = nftPublicBackpackService.queryByEqNftId(nftEquipment.getId());
        if (backpackEntity.getType().equals(NFTEnumConstant.NftTypeEnum.HERO.getCode())
                && backpackEntity.getIsTraded().equals(WhetherEnum.NO.value())) {
            // 发送皮肤nft 首次购买 成功消息
            SkinNftFirstBuySuccessDto dto = new SkinNftFirstBuySuccessDto();
            dto.setNftPicId(backpackEntity.getNftPicId());
            kafkaProducer.sendAsync(KafkaTopic.SKIN_NFT_BUY_SUCCESS_FIRST, JSONUtil.toJsonStr(dto));
        }
        try {
            GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(auctionBiding.getBuyer());
            log.info("acceptOffer update backpack getUserInfoByPublicAddress,param:{},result:{}", auctionBiding.getBuyer(), result);
            backpackEntity.setUserId(result.getData().getId());
            backpackEntity.setOwner(auctionBiding.getBuyer());
        } catch (Exception e) {
            log.error("内部请求uc获取用户信息失败");
        }
        backpackEntity.setIsTraded(WhetherEnum.YES.value());
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
            log.info("请求NFT接受报价成功接口返回，  result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
            String data = jsonObject.getString("data");
            return JSONUtil.toBean(data, NftOfferDetailResp.class);
        } catch (Exception e) {
            log.error("NFT接受报价成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT accept offer failed!");
        }
    }

    @Override
    public void auctionSuccess(NftSaleSuccessReq req) {
        NftAuctionHouseBiding auctionBiding = nftAuctionHouseBidingService.getById(req.getBidingId());
        if (auctionBiding == null) {
            return;
        }
        Long auctionId = auctionBiding.getAuctionId();
        NftEquipment nftEquipment = nftEquipmentService.queryByMintAddress(auctionBiding.getMintAddress());
        if (nftEquipment == null
                || !auctionBiding.getAuctionId().equals(nftEquipment.getAuctionId())
                || WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            return;
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
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT拍卖达成交易成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT transaction failed!");
        }
        // 退还托管费
        NftRefundFeeReq feeReq = new NftRefundFeeReq();
        feeReq.setAuctionId(auctionId);
        refundFee(feeReq);
    }

    @Override
    public void cancelOffer(NftCancelOfferReq req) {
        NftAuctionHouseBiding auctionBiding = nftAuctionHouseBidingService.getById(req.getBidingId());
        if (auctionBiding == null) {
            return;
        }
        if (auctionBiding.getCancelTime() != null && auctionBiding.getCancelTime() != 0) {
            return;
        }
        // offer归属人验证
        //ucUserService.ownerValidation(auctionBiding.getBuyer());
        // 调用/api/auction/cancelBid通知，取消出价成功
        String params = String.format("bidingId=%s&receipt=%s&signature=%s", req.getBidingId(), auctionBiding.getReceipt(), req.getSignature());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getAuctionCancelBid() + "?" + params;
        log.info("NFT取消出价成功，开始通知， url:{}， params:{}", url, params);
        try {
            HttpResponse response = HttpRequest.get(url)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
            log.info("NFT取消出价成功通知返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT取消出价成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT cancel offer failed!");
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

    private Integer convertOrderState(NftEquipment nftEquipment, NftMarketOrderEntity marketOrder, Boolean isLock) {
        int state;
        if (WhetherEnum.YES.value() == nftEquipment.getIsDelete()) {
            return NftStateEnum.BURNED.getCode();
        }
        if (WhetherEnum.YES.value() == nftEquipment.getIsDeposit()) {
            if (isLock) {
                state = NftStateEnum.LOCKED.getCode();
            } else {
                state = NftStateEnum.DEPOSITED.getCode();
            }
        } else {
            if (NftOrderTypeEnum.ON_AUCTION.getCode() == nftEquipment.getOnSale()) {
                // 拍卖中
                state = NftStateEnum.ON_AUCTION.getCode();
                if (marketOrder != null && NftStateEnum.IN_SETTLEMENT.getCode() == marketOrder.getStatus()) {
                    // 结算中
                    state = NftStateEnum.IN_SETTLEMENT.getCode();
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

    private void initRefundFee(BigDecimal amount, String toAddress, NftFeeRecordEntity nftFeeRecord) {
        // 调用/api/admin/refundFee，请求NFT托管费退还
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getRefundFee();
        TransferSolMessageDto dto = new TransferSolMessageDto();
        dto.setAmount(amount);
        dto.setToAddress(toAddress);
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT托管费退还开始请求， url:{}， params:{}", url, param);
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
            String data = jsonObject.getString("data");
            if (StringUtils.isNotBlank(data)) {
                JSONObject dataJson = JSONObject.parseObject(data);
                nftFeeRecord.setTxHash(dataJson.getString("txHash"));
            }
        } catch (Exception e) {
            log.error("请求NFT托管费退还接口失败，message：{}", e.getMessage());
            nftFeeRecord.setStatus(WhetherEnum.NO.value());
        }
    }

    private String getUserById(Long userId) {
        String publicAddress = null;
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(userId);
            publicAddress = result.getData();
        } catch (Exception e) {
            log.error("内部请求uc获取用户公共地址失败");
        }
        return publicAddress;
    }

}
