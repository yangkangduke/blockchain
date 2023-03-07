package com.seeds.game.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.game.NftDistributeReq;
import com.seeds.admin.dto.request.game.NftTakebackReq;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.game.dto.request.OpenNftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackPageReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.dto.response.NftPublicBackpackResp;
import com.seeds.game.dto.response.OpenNftPublicBackpackDisResp;
import com.seeds.game.entity.CallGameApiErrorLogEntity;
import com.seeds.game.entity.NftPublicBackpackEntity;
import com.seeds.game.entity.ServerRegionEntity;
import com.seeds.game.entity.ServerRoleEntity;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.enums.NftConfigurationEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftPublicBackpackMapper;
import com.seeds.game.mq.producer.KafkaProducer;
import com.seeds.game.service.CallGameApiLogService;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.game.service.IServerRegionService;
import com.seeds.game.service.IServerRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
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

    @Override
    public IPage<NftPublicBackpackResp> queryPage(NftPublicBackpackPageReq req) {

        LambdaQueryWrapper<NftPublicBackpackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(req.getIsConfiguration()), NftPublicBackpackEntity::getIsConfiguration, req.getIsConfiguration())
                .like(!Objects.isNull(req.getName()), NftPublicBackpackEntity::getName, req.getName())
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
        this.updateById(entity);
    }

    @Override
    public NftPublicBackpackResp detail(Long id) {
        NftPublicBackpackResp nftPublicBackpackResp = new NftPublicBackpackResp();
        NftPublicBackpackEntity entity = this.getById(id);
        if (null != entity) {
            BeanUtils.copyProperties(entity, nftPublicBackpackResp);
        }
        return nftPublicBackpackResp;
    }

    @Override
    public OpenNftPublicBackpackDisResp distribute(NftPublicBackpackDisReq req) {
        NftPublicBackpackEntity nftItem = this.getById(req.getId());

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }

        // 1.校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }
        // 2.校验当前NFT物品是否是未分配转态
        if (nftItem.getIsConfiguration().equals(NftConfigurationEnum.ASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10003_NFT_ITEM_HAVE_BEEN_ASSIGNED);
        }
        // 3.校验分配的角色等级是否满10级
        ServerRoleEntity roleEntity = serverRoleService.getById(req.getServerRoleId());
        if (Objects.isNull(roleEntity)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_NOT_EXIST);
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

        // 组装返回值
        ServerRegionEntity serverRegion = this.getServerRegionEntity(req.getServerRoleId());
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

        NftPublicBackpackEntity nftItem = this.getById(req.getId());

        if (Objects.isNull(nftItem)) {
            throw new GenericException(GameErrorCodeEnum.ERR_10001_NFT_ITEM_NOT_EXIST);
        }

        // 校验当前NFT物品是否属于当前用户
        Long userId = nftItem.getUserId();
        if (!userId.equals(req.getUserId())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10002_NFT_ITEM_DOES_NOT_BELONG_TO_CURRENT_USER);
        }

        // 调用游戏方接口，执行收回
        this.callGameTakeback(nftItem);

        // 更新公共背包数据
        nftItem.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        nftItem.setServerRoleId(0L);
        nftItem.setUpdatedAt(System.currentTimeMillis());
        this.updateById(nftItem);
    }


    @Override
    public OpenNftPublicBackpackDisResp transfer(NftPublicBackpackDisReq req) {
        NftPublicBackpackEntity nftItem = this.getById(req.getId());

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
        if (nftItem.getIsConfiguration().equals(NftConfigurationEnum.UNASSIGNED.getCode())) {
            throw new GenericException(GameErrorCodeEnum.ERR_10004_NFT_ITEM_CANNOT_TRANSFER);
        }
        // 校验分配的角色等级是否满10级
        ServerRoleEntity roleEntity = serverRoleService.getById(req.getServerRoleId());
        if (Objects.isNull(roleEntity)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_NOT_EXIST);
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


        // 更新公共背包数据
        nftItem.setServerRoleId(req.getServerRoleId());
        nftItem.setIsConfiguration(NftConfigurationEnum.ASSIGNED.getCode());
        nftItem.setUpdatedAt(System.currentTimeMillis());
        this.updateById(nftItem);

        // 组装返回值
        ServerRegionEntity serverRegion = this.getServerRegionEntity(req.getServerRoleId());
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

    @Override
    public List<NftPublicBackpackResp> queryList(OpenNftPublicBackpackPageReq req) {
        List<NftPublicBackpackResp> respList = new ArrayList<>();

        LambdaQueryWrapper<NftPublicBackpackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(req.getIsConfiguration()), NftPublicBackpackEntity::getIsConfiguration, req.getIsConfiguration())
                .like(!Objects.isNull(req.getName()), NftPublicBackpackEntity::getName, req.getName())
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
        distributeReq.setConfigId(nftItem.getItemId().intValue());
        distributeReq.setTokenId(nftItem.getTokenId());
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

            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String ret = jsonObject.getString("ret");
            log.info("请求游戏 nft 分发接口返回，  result:{}", response.body());
            if (!response.isOk() || !"ok".equalsIgnoreCase(ret)) {
                updateConfig(nftItem, NftConfigurationEnum.UNASSIGNED.getCode());
                // 记录调用错误日志
                errorLog(distributeUrl, params, ret);
            }
        } catch (Exception e) {
            updateConfig(nftItem, NftConfigurationEnum.UNASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(distributeUrl, params, e.getMessage());
            throw new com.seeds.uc.exceptions.GenericException("Failed to call game-api to distribute nft");
        }

    }

    private void updateConfig(NftPublicBackpackEntity nftItem, Integer isConfig) {
        NftPublicBackpackEntity state = new NftPublicBackpackEntity();
        state.setId(nftItem.getId());
        state.setIsConfiguration(isConfig);
        this.updateById(state);
    }

    private void callGameTakeback(NftPublicBackpackEntity nftItem) {


        // 校验是否在操作中，操作中直接返回，减少不必要的请求
        this.checkState(nftItem);

        ServerRegionEntity serverRegion = this.getServerRegionEntity(nftItem.getServerRoleId());

        GenericDto<String> dto = null;
        try {
            dto = remoteGameService.queryGameApi(1L, ApiType.NFT_PACKAGE_DISTRIBUTE.getCode());
        } catch (Exception e) {
            log.info("rpc all seeds-admin ,queryGameApi error {}", e.getMessage());
        }
        String takebackUrl = "http://" + serverRegion.getInnerHost() + dto.getData();
        NftTakebackReq takeback = new NftTakebackReq();
        takeback.setType(nftItem.getType());
        takeback.setAutoId(nftItem.getAutoId());
        takeback.setAccId(nftItem.getServerRoleId());
        takeback.setConfigId(nftItem.getItemId().intValue());
        takeback.setTokenId(nftItem.getTokenId());

        String params = JSONUtil.toJsonStr(takeback);

        log.info("开始请求游戏 nft 收回接口， url:{}， params:{}", takebackUrl, params);
        HttpResponse response = null;
        try {
            response = HttpRequest.post(takebackUrl)
                    .timeout(10 * 1000)
                    .header("Content-Type", "application/json")
                    .body(params)
                    .execute();

            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String ret = jsonObject.getString("ret");
            log.info("请求游戏 nft 收回接口返回，  result:{}", response.body());
            if (!"ok".equalsIgnoreCase(ret)) {
                updateConfig(nftItem, NftConfigurationEnum.ASSIGNED.getCode());
                // 记录调用错误日志
                errorLog(takebackUrl, params, ret);
            }
        } catch (Exception e) {
            updateConfig(nftItem, NftConfigurationEnum.ASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(takebackUrl, params, e.getMessage());
            throw new com.seeds.uc.exceptions.GenericException("Failed to call game-api to distribute nft");
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
            throw new GenericException(GameErrorCodeEnum.ERR_20001_ROLE_NOT_EXIST);
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
