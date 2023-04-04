package com.seeds.game.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.config.SeedsApiConfig;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.external.DepositSuccessMessageDto;
import com.seeds.game.dto.request.external.TransferNftMessageDto;
import com.seeds.game.dto.request.internal.*;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.*;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NFTEnumConstant;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.enums.NftOrderStatusEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftPublicBackpackMapper;
import com.seeds.game.mq.producer.KafkaProducer;
import com.seeds.game.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * NFT公共背包 服务实现类
 * </p>
 *
 * @author hewei
 * @since 2023-01-31
 */
@Service
@Slf4j
public class NftPublicBackpackServiceImpl extends ServiceImpl<NftPublicBackpackMapper, NftPublicBackpackEntity> implements INftPublicBackpackService {

    @Autowired
    private IServerRoleService serverRoleService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private RemoteGameService remoteGameService;

    @Autowired
    private IServerRegionService serverRegionService;

    @Autowired
    private CallGameApiLogService callGameApiLogService;

    @Autowired
    private INftEventService nftEventService;

    @Autowired
    private INftEquipmentService nftEquipmentService;

    @Autowired
    private INftEventEquipmentService nftEventEquipmentService;

    @Autowired
    private INftMarketOrderService nftMarketOrderService;

    @Autowired
    private SeedsApiConfig seedsApiConfig;

    @Autowired
    private UcUserService ucUserService;

    @Override
    public IPage<NftPublicBackpackResp> queryPage(NftPublicBackpackPageReq req) {

        LambdaQueryWrapper<NftPublicBackpackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(req.getIsConfiguration()), NftPublicBackpackEntity::getIsConfiguration, req.getIsConfiguration())
                .like(!Objects.isNull(req.getName()), NftPublicBackpackEntity::getName, req.getName())
                .ne(NftPublicBackpackEntity::getState, NFTEnumConstant.NFTStateEnum.LOCK.getCode())
                .eq(NftPublicBackpackEntity::getUserId, req.getUserId());

        Page<NftPublicBackpackEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<NftPublicBackpackEntity> records = this.page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            NftPublicBackpackResp resp = new NftPublicBackpackResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void create(NftPublicBackpackReq req) {
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setCreatedAt(System.currentTimeMillis());
        entity.setCreatedBy(req.getUserId());
        entity.setUpdatedAt(System.currentTimeMillis());
        entity.setUpdatedBy(req.getUserId());
        this.save(entity);
    }

