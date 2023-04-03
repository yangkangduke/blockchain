package com.seeds.game.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.game.config.SeedsApiConfig;
import com.seeds.game.dto.request.NftMintSuccessReq;
import com.seeds.game.dto.request.external.MintSuccessMessageDto;
import com.seeds.game.dto.request.internal.NftEventAddReq;
import com.seeds.game.dto.request.internal.NftEventEquipmentReq;
import com.seeds.game.dto.request.internal.NftEventNotifyReq;
import com.seeds.game.dto.request.internal.NftEventPageReq;
import com.seeds.game.dto.response.EventTypeNum;
import com.seeds.game.dto.response.NftEventEquipmentResp;
import com.seeds.game.dto.response.NftEventResp;
import com.seeds.game.entity.*;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftEventMapper;
import com.seeds.game.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
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
    private SeedsApiConfig seedsApiConfig;

    @Override
    @Transactional
    public IPage<NftEventResp> getPage(NftEventPageReq req) {


        LambdaQueryWrapper<NftEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(req.getType()), NftEvent::getType, req.getType())
                .eq(NftEvent::getUserId, req.getUserId())
                .in(!CollectionUtils.isEmpty(req.getStatus()), NftEvent::getStatus, req.getStatus());

        wrapper.last(!CollectionUtils.isEmpty(req.getSorts()), NftEventPageReq.getOrderByStatement(req.getSorts()));
        Page<NftEvent> page = new Page<>(req.getCurrent(), req.getSize());
        List<NftEvent> records = this.page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        // 获取事件涉及的装备
        List<Long> eventIds = records.stream().map(p -> p.getId()).collect(Collectors.toList());
        Map<Long, List<NftEventEquipment>> map = eventEquipmentService.list(new LambdaQueryWrapper<NftEventEquipment>().in(NftEventEquipment::getEventId, eventIds))
                .stream().collect(Collectors.groupingBy(NftEventEquipment::getEventId));

        // 更新未读数（click设置为以点击）
        baseMapper.updateClick(req.getType(), req.getUserId());

        return page.convert(p -> {
            NftEventResp resp = new NftEventResp();
            BeanUtils.copyProperties(p, resp);
            List<NftEventEquipment> equipments = map.get(p.getId());
            if (!CollectionUtils.isEmpty(equipments)) {
                resp.setEventEquipments(CglibUtil.copyList(equipments, NftEventEquipmentResp::new));
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
            // 必须有一个是nft,且是托管状态 todo  合成时作为材料的nft标记为临时锁定，不能被withdraw
        }
        // 记录toNFT请求
        NftEvent nftEvent = new NftEvent();
        BeanUtils.copyProperties(req, nftEvent);
        List<NftEventEquipmentReq> eventEquipmentReqs = req.getEquipments().stream().filter(p -> p.getIsConsume().equals(WhetherEnum.NO.value())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(eventEquipmentReqs)) {
            nftEvent.setName(eventEquipmentReqs.get(0).getName());
        }
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.PENDING.getCode());
        nftEvent.setCreatedAt(System.currentTimeMillis());
        nftEvent.setCreatedBy(req.getUserId());
        nftEvent.setUpdatedAt(System.currentTimeMillis());
        nftEvent.setUpdatedBy(req.getUserId());
        this.save(nftEvent);
        List<NftEventEquipment> equipments = CglibUtil.copyList(req.getEquipments(), NftEventEquipment::new);

        equipments.forEach(p -> {
            p.setEventId(nftEvent.getId());
        });
        eventEquipmentService.saveBatch(equipments);

        // todo 后期去掉 方便游戏测试直接mint成功
        success(nftEvent);
    }

    @Async
    void success(NftEvent nftEvent) {

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NftMintSuccessReq req1 = new NftMintSuccessReq();
        req1.setEventId(nftEvent.getId());
        req1.setAutoDeposite(1);
        req1.setTokenAddress(nftEvent.getId() + "token0xabc");
        NftMintSuccessReq.Equipment equipment = new NftMintSuccessReq.Equipment();
        equipment.setNftId(nftEvent.getId().intValue());
        equipment.setNftTokenId(nftEvent.getId().intValue());
        req1.setZequipment(equipment);
        this.OptSuccess(req1);
    }

    @Override
    public Boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public Boolean cancel(Long id) {

        NftEvent nftEvent = new NftEvent();
        nftEvent.setId(id);
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.CANCELLED.getCode());

        // 通知游戏方事件取消
        NftEvent event = this.getById(id);
        NftEventEquipment equipment = eventEquipmentService.getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, id).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));
        this.callGameNotify(event.getServerRoleId(), 0, 2, "", equipment.getItemType(), equipment.getAutoId(), equipment.getConfigId(), event.getServerRoleId());
        // 更新本地数据库

        List<NftEventEquipment> equipments = eventEquipmentService.list(new LambdaQueryWrapper<NftEventEquipment>().in(NftEventEquipment::getEventId, id));
        equipments.stream().forEach(p -> {
            p.setAutoId(p.getAutoId() * 10);
        });
        eventEquipmentService.updateBatchById(equipments);

        return this.updateById(nftEvent);
    }

    @Override
    public List<EventTypeNum> getTypeNum(Long userId) {
        // 获取各个类型的数量
        List<EventTypeNum> typeCount = baseMapper.getTypeCount(userId);
        return typeCount;
    }

    @Override
    @Transactional
    public void OptSuccess(NftMintSuccessReq mintSuccessReq) {
        NftEvent nftEvent = this.getById(mintSuccessReq.getEventId());
        NftEventEquipment equipment = eventEquipmentService
                .getOne(new LambdaQueryWrapper<NftEventEquipment>().eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));

        // 调用/api/equipment/mintSuccess  mint成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getMintSuccess();
        MintSuccessMessageDto dto = new MintSuccessMessageDto();
        BeanUtils.copyProperties(mintSuccessReq, dto);
        String param = JSONUtil.toJsonStr(dto);
        log.info("mint成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpRequest.post(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            log.error("mint成功通知失败，message：{}", e.getMessage());
        }

