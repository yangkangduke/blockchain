package com.seeds.game.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.config.SeedsApiConfig;
import com.seeds.game.dto.MetadataAttrDto;
import com.seeds.game.dto.request.ComposeSuccessReq;
import com.seeds.game.dto.request.MintSuccessReq;
import com.seeds.game.dto.request.NftMintEquipReq;
import com.seeds.game.dto.request.NftMintSuccessReq;
import com.seeds.game.dto.request.external.EquipComposeRequestDto;
import com.seeds.game.dto.request.external.EquipMintRequestDto;
import com.seeds.game.dto.request.external.MintSuccessMessageDto;
import com.seeds.game.dto.request.internal.NftEventAddReq;
import com.seeds.game.dto.request.internal.NftEventEquipmentReq;
import com.seeds.game.dto.request.internal.NftEventPageReq;
import com.seeds.game.dto.response.EventTypeNum;
import com.seeds.game.dto.response.MintSuccessMessageResp;
import com.seeds.game.dto.response.NftEventEquipmentResp;
import com.seeds.game.dto.response.NftEventResp;
import com.seeds.game.entity.*;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftEventMapper;
import com.seeds.game.mq.producer.KafkaProducer;
import com.seeds.game.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * nft通知 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-03-23
 */
@Service
@Slf4j
public class NftEventServiceImpl extends ServiceImpl<NftEventMapper, NftEvent> implements INftEventService {

    @Autowired
    private INftEventEquipmentService eventEquipmentService;

    @Autowired
    private INftAttributeService attributeService;
    @Autowired
    @Lazy
    private INftPublicBackpackService nftPublicBackpackService;

    @Autowired
    private SeedsApiConfig seedsApiConfig;

    @Autowired
    private ItemImageService itemImageService;

    @Autowired
    private IUpdateBackpackErrorLogService updateBackpackErrorLogService;

    @Autowired
    private NftMarketPlaceService marketPlaceService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private IAsyncNotifyGameService asyncNotifyGameService;

    @Override
    @Transactional
    public IPage<NftEventResp> getPage(NftEventPageReq req) {


        LambdaQueryWrapper<NftEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(req.getType()), NftEvent::getType, req.getType())
                .eq(NftEvent::getUserId, req.getUserId())
                .in(!CollectionUtils.isEmpty(req.getStatus()), NftEvent::getStatus, req.getStatus());

        wrapper.last(NftEventPageReq.getOrderByStatement(req.getSorts()));
        Page<NftEvent> page = new Page<>(req.getCurrent(), req.getSize());
        List<NftEvent> records = this.page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        // 获取事件涉及的装备
        List<Long> eventIds = records.stream().map(p -> p.getId()).collect(Collectors.toList());
        List<NftEventEquipment> eventEquipments = eventEquipmentService.list(new LambdaQueryWrapper<NftEventEquipment>().in(NftEventEquipment::getEventId, eventIds));
        Map<Long, List<NftEventEquipment>> map = eventEquipments
                .stream().collect(Collectors.groupingBy(NftEventEquipment::getEventId));

        List<Long> autoIds = eventEquipments.stream().map(p -> p.getAutoId()).collect(Collectors.toList());
        Map<Long, String> autoIdTokneAddressMap = nftPublicBackpackService.queryTokenAddressByAutoIds(autoIds);
        // 更新未读数（click设置为以点击）
        baseMapper.updateClick(req.getType(), req.getUserId());