    @Override
    public void update(NftPublicBackpackReq req) {
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setUpdatedAt(System.currentTimeMillis());
        entity.setUpdatedBy(req.getUserId());
        this.update(entity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));
    }

    @Override
    public NftPublicBackpackResp detail(Integer autoId) {
        NftPublicBackpackResp nftPublicBackpackResp = new NftPublicBackpackResp();
        NftPublicBackpackEntity entity = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, autoId));
        if (null != entity) {
            BeanUtils.copyProperties(entity, nftPublicBackpackResp);
        }
        return nftPublicBackpackResp;
    }

    @Override
    public OpenNftPublicBackpackDisResp distributeBatch(List<NftPublicBackpackDisReq> reqs) {
        for (NftPublicBackpackDisReq req : reqs) {
            this.distribute(req);
        }
        return null;
    }

    @Override
    public void takeBackBatch(List<NftPublicBackpackTakeBackReq> reqs) {
        for (NftPublicBackpackTakeBackReq req : reqs) {
            this.takeBack(req);
        }
    }

    @Override
    public OpenNftPublicBackpackDisResp transferBatch(List<NftPublicBackpackDisReq> reqs) {
        for (NftPublicBackpackDisReq req : reqs) {
            this.transfer(req);
        }
        return null;
    }

    @Override
    @Transactional
    public OpenNftPublicBackpackDisResp distribute(NftPublicBackpackDisReq req) {
        NftPublicBackpackEntity nftItem = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }

        // 校验当前NFT物品是否 托管
        if (nftItem.getIsConfiguration().equals(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10015_NFT_ITEM_HAS_NOT_BEEN_DEPOSITED);
        }

        // 1.校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 2.校验当前NFT物品是否是未分配转态
        if (!nftItem.getIsConfiguration().equals(NftConfigurationEnum.UNASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10003_NFT_ITEM_HAVE_BEEN_ASSIGNED);
        }
        // 3.校验分配的角色等级是否满10级
        ServerRoleEntity roleEntity = serverRoleService.getById(req.getServerRoleId());
        if (Objects.isNull(roleEntity)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20002_ROLE_NOT_EXIST);
        }
        if (roleEntity.getLevel() < 10) {
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_LEVE_IS_LESS_THAN_TEN);
        }

        // 调用游戏方接口，执行分配
        this.callGameDistribute(nftItem, req.getServerRoleId());

        // 更新公共背包数据
        nftItem.setServerRoleId(req.getServerRoleId());
        nftItem.setIsConfiguration(NftConfigurationEnum.ASSIGNED.getCode());
        nftItem.setUpdatedAt(System.currentTimeMillis());
        this.updateById(nftItem);
        ServerRegionEntity serverRegion = this.getServerRegionEntity(req.getServerRoleId());

        // 记录分配事件
        recoredNftEvent(nftItem, userId, NFTEnumConstant.NFTTransEnum.BACKPACK.getDesc(), serverRegion.getGameServerName());


        // 组装返回值
        OpenNftPublicBackpackDisResp resp = new OpenNftPublicBackpackDisResp();
        resp.setGameServer(roleEntity.getGameServer());
        resp.setGameServerName(serverRegion.getGameServerName());
        resp.setItemName(nftItem.getName());
        resp.setRegion(roleEntity.getRegion());
        resp.setRegionName(serverRegion.getRegionName());
        resp.setRoleName(roleEntity.getName());
        resp.setLevel(roleEntity.getLevel());

        return resp;
    }

    private void checkState(NftPublicBackpackEntity nftItem) {

        if (nftItem.getIsConfiguration().equals(NftConfigurationEnum.PROCESSING.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10006_NFT_PROCESSING);
        }
        updateConfig(nftItem, NftConfigurationEnum.PROCESSING.getCode());
    }


    @Override
    public void takeBack(NftPublicBackpackTakeBackReq req) {

        NftPublicBackpackEntity nftItem = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }

        // 校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 校验当前NFT物品是否是分配转态
        if (!nftItem.getIsConfiguration().equals(NftConfigurationEnum.ASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10004_NFT_ITEM_NOT_ASSIGNED);
        }

