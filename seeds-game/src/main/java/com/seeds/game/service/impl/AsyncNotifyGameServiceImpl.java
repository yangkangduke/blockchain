package com.seeds.game.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.game.dto.request.NftMintSuccessReq;
import com.seeds.game.dto.request.internal.NftEventNotifyReq;
import com.seeds.game.dto.response.MintSuccessMessageResp;
import com.seeds.game.entity.*;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: hewei
 * @date 2023/4/4
 */
@Service
@Slf4j
public class AsyncNotifyGameServiceImpl implements IAsyncNotifyGameService {

    @Autowired
    @Lazy
    private INftEventService nftEventService;
    @Autowired
    private INftEventEquipmentService eventEquipmentService;

    @Autowired
    private RemoteGameService remoteGameService;

    @Autowired
    private IServerRoleService serverRoleService;

    @Autowired
    private IServerRegionService serverRegionService;

    @Autowired
    private CallGameApiLogService callGameApiLogService;

    @Autowired
    private INftAttributeService attributeService;
    @Autowired
    @Lazy
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private NftMarketPlaceService marketPlaceService;

    @Autowired
    private INftReferencePriceService nftReferencePriceService;

    @Async
    @Override
    public void mintSuccess(NftMintSuccessReq mintSuccessReq) {

        try {
            Thread.sleep(3000); // 5000毫秒 = 5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NftEvent nftEvent = nftEventService.getById(mintSuccessReq.getEventId());
        NftEventEquipment equipment = eventEquipmentService
                .getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));

        // 调用/api/equipment/mintSuccess  mint成功
//        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getMintSuccess();
//        MintSuccessMessageDto dto = new MintSuccessMessageDto();
//        BeanUtils.copyProperties(mintSuccessReq, dto);
//        String param = JSONUtil.toJsonStr(dto);
//        log.info("mint成功，开始通知， url:{}， params:{}", url, param);
//        MintSuccessMessageResp data = null;
//        try {
//            HttpResponse response = HttpRequest.post(url)
//                    .timeout(5 * 1000)
//                    .header("Content-Type", "application/json")
//                    .body(param)
//                    .execute();
//            JSONObject jsonObject = JSONObject.parseObject(response.body());
//            data = JSONObject.toJavaObject((JSON) jsonObject.get("data"), MintSuccessMessageResp.class);
//
//        } catch (Exception e) {
//            log.error("mint成功通知失败，message：{}", e.getMessage());
//        }

        // 通知游戏mint或者合成成功   // optType 1 mint成功,2取消
        this.callGameNotify(nftEvent.getServerRoleId(), mintSuccessReq.getAutoDeposite(), 1, mintSuccessReq.getMintAddress(), equipment.getItemType(), equipment.getAutoId(), equipment.getConfigId(), nftEvent.getServerRoleId(), equipment.getAttributes());

        MintSuccessMessageResp data = new MintSuccessMessageResp();
        data.setId(nftEvent.getId());
        data.setName("Seeds NFT #" + nftEvent.getId());
        data.setOwner("xxaa");
        data.setTokenId(nftEvent.getId());
        data.setMintAddress(mintSuccessReq.getMintAddress());