        return page.convert(p -> {
            NftEventResp resp = new NftEventResp();
            BeanUtils.copyProperties(p, resp);
            List<NftEventEquipment> equipments = map.get(p.getId());
            if (!CollectionUtils.isEmpty(equipments)) {
                List<NftEventEquipmentResp> equipmentResps = CglibUtil.copyList(equipments, NftEventEquipmentResp::new);
                equipmentResps.forEach(i -> {
                    i.setMintAddress(autoIdTokneAddressMap.get(i.getAutoId()));
                });
                resp.setEventEquipments(equipmentResps);
            }
            return resp;
        });

    }

    @Override
    public Long getHandleFlag(Long userId) {
        return this.count(new LambdaQueryWrapper<NftEvent>().eq(NftEvent::getUserId, userId).eq(NftEvent::getClick, WhetherEnum.NO.value()));
    }

    @Override
    @Transactional
    public void toNft(NftEventAddReq req) {
        if (req.getType().equals(NFTEnumConstant.NFTEventType.COMPOUND.getCode())) {
            // 合成时作为材料的nft标记为临时锁定，
            List<NftEventEquipmentReq> equipmentReqs = req.getEquipments().stream()
                    .filter(p -> p.getIsNft().equals(WhetherEnum.YES.value()) && p.getIsConsume().equals(WhetherEnum.YES.value()))
                    .collect(Collectors.toList());

            List<NftPublicBackpackEntity> updateList = equipmentReqs.stream().map(p -> {
                NftPublicBackpackEntity nftBackpack = new NftPublicBackpackEntity();
                nftBackpack.setAutoId(p.getAutoId());
                nftBackpack.setState(NFTEnumConstant.NFTStateEnum.LOCK.getCode());
                return nftBackpack;
            }).collect(Collectors.toList());
            log.info("合成材料标记为锁定状态，param ：{}", JSONUtil.toJsonStr(updateList));
            nftPublicBackpackService.updateBatchByQueryWrapper(updateList, item ->
                    new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, item.getAutoId()));
        }
        // 记录toNFT请求
        NftEvent nftEvent = new NftEvent();
        BeanUtils.copyProperties(req, nftEvent);
        List<NftEventEquipmentReq> eventEquipmentReqs = req.getEquipments().stream().filter(p -> p.getIsConsume().equals(WhetherEnum.NO.value())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(eventEquipmentReqs)) {
            try {
                nftEvent.setName(URLDecoder.decode(eventEquipmentReqs.get(0).getName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.PENDING.getCode());
        nftEvent.setCreatedAt(System.currentTimeMillis());
        nftEvent.setCreatedBy(req.getUserId());
        nftEvent.setUpdatedAt(System.currentTimeMillis());
        nftEvent.setUpdatedBy(req.getUserId());
        this.save(nftEvent);
        req.getEquipments().forEach(p -> {
            try {
                p.setName(URLDecoder.decode(p.getName(), "UTF-8"));
                p.setBaseAttrValue(handleStr(URLDecoder.decode(p.getBaseAttrValue(), "UTF-8")));
                p.setRarityAttrValue(URLDecoder.decode(p.getRarityAttrValue(), "UTF-8"));
                p.setSpecialAttrDesc(handleStr(URLDecoder.decode(p.getSpecialAttrDesc(), "UTF-8")));
                p.setPassiveAttrDesc(removePrefix(URLDecoder.decode(p.getPassiveAttrDesc(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        List<NftEventEquipment> equipments = CglibUtil.copyList(req.getEquipments(), NftEventEquipment::new);

        equipments.forEach(p -> {
            int durability = 0;
            try {
                JSONObject jsonObject = JSONObject.parseObject(p.getAttributes());
                durability = (int) jsonObject.get("durability");
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.setDurability(durability);
            p.setEventId(nftEvent.getId());
            p.setImageUrl(itemImageService.queryImgByItemId(p.getItemId()));
            try {
                p.setName(URLDecoder.decode(p.getName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        eventEquipmentService.saveBatch(equipments);

//        // todo 后期去掉 方便游戏测试直接mint成功
//        NftMintSuccessReq req1 = new NftMintSuccessReq();
//        req1.setEventId(nftEvent.getId());
//        req1.setAutoDeposite(1);
//        req1.setMintAddress(nftEvent.getId() + "token0xabc");
//        asyncNotifyGameService.mintSuccess(req1);
    }


    @Override
    public Boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Boolean cancel(Long id) {
        List<NftEventEquipment> equipments = eventEquipmentService.list(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, id));
        // 合成时作为材料的nft标记为deposit，
        List<NftEventEquipment> equipments1 = equipments.stream()
                .filter(p -> p.getIsNft().equals(WhetherEnum.YES.value()) && p.getIsConsume().equals(WhetherEnum.YES.value()))
                .collect(Collectors.toList());

        List<NftPublicBackpackEntity> updateList = equipments1.stream().map(p -> {
            NftPublicBackpackEntity nftBackpack = new NftPublicBackpackEntity();
            nftBackpack.setAutoId(p.getAutoId());
            nftBackpack.setState(NFTEnumConstant.NFTStateEnum.DEPOSITED.getCode());
            return nftBackpack;
        }).collect(Collectors.toList());
        log.info("合成材料标记为deposit状态，param ：{}", JSONUtil.toJsonStr(updateList));
        nftPublicBackpackService.updateBatchByQueryWrapper(updateList, item ->
                new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, item.getAutoId()));
        NftEvent nftEvent = new NftEvent();
        nftEvent.setId(id);
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.CANCELLED.getCode());

        // 通知游戏方事件取消
        NftEvent event = this.getById(id);
        NftEventEquipment equipment = eventEquipmentService.getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, id).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));
        asyncNotifyGameService.callGameNotify(event.getServerRoleId(), WhetherEnum.NO.value(), NFTEnumConstant.NftEventOptEnum.CANCEL.getCode(), "", equipment.getItemType(), equipment.getAutoId(), equipment.getConfigId(), event.getServerRoleId());
        // 更新本地数据库
        return this.updateById(nftEvent);
    }

    @Override
    public List<EventTypeNum> getTypeNum(Long userId) {
        // 获取各个类型的数量
        List<EventTypeNum> typeCount = baseMapper.getTypeCount(userId);
        return typeCount;
    }

    @Override
    @Transactional(noRollbackFor = GenericException.class)
    public void mintSuccess(NftMintSuccessReq mintSuccessReq) {
        NftEvent nftEvent = this.getById(mintSuccessReq.getEventId());
        NftEventEquipment equipment = eventEquipmentService
                .getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));

        // 调用/api/equipment/mintSuccess  mint成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getMintSuccess();
        MintSuccessMessageDto dto = new MintSuccessMessageDto();
        BeanUtils.copyProperties(mintSuccessReq, dto);
        dto.setTokenAddress("");
        String param = JSONUtil.toJsonStr(dto);
        log.info("mint成功，开始通知， url:{}， params:{}", url, param);
        MintSuccessMessageResp data = null;
        HttpResponse response = null;
        try {
            response = HttpRequest.post(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            log.error("mint成功通知失败，message：{}", e.getMessage());
            recordNftEventLog(mintSuccessReq.getEventId(), mintSuccessReq.getMintAddress());
            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
            this.updateById(nftEvent);
            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_LATER);
        }
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        log.info(" mint成功--result:{}", jsonObject);
        if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
            // 生成metadata
            data = JSONObject.toJavaObject((JSON) jsonObject.get("data"), MintSuccessMessageResp.class);
            // 通知游戏方，更新本地数据库
            updateLocalDB(mintSuccessReq.getAutoDeposite(), mintSuccessReq.getMintAddress(), nftEvent, equipment, data);
            createMetadata(equipment, data.getTokenId());
        } else {
            log.error("mint成功，通知失败，message：{}", jsonObject.get("message"));
            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
            this.updateById(nftEvent);
            recordNftEventLog(mintSuccessReq.getEventId(), mintSuccessReq.getMintAddress());
            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_LATER);
        }

    }


    @Override
    public void mintSuccessCallback(MintSuccessReq req) {
        NftEvent nftEvent = this.getById(req.getEventId());
        if (nftEvent.getStatus().equals(NFTEnumConstant.NFTEventStatus.MINTING.getCode())){
            NftEventEquipment equipment = eventEquipmentService
                    .getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));
            MintSuccessMessageResp data = new MintSuccessMessageResp();
            BeanUtils.copyProperties(req, data);
            //生成metadata
            createMetadata(equipment, data.getTokenId());
            // 通知游戏方，更新本地数据库
            updateLocalDB(nftEvent.getIsDeposit(), req.getMintAddress(), nftEvent, equipment, data);
        }
    }


    private void recordLog(Long eventId, String mintAddress) {
        // 记录失败日志
        UpdateBackpackErrorLog backpackErrorLog = new UpdateBackpackErrorLog();
        backpackErrorLog.setEventId(eventId);
        backpackErrorLog.setMintAddress(mintAddress);
        backpackErrorLog.setCreatedAt(System.currentTimeMillis());
        updateBackpackErrorLogService.save(backpackErrorLog);
    }

    private void updateLocalDB(Integer autoDeposit, String mintAddress, NftEvent nftEvent, NftEventEquipment equipment, MintSuccessMessageResp data) {
        // 通知游戏mint或者合成成功   // optType 1 mint成功,2取消
         asyncNotifyGameService.callGameNotify(nftEvent.getServerRoleId(), autoDeposit, NFTEnumConstant.NftEventOptEnum.SUCCESS.getCode(), mintAddress, equipment.getItemType(), equipment.getAutoId(), equipment.getConfigId(), nftEvent.getServerRoleId());
        //  插入公共背包（作为合成材料的nft标记为被消耗）、插入属性表、更新event事件状态、通知游戏
        if (Objects.nonNull(data)) {
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
            backpackEntity.setCreatedAt(System.currentTimeMillis());
            backpackEntity.setUpdatedAt(System.currentTimeMillis());
            if (autoDeposit.equals(WhetherEnum.YES.value())) {
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
            // 设置参考价
            try {
                BigDecimal usdRate = marketPlaceService.usdRate(CurrencyEnum.SOL.getCode());
                backpackEntity.setProposedPrice(new BigDecimal(durability).divide(usdRate,2,BigDecimal.ROUND_HALF_UP));
            } catch (Exception e) {
                e.printStackTrace();
            }
            nftPublicBackpackService.save(backpackEntity);
            // 如果是合成，作为合成材料的nft标记为销毁的状态
            if (nftEvent.getType().equals(NFTEnumConstant.NFTEventType.COMPOUND.getCode())) {
                List<NftEventEquipment> consumeNfts = eventEquipmentService
                        .list(new LambdaQueryWrapper<NftEventEquipment>()
                                .eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.YES.value())
                                .eq(NftEventEquipment::getIsNft, WhetherEnum.YES.value()));

                List<NftPublicBackpackEntity> updateList = consumeNfts.stream().map(p -> {
                    NftPublicBackpackEntity nftBackpack = new NftPublicBackpackEntity();
                    nftBackpack.setAutoId(p.getAutoId());
                    nftBackpack.setState(NFTEnumConstant.NFTStateEnum.BURNED.getCode());
                    return nftBackpack;
                }).collect(Collectors.toList());
                log.info("合成材料标记为销毁状态，param ：{}", JSONUtil.toJsonStr(updateList));
                nftPublicBackpackService.updateBatchByQueryWrapper(updateList, item ->
                        new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, item.getAutoId()));
            }
            //  插入属性表
            insetAttr(equipment, data, durability, rarityAttr);
            // 更新event事件状态
            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTED.getCode());
            this.updateById(nftEvent);
        }
    }

    @Override
    @Transactional(noRollbackFor = GenericException.class)
    public void composeSuccess(ComposeSuccessReq req) {
        NftEvent nftEvent = this.getById(req.getEventId());
        if (null == nftEvent || !nftEvent.getUserId().equals(UserContext.getCurrentUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_EVENT);
        }

        EquipComposeRequestDto composeRequestDto = new EquipComposeRequestDto();
        composeRequestDto.setEventId(req.getEventId());
        composeRequestDto.setNonce(req.getNonce());
        composeRequestDto.setFeeHash(req.getFeeHash());
        composeRequestDto.setIsDeposit(req.getAutoDeposite());
        composeRequestDto.setSig(req.getSig());
        composeRequestDto.setMintAddresses(req.getMintAddresses());
        composeRequestDto.setWalletAddress(req.getWalletAddress());
        String params = JSONUtil.toJsonStr(composeRequestDto);
        kafkaProducer.sendAsync(KafkaTopic.EQUIP_COMPOSE_REQUEST, params);
        log.info("发送compose请求消息成功，params:{}", params);

        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
        nftEvent.setIsDeposit(req.getAutoDeposite());
        this.updateById(nftEvent);

//        // 调用/api/chainOp/buySuccess通知，购买成功
//        String params = String.format("nonce=%s&feeHash=%s&isDeposit=%s&mintAddresses=%s&sig=%s&walletAddress=%s", req.getNonce(), req.getFeeHash(), req.getAutoDeposite(), req.getMintAddresses(), req.getSig(), req.getWalletAddress());
//        // 调用/api/equipment/compose  合成成功
//        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getCompose() + "?" + params;
//        log.info("合成成功，开始通知， url:{}， params:{}", url, params);
//        MintSuccessMessageResp data = null;
//        HttpResponse response = null;
//        try {
//            response = HttpRequest.get(url)
//                    .timeout(60 * 1000)
//                    .header("Content-Type", "application/json")
//                    .execute();
//        } catch (Exception e) {
//            log.error("NFT合成成功通知失败，message：{}", e.getMessage());
//            //   recordLog(mintSuccessReq.getEventId(), mintSuccessReq.getMintAddress());
//            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
//            this.updateById(nftEvent);
//            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_LATER);
//        }
//        JSONObject jsonObject = JSONObject.parseObject(response.body());
//        log.info(" 合成成功--result:{}", jsonObject);
//        if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
//            data = JSONObject.toJavaObject((JSON) jsonObject.get("data"), MintSuccessMessageResp.class);
//            NftEventEquipment equipment = eventEquipmentService
//                    .getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));
//            updateLocalDB(req.getAutoDeposite(), data.getMintAddress(), nftEvent, equipment, data);
//            //生成metadata
//            createMetadata(equipment, data.getTokenId());
//        } else {
//            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
//            this.updateById(nftEvent);
//            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_LATER);
//        }

    }

    @Override
    @Transactional(noRollbackFor = GenericException.class)
    public void mintEquip(NftMintEquipReq req) {
        NftEvent nftEvent = this.getById(req.getEventId());
        if (null == nftEvent || !nftEvent.getUserId().equals(UserContext.getCurrentUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_EVENT);
        }

        EquipMintRequestDto mintRequestDto = new EquipMintRequestDto();
        mintRequestDto.setEventId(req.getEventId());
        mintRequestDto.setIsDeposit(req.getAutoDeposite());
        mintRequestDto.setFeeHash(req.getFeeHash());
        mintRequestDto.setToUserAddress(req.getToUserAddress());
        String params = JSONUtil.toJsonStr(mintRequestDto);
        kafkaProducer.sendAsync(KafkaTopic.EQUIP_MINT_REQUEST, params);
        log.info("发送mint请求消息成功，params:{}", params);

        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
        nftEvent.setIsDeposit(req.getAutoDeposite());
        this.updateById(nftEvent);

       /* String params = String.format("isDeposit=%s&feeHash=%s&toUserAddress=%s", req.getAutoDeposite(), req.getFeeHash(), req.getToUserAddress());
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getMintEquip() + "?" + params;
        log.info("mintEquip， url:{}， params:{}", url, params);
        MintSuccessMessageResp data = null;
        HttpResponse response = null;
        try {
            response = HttpRequest.get(url)
                    .timeout(60 * 1000)
                    .header("Content-Type", "application/json")
                    .execute();
        } catch (Exception e) {
            log.error("NFT mintEquip 失败，message：{}", e.getMessage());
            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
            this.updateById(nftEvent);
            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_LATER);
        }
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        log.info(" mintEquip 成功--result:{}", jsonObject);
        if (jsonObject.get("code").equals(HttpStatus.SC_OK)) {
            data = JSONObject.toJavaObject((JSON) jsonObject.get("data"), MintSuccessMessageResp.class);
            NftEventEquipment equipment = eventEquipmentService
                    .getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));
            updateLocalDB(req.getAutoDeposite(), data.getMintAddress(), nftEvent, equipment, data);
            //生成metadata
            createMetadata(equipment, data.getTokenId());
        } else {
            nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTING.getCode());
            this.updateById(nftEvent);
            throw new GenericException(GameErrorCodeEnum.ERR_10019_CHECK_LATER);
        }*/
    }


    private void insetAttr(NftEventEquipment equipment, MintSuccessMessageResp data, int durability, int rarityAttr) {
        NftAttributeEntity attributeEntity = new NftAttributeEntity();
        attributeEntity.setEqNftId(data.getId());
        attributeEntity.setMintAddress(data.getMintAddress());
        attributeEntity.setGrade(equipment.getLvl());
        attributeEntity.setDurability(durability);
        attributeEntity.setDurabilityConfig(equipment.getDurabilityConfig());
        attributeEntity.setRarityAttr(rarityAttr);
        attributeEntity.setSpecialAttrDesc(equipment.getSpecialAttrDesc());
        attributeEntity.setPassiveAttrDesc(equipment.getPassiveAttrDesc());
        attributeEntity.setBaseAttrValue(equipment.getBaseAttrValue());
        attributeEntity.setRarityAttrValue(equipment.getRarityAttrValue());
        attributeService.save(attributeEntity);
    }

    private void recordNftEventLog(Long eventId, String mintAddress) {
        // 记录失败日志
        UpdateBackpackErrorLog backpackErrorLog = new UpdateBackpackErrorLog();
        backpackErrorLog.setEventId(eventId);
        backpackErrorLog.setMintAddress(mintAddress);
        backpackErrorLog.setCreatedAt(System.currentTimeMillis());
        updateBackpackErrorLogService.save(backpackErrorLog);
    }


    private static String handleStr(String str) {
        // 去除方括号以及方括号中的字符
        return str.replaceAll("\\[.+?\\]", "").replace("Blade buff", "");
    }

    private static String removePrefix(String str) {
        // 去除方括号以及方括号中的字符
        return str.replace("Passive: ", "");
    }

    private void createMetadata(NftEventEquipment equipment, Long tokenId) {
        MetadataAttrDto metadataAttrDto = new MetadataAttrDto();
        metadataAttrDto.setConfigId(equipment.getConfigId());
        metadataAttrDto.setAutoId(equipment.getAutoId());
        int durability = 0;
        int rarityAttr = 0;
        try {
            JSONObject attr = JSONObject.parseObject(equipment.getAttributes());
            durability = (int) attr.get("durability");
            rarityAttr = (int) attr.get("rarityAttr");
        } catch (Exception e) {
            e.printStackTrace();
        }
        metadataAttrDto.setTokenId(tokenId.intValue());
        metadataAttrDto.setDurability(durability);
        metadataAttrDto.setQuality(equipment.getLvl());
        metadataAttrDto.setRareAttribute(rarityAttr);
        metadataAttrDto.setImage(equipment.getImageUrl());
        metadataAttrDto.setName(NFTEnumConstant.TokenNamePreEnum.SEQN.getName() + tokenId);
        kafkaProducer.sendAsync(KafkaTopic.NFT_MINT_SUCCESS, JSONUtil.toJsonStr(metadataAttrDto));
        log.info("tokenId:{},发送【metadata】数据：{}", tokenId, JSONUtil.toJsonStr(metadataAttrDto));
    }
}