//        // 调用游戏方接口，执行收回
        this.callGameTakeback(nftItem);

        ServerRegionEntity serverRegion = this.getServerRegionEntity(nftItem.getServerRoleId());
        // 记录转移事件
        recoredNftEvent(nftItem, userId, serverRegion.getGameServerName(), NFTEnumConstant.NFTTransEnum.BACKPACK.getDesc());


        // 更新公共背包数据
        nftItem.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        nftItem.setServerRoleId(0L);
        nftItem.setUpdatedAt(System.currentTimeMillis());
        this.updateById(nftItem);
    }


    @Override
    public OpenNftPublicBackpackDisResp transfer(NftPublicBackpackDisReq req) {
        NftPublicBackpackEntity nftItem = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // 当前NFT已经属于想要转移的角色，不需要再转移
        if (nftItem.getServerRoleId().equals(req.getServerRoleId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10005_NFT_ITEM_ALREADY_BELONGS_TO_THE_ROLE);
        }
        // 校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 校验当前NFT物品是否是分配转态
        if (!nftItem.getIsConfiguration().equals(NftConfigurationEnum.ASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10004_NFT_ITEM_NOT_ASSIGNED);
        }
        // 校验分配的角色等级是否满10级
        ServerRoleEntity roleEntity = serverRoleService.getById(req.getServerRoleId());
        if (Objects.isNull(roleEntity)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20002_ROLE_NOT_EXIST);
        }
        if (roleEntity.getLevel() < 10) {
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_LEVE_IS_LESS_THAN_TEN);
        }
        // 不属于当前用户下的角色
        if (!roleEntity.getUserId().equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_20003_ROLE_NOT_BELONGS_TO_CURRENT_USER);
        }
        // 调用游戏方接口，执行收回,再分发
        // 执行收回
        this.callGameTakeback(nftItem);
        // 分发
        this.callGameDistribute(nftItem, req.getServerRoleId());

        String from = this.getServerRegionEntity(nftItem.getServerRoleId()).getGameServerName();

        // 更新公共背包数据
        nftItem.setServerRoleId(req.getServerRoleId());
        nftItem.setIsConfiguration(NftConfigurationEnum.ASSIGNED.getCode());
        nftItem.setUpdatedAt(System.currentTimeMillis());
        this.updateById(nftItem);


        ServerRegionEntity serverRegion = this.getServerRegionEntity(req.getServerRoleId());
        // 记录转移事件
        recoredNftEvent(nftItem, userId, from, serverRegion.getGameServerName());

        // 组装返回值
        OpenNftPublicBackpackDisResp resp = new OpenNftPublicBackpackDisResp();
        resp.setGameServer(roleEntity.getGameServer());
        resp.setGameServerName(serverRegion.getGameServerName());
        resp.setItemName(nftItem.getName());
        resp.setRegion(roleEntity.getRegion());
        resp.setRegionName(serverRegion.getRegionName());
        resp.setRoleName(roleEntity.getName());
        resp.setLevel(roleEntity.getLevel());
        return resp;
    }

    private void recoredNftEvent(NftPublicBackpackEntity nftItem, Long userId, String from, String to) {
        //插入nft_event表
        NftEvent nftEvent = new NftEvent();
        nftEvent.setType(NFTEnumConstant.NFTEventType.OTHER.getCode());
        nftEvent.setName(nftItem.getName());
        nftEvent.setTransferFrom(from);
        nftEvent.setTransferTo(to);
        nftEvent.setUserId(userId);
        nftEvent.setUpdatedBy(userId);
        nftEvent.setCreatedBy(userId);
        nftEvent.setUpdatedAt(System.currentTimeMillis());
        nftEvent.setCreatedAt(System.currentTimeMillis());
        nftEventService.save(nftEvent);
        //插入nft_event_equipment表
        NftEventEquipment one = nftEventEquipmentService.getOne(new LambdaQueryWrapper<NftEventEquipment>()
                .eq(NftEventEquipment::getAutoId, nftItem.getAutoId()).eq(NftEventEquipment::getIsConsume, WhetherEnum.NO.value()));
        if (Objects.isNull(one)) {
            NftEventEquipment equipment = new NftEventEquipment();
            equipment.setEventId(nftEvent.getId());
            equipment.setImageUrl(nftItem.getImage());
            equipment.setAutoId(nftItem.getAutoId());
            equipment.setAttributes(nftItem.getAttributes());
            nftEventEquipmentService.save(equipment);
        }
    }

    @Override
    public List<NftPublicBackpackResp> queryList(NftPublicBackpackPageReq req) {
        List<NftPublicBackpackResp> respList = new ArrayList<>();

        LambdaQueryWrapper<NftPublicBackpackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(req.getIsConfiguration()), NftPublicBackpackEntity::getIsConfiguration, req.getIsConfiguration())
                .like(!Objects.isNull(req.getName()), NftPublicBackpackEntity::getName, req.getName())
                .eq(NftPublicBackpackEntity::getState, NFTEnumConstant.NFTStateEnum.DEPOSITED.getCode())
                .eq(NftPublicBackpackEntity::getUserId, req.getUserId());
        List<NftPublicBackpackEntity> list = this.list(wrapper);

        if (!CollectionUtils.isEmpty(list)) {
            respList = list.stream().map(p -> {
                NftPublicBackpackResp resp = new NftPublicBackpackResp();
                BeanUtils.copyProperties(p, resp);
                return resp;
            }).collect(Collectors.toList());
        }
        return respList;
    }

    @Override
    public NftPublicBackpackEntity queryByMintAddress(String mintAddress) {
        return getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>()
                .eq(NftPublicBackpackEntity::getTokenId, mintAddress));
    }

    @Override
    public NftPublicBackpackEntity queryByTokenId(String tokenId) {
        return getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>()
                .eq(NftPublicBackpackEntity::getTokenId, tokenId));
    }

    // lootmode 结算 nft物品所有权转移，中心化操作
    @Override
    public void ownerTransfer(OpenNftOwnershipTransferReq req) {
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setUserId(req.getToUserId());
        this.update(entity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));
    }

    @Override
    public NftPublicBackpackEntity queryByEqNftId(Long eqNftId) {
        return getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, eqNftId));
    }

    @Override
    public void deposited(NftDepositedReq req) {
        NftEquipment nft = nftEquipmentService.getOne(new LambdaQueryWrapper<NftEquipment>().eq(NftEquipment::getMintAddress, req.getMintAddress()));
        if (nft == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // NFT已经处于托管中
        if (WhetherEnum.YES.value() == nft.getIsDeposit()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10008_NFT_ITEM_IS_DEPOSIT);
        }
        // NFT上架中不能托管
        if (WhetherEnum.YES.value() == nft.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10007_NFT_ITEM_IS_ALREADY_ON_SALE);
        }
        //更新背包状态 deposited
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(NFTEnumConstant.NFTStateEnum.DEPOSITED.getCode());
        this.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getEqNftId, nft.getId()));
        // NFT非归属人不能托管
        ucUserService.ownerValidation(nft.getOwner());
        // 调用/api/equipment/depositNft通知，托管NFT成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getDepositNft();
        DepositSuccessMessageDto dto  = new DepositSuccessMessageDto();
        BeanUtils.copyProperties(req, dto);
        dto.setMintAddress(nft.getMintAddress());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT托管成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpRequest.post(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            log.error("NFT托管成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public void unDeposited(NftUnDepositedReq req) {

        // NFT锁定中不能取回
        //  NftPublicBackpackEntity backpackNft = queryByEqNftId(req.getNftId());
        NftPublicBackpackEntity backpackNft = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getTokenAddress, req.getMintAddress()));
        if (backpackNft == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        NftEquipment nft = nftEquipmentService.getOne(new LambdaQueryWrapper<NftEquipment>().eq(NftEquipment::getMintAddress, req.getMintAddress()));
        if (nft == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }
        // NFT已经取回
        if (WhetherEnum.NO.value() == nft.getIsDeposit()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10016_NFT_ITEM_HAS_BEEN_RETRIEVED);
        }
        // 更改背包状态，通知游戏方NFT收回到背包。
        // 调用游戏方接口，执行收回
        this.callGameTakeback(backpackNft);
        // 更新背包状态
        backpackNft.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        backpackNft.setServerRoleId(0L);
        backpackNft.setUpdatedAt(System.currentTimeMillis());
        this.updateById(backpackNft);
        // NFT非归属人不能取回
        ucUserService.ownerValidation(nft.getOwner());
        // 调用/api/equipment/withdrawNft通知，取回NFT成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getWithdrawNft();
        TransferNftMessageDto dto = new TransferNftMessageDto();
        BeanUtils.copyProperties(req, dto);
        dto.setMintAddress(nft.getMintAddress());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT取回成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpRequest.post(url)
                    .timeout(5 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
        } catch (Exception e) {
            log.error("NFT取回成功通知失败，message：{}", e.getMessage());
        }
    }

    @Override
    public List<NftTypeNum> typeNum() {
        Long userId = UserContext.getCurrentUserId();
        return baseMapper.selectTypeNum(userId);

    }

    @Override
    public List<NftType> getNftTypeList(Integer type) {
        Long userId = UserContext.getCurrentUserId();
        return baseMapper.getNftTypeList(userId,type);
    }

    @Override
    public List<NftPublicBackpackWebResp> getPageForWeb(NftBackpackWebPageReq req) {
        List<NftPublicBackpackWebResp> list = baseMapper.getPageForWeb(req);
        list = list.stream().map(p -> {
            NftPublicBackpackWebResp resp = new NftPublicBackpackWebResp();
            BeanUtils.copyProperties(p, resp);
            ServerRegionEntity serverRegionEntity = this.getServerRegionEntity(p.getServerRoleId());
            resp.setServerName(serverRegionEntity.getGameServerName());
            return resp;
        }).collect(Collectors.toList());
        return list;
    }



    private void callGameDistribute(NftPublicBackpackEntity nftItem, Long serverRoleId) {

        ServerRegionEntity serverRegion = this.getServerRegionEntity(serverRoleId);
        // 校验是否在操作中，操作中直接返回，减少不必要的请求
        this.checkState(nftItem);

        GenericDto<String> dto = null;
        try {
            dto = remoteGameService.queryGameApi(1L, ApiType.NFT_PACKAGE_DISTRIBUTE.getCode());
        } catch (Exception e) {
            log.info("rpc all seeds-admin ,queryGameApi error {}", e.getMessage());
        }

        String distributeUrl = "http://" + serverRegion.getInnerHost() + dto.getData();

        NftDistributeReq distributeReq = new NftDistributeReq();
        distributeReq.setAutoId(nftItem.getAutoId());
        distributeReq.setAccId(serverRoleId);
        distributeReq.setType(nftItem.getType());
        if (nftItem.getType().equals(NFTEnumConstant.NftTypeEnum.HERO.getCode())) {
            distributeReq.setImgUrl(nftItem.getImage());
        }
        distributeReq.setConfigId(nftItem.getItemId().intValue());
        distributeReq.setTokenAddress(nftItem.getTokenAddress());
        distributeReq.setServerName(serverRegion.getGameServerName());
        distributeReq.setRegionName(serverRegion.getRegionName());

        String params = JSONUtil.toJsonStr(distributeReq);

        log.info("开始请求游戏 nft 分发接口， url:{}， params:{}", distributeUrl, params);
        HttpResponse response = null;
        try {
            response = HttpRequest.post(distributeUrl)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .body(params)
                    .execute();
        } catch (Exception e) {
            updateConfig(nftItem, NftConfigurationEnum.UNASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(distributeUrl, params, "connect timed out");
            throw new GenericException("Failed to call game-api to distribute nft,connect timed out");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String ret = jsonObject.getString("ret");
        log.info("请求游戏 nft 分发接口返回，  result:{}", response.body());
        if (!response.isOk() || !"ok".equalsIgnoreCase(ret)) {
            updateConfig(nftItem, NftConfigurationEnum.UNASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(distributeUrl, params, ret);
            throw new GenericException("Failed to call game-api to distribute nft, ret" + ret);
        }


    }

    private void updateConfig(NftPublicBackpackEntity nftItem, Integer isConfig) {
        NftPublicBackpackEntity state = new NftPublicBackpackEntity();
        state.setId(nftItem.getId());
        state.setIsConfiguration(isConfig);
        this.updateById(state);
    }

    private void callGameTakeback(NftPublicBackpackEntity nftItem) {

        ServerRegionEntity serverRegion = this.getServerRegionEntity(nftItem.getServerRoleId());

        // 校验是否在操作中，操作中直接返回，减少不必要的请求
        this.checkState(nftItem);

        GenericDto<String> dto = null;
        try {
            dto = remoteGameService.queryGameApi(1L, ApiType.NFT_PACKAGE_TAKEBACK.getCode());
        } catch (Exception e) {
            log.info("rpc all seeds-admin ,queryGameApi error {}", e.getMessage());
        }
        String takebackUrl = "http://" + serverRegion.getInnerHost() + dto.getData();
        NftTakebackReq takeback = new NftTakebackReq();
        takeback.setType(nftItem.getType());
        takeback.setAutoId(nftItem.getAutoId());
        takeback.setAccId(nftItem.getServerRoleId());
        takeback.setConfigId(nftItem.getItemId().intValue());
        takeback.setTokenAddress(nftItem.getTokenAddress());

        String params = JSONUtil.toJsonStr(takeback);

        log.info("开始请求游戏 nft 收回接口， url:{}， params:{}", takebackUrl, params);
        HttpResponse response = null;
        try {
            response = HttpRequest.post(takebackUrl)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .body(params)
                    .execute();
        } catch (Exception e) {
            updateConfig(nftItem, NftConfigurationEnum.ASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(takebackUrl, params, "connect timed out");
            throw new GenericException("Failed to call game-api to takeback nft,connect timed out");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String ret = jsonObject.getString("ret");
        log.info("请求游戏 nft 收回接口返回，  result:{}", response.body());
        if (!"ok".equalsIgnoreCase(ret)) {
            updateConfig(nftItem, NftConfigurationEnum.ASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(takebackUrl, params, ret);
            throw new GenericException("Failed to call game-api to takeback nft, ret" + ret);
        }

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

}