//        // 通知游戏mint或者合成成功   // optType 1 mint成功,2取消
//        this.callGameNotify(nftEvent.getServerRoleId(), mintSuccessReq.getAutoDeposite(), 1, mintSuccessReq.getTokenAddress(), equipment.getItemType(), equipment.getAutoId(), equipment.getConfigId(), nftEvent.getServerRoleId());

        //   插入公共背包（作为合成材料的nft标记为被消耗）、插入属性表、更新event事件状态、通知游戏
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setEqNftId(Long.valueOf(mintSuccessReq.getZequipment().getNftId()));
        backpackEntity.setName(nftEvent.getName());
        backpackEntity.setUserId(nftEvent.getUserId());
        backpackEntity.setTokenId(mintSuccessReq.getZequipment().getNftTokenId().toString());
        backpackEntity.setTokenAddress(mintSuccessReq.getTokenAddress());
        backpackEntity.setCreatedBy(nftEvent.getUserId());
        backpackEntity.setUpdatedBy(nftEvent.getUserId());
        backpackEntity.setCreatedAt(System.currentTimeMillis());
        backpackEntity.setUpdatedAt(System.currentTimeMillis());

        if (mintSuccessReq.getAutoDeposite().equals(1)) {
            backpackEntity.setServerRoleId(nftEvent.getServerRoleId());
            backpackEntity.setIsConfiguration(NftConfigurationEnum.ASSIGNED.getCode());
            backpackEntity.setState(NFTEnumConstant.NFTStateEnum.DEPOSITED.getCode());
        } else {
            backpackEntity.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
            backpackEntity.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        }
        //backpackEntity.setDesc();
        backpackEntity.setImage(equipment.getImageUrl());
        backpackEntity.setType(equipment.getItemType());
        backpackEntity.setItemId(equipment.getConfigId());
        backpackEntity.setItemTypeId(equipment.getItemId());
        backpackEntity.setAutoId(equipment.getAutoId());
        backpackEntity.setItemId(equipment.getConfigId());
        backpackEntity.setGrade(equipment.getLvl());
        HashMap<String, Object> attr = new HashMap<>();

        attr.put("level", equipment.getLvl());
        JSONObject jsonObject = JSONObject.parseObject(equipment.getAttributes());
        Integer durability = Integer.valueOf(jsonObject.getString("durability"));
        attr.put("durability", durability);
        String jsonStr = JSONUtil.toJsonStr(attr);
        backpackEntity.setAttributes(jsonStr);
        nftPublicBackpackService.save(backpackEntity);

        // 如果是合成，作为合成材料的nft标记为临时锁定的状态
        if (nftEvent.getType().equals(NFTEnumConstant.NFTEventType.COMPOUND.getCode())) {
            NftEventEquipment consumeNft = eventEquipmentService
                    .getOne(new LambdaQueryWrapper<NftEventEquipment>()
                            .eq(NftEventEquipment::getEventId, nftEvent.getId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.YES.value())
                            .eq(NftEventEquipment::getIsNft, WhetherEnum.YES.value()));
            NftPublicBackpackEntity nftbackpack = nftPublicBackpackService.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, consumeNft.getAutoId()));
            nftbackpack.setState(NFTEnumConstant.NFTStateEnum.LOCK.getCode());
            nftPublicBackpackService.updateById(nftbackpack);
        }
        //  插入属性表
        NftAttributeEntity attributeEntity = new NftAttributeEntity();
        attributeEntity.setEqNftId(Long.valueOf(mintSuccessReq.getZequipment().getNftId()));
        attributeEntity.setGrade(equipment.getLvl());
        attributeEntity.setDurability(durability);
        attributeEntity.setBaseAttrValue(equipment.getBaseAttrValue());
        attributeEntity.setRarityAttrValue(equipment.getRarityAttrValue());
        attributeService.save(attributeEntity);

        // 更新event事件状态
        nftEvent.setStatus(NFTEnumConstant.NFTEventStatus.MINTED.getCode());
        this.updateById(nftEvent);
    }


    // nft 操作通知  // optType 1 mint成功,2取消
    private void callGameNotify(Long serverRoleId, Integer autoDeposite, Integer optType, String tokenAddress, Integer itemType, Long autoId, Long configId, Long accId) {

        ServerRegionEntity serverRegion = this.getServerRegionEntity(serverRoleId);

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
        if (optType.equals(1)) {
            notifyReq.setRegionName(serverRegion.getRegionName());
            notifyReq.setServerName(serverRegion.getGameServerName());
            notifyReq.setState(autoDeposite.equals(1) ? 3 : 4);
            notifyReq.setTokenAddress(tokenAddress);
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
            errorLog(notifyUrl, params, "connect timed out");
            throw new GenericException("Failed to call game-api to notify,connect timed out");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String ret = jsonObject.getString("ret");
        log.info("请求游戏  nft notify 接口返回，  result:{}", response.body());
        if (!"ok".equalsIgnoreCase(ret)) {
            // 记录调用错误日志
            errorLog(notifyUrl, params, ret);
            throw new GenericException("Failed to call game-api to notify, " + ret);
        }

    }


    private ServerRegionEntity getServerRegionEntity(Long id) {
        ServerRoleEntity role = serverRoleService.getById(id);
        if (Objects.isNull(role)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20002_ROLE_NOT_EXIST);
        }
        ServerRegionEntity serverRegion = serverRegionService.getOne(new LambdaQueryWrapper<ServerRegionEntity>()
                .eq(ServerRegionEntity::getRegion, role.getRegion())
                .eq(ServerRegionEntity::getGameServer, role.getGameServer()));

        if (Objects.isNull(serverRegion)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20004_SERVER_REGION_NOT_EXIST);
        }
        return serverRegion;
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
}