        //   插入公共背包（作为合成材料的nft标记为被消耗）、插入属性表、更新event事件状态、通知游戏
        if (Objects.nonNull(data)) {
            long currentTimeMillis = System.currentTimeMillis();
            NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
            backpackEntity.setEqNftId(data.getId());
            backpackEntity.setName(nftEvent.getName());
            backpackEntity.setTokenName(data.getName());
            backpackEntity.setOwner(data.getOwner());
            backpackEntity.setUserId(nftEvent.getUserId());
            backpackEntity.setTokenId(data.getTokenId());
            backpackEntity.setTokenAddress(data.getMintAddress());
            backpackEntity.setCreatedBy(nftEvent.getUserId());
            backpackEntity.setUpdatedBy(nftEvent.getUserId());
            backpackEntity.setCreatedAt(currentTimeMillis);
            backpackEntity.setUpdatedAt(currentTimeMillis);
            if (mintSuccessReq.getAutoDeposite().equals(WhetherEnum.YES.value())) {
                // 自动托管
                backpackEntity.setServerRoleId(nftEvent.getServerRoleId());
                backpackEntity.setIsConfiguration(NftConfigurationEnum.ASSIGNED.getCode());
                backpackEntity.setState(NFTEnumConstant.NFTStateEnum.DEPOSITED.getCode());
            } else {
                backpackEntity.setServerRoleId(0L);
                backpackEntity.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
                backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
            }
            backpackEntity.setDesc(NFTEnumConstant.NFTDescEnum.SEEDS_EQUIP.getDesc());
            backpackEntity.setImage(equipment.getImageUrl());
            backpackEntity.setType(equipment.getItemType());
            backpackEntity.setItemId(equipment.getConfigId());
            backpackEntity.setItemTypeId(equipment.getItemId());
            backpackEntity.setAutoId(equipment.getAutoId());
            backpackEntity.setItemId(equipment.getConfigId());
            backpackEntity.setGrade(equipment.getLvl());
            int durability = 0;
            int rarityAttr = 0;
            try {
                JSONObject jsonObject = JSONObject.parseObject(equipment.getAttributes());
                durability = (int) jsonObject.get("durability");
                rarityAttr = (int) jsonObject.get("rarityAttr");
            } catch (Exception e) {
                e.printStackTrace();
            }
            backpackEntity.setAttributes(equipment.getAttributes());
            // 获取参考单价
            BigDecimal unitPrice = nftReferencePriceService.queryLowGradeAveragePrice(backpackEntity.getItemId());
            // 设置参考价
            backpackEntity.setProposedPrice(unitPrice.multiply(new BigDecimal(durability)));
            nftPublicBackpackService.save(backpackEntity);

            // 如果是合成，作为合成材料的nft标记为销毁的状态
            if (nftEvent.getType().equals(NFTEnumConstant.NFTEventType.COMPOUND.getCode())) {
                NftEventEquipment consumeNft = eventEquipmentService
                        .getOne(new LambdaQueryWrapper<NftEventEquipment>()
                                .eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.YES.value())
                                .eq(NftEventEquipment::getIsNft, WhetherEnum.YES.value()));
                NftPublicBackpackEntity nftbackpack = nftPublicBackpackService.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, consumeNft.getAutoId()));
                nftbackpack.setState(NFTEnumConstant.NFTStateEnum.BURNED.getCode());
                nftPublicBackpackService.updateById(nftbackpack);
            }
            insetAttr(equipment, data, durability, rarityAttr);

            // 更新event事件状态
            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTED.getCode());
            nftEventService.updateById(nftEvent);
        }

    }

    // nft 操作通知  // optType 1 mint成功,2取消
    @Async
    @Override
    public void callGameNotify(Long serverRoleId, Integer autoDeposite, Integer optType, String tokenAddress, Integer itemType, Long autoId, Long configId, Long accId, String attribute) {

        ServerRegionEntity serverRegion = serverRegionService.queryByServerRoleId(serverRoleId);

        GenericDto<String> dto = null;
        try {
            dto = remoteGameService.queryGameApi(1L, ApiType.NFT_TO_NFT_NOTIFY.getCode());
        } catch (Exception e) {
            log.info("rpc all seeds-admin ,queryGameApi error {}", e.getMessage());
        }
        String notifyUrl = "http://" + serverRegion.getInnerHost() + dto.getData();
        NftEventNotifyReq notifyReq = new NftEventNotifyReq();
        notifyReq.setOptType(optType);
        notifyReq.setAutoId(autoId);
        notifyReq.setConfigId(configId);
        notifyReq.setAccId(accId);
        notifyReq.setType(itemType);
        // 1 mint成功,2取消
        if (optType.equals(NFTEnumConstant.NftEventOptEnum.SUCCESS.getCode())) {
            notifyReq.setRegionName(serverRegion.getRegionName());
            notifyReq.setServerName(serverRegion.getGameServerName());
            notifyReq.setState(autoDeposite.equals(WhetherEnum.YES.value()) ? NFTEnumConstant.NFTStateEnum.DEPOSITED.getCode() : NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
            notifyReq.setTokenAddress(tokenAddress);
            notifyReq.setPrice(getPrice(attribute));
        }
        String params = JSONUtil.toJsonStr(notifyReq);

        log.info("开始请求游戏 nft notify 接口， url:{}， params:{}", notifyUrl, params);
        HttpResponse response = null;
        try {
            response = HttpRequest.post(notifyUrl)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .body(params)
                    .execute();
        } catch (Exception e) {
            // 记录调用错误日志
            errorLog(notifyUrl, params, e.getMessage());
            //  throw new GenericException("Failed to call game-api to notify,connect timed out");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String ret = jsonObject.getString("ret");
        log.info("请求游戏  nft notify 接口返回，  result:{}", response.body());
        if (!"ok".equalsIgnoreCase(ret)) {
            // 记录调用错误日志
            errorLog(notifyUrl, params, ret);
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }

    }

    private String getPrice(String attribute) {
        String price = "0";
        int durability = 0;
        try {
            JSONObject jsonObject = JSONObject.parseObject(attribute);
            durability = (int) jsonObject.get("durability");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置参考价
        try {
            BigDecimal usdRate = marketPlaceService.usdRate(CurrencyEnum.SOL.getCode());
            price = new BigDecimal(durability).divide(usdRate, 2, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private void errorLog(String url, String params, String msg) {
        CallGameApiErrorLogEntity errorLog = new CallGameApiErrorLogEntity();
        errorLog.setCallTime(System.currentTimeMillis());
        errorLog.setUrl(url);
        errorLog.setMethod(HttpMethod.POST.name());
        errorLog.setParams(params);
        errorLog.setMsg(msg);
        callGameApiLogService.save(errorLog);
    }

    private void insetAttr(NftEventEquipment equipment, MintSuccessMessageResp data, int durability, int rarityAttr) {
        NftAttributeEntity attributeEntity = new NftAttributeEntity();
        attributeEntity.setEqNftId(data.getId());
        attributeEntity.setMintAddress(data.getMintAddress());
        attributeEntity.setGrade(equipment.getLvl());
        attributeEntity.setDurability(durability);
        attributeEntity.setRarityAttr(rarityAttr);
        attributeEntity.setSpecialAttrDesc(equipment.getSpecialAttrDesc());
        attributeEntity.setPassiveAttrDesc(equipment.getPassiveAttrDesc());
        attributeEntity.setBaseAttrValue(equipment.getBaseAttrValue());
        attributeEntity.setRarityAttrValue(equipment.getRarityAttrValue());
        attributeService.save(attributeEntity);
    }
}
