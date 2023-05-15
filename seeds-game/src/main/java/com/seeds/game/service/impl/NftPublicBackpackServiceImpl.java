package com.seeds.game.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.entity.SysGameApiEntity;
import com.seeds.admin.enums.SkinNftEnums;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteGameService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.common.enums.CurrencyEnum;
import com.seeds.common.web.context.UserContext;
import com.seeds.game.config.SeedsApiConfig;
import com.seeds.game.dto.request.*;
import com.seeds.game.dto.request.external.DepositSuccessMessageDto;
import com.seeds.game.dto.request.external.NftDepositCheckDto;
import com.seeds.game.dto.request.external.TransferNftMessageDto;
import com.seeds.game.dto.request.internal.*;
import com.seeds.game.dto.response.*;
import com.seeds.game.entity.*;
import com.seeds.game.enums.*;
import com.seeds.game.exception.GenericException;
import com.seeds.game.mapper.NftPublicBackpackMapper;
import com.seeds.game.mq.producer.KafkaProducer;
import com.seeds.game.service.*;
import com.seeds.uc.dto.response.UcUserResp;
import com.seeds.uc.feign.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.seeds.game.enums.GameErrorCodeEnum.ERR_50001_CALL_GAME_INTERFACE_ERROR;

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
    private NftMarketPlaceService nftMarketPlaceService;

    @Autowired
    private SeedsApiConfig seedsApiConfig;

    @Autowired
    private INftAttributeService attributeService;

    @Autowired
    private UcUserService ucUserService;

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private GameFileService gameFileService;

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
        // 更新nft属性表
        if (Objects.nonNull(req.getAttributes())) {
            updateAttr(req);
        }
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
    public Integer distributeBatch(List<NftPublicBackpackDisReq> reqs) {
        Integer num = reqs.size();
        for (NftPublicBackpackDisReq req : reqs) {
            try {
                this.distribute(req);
            } catch (Exception e) {
                num -= 1;
                log.info("nft 分配失败 autoId:{},error:{}", req.getAutoId(), e.getMessage());
            }
        }
        return num;
    }

    @Override
    public Integer takeBackBatch(List<NftPublicBackpackTakeBackReq> reqs) {
        Integer num = reqs.size();
        for (NftPublicBackpackTakeBackReq req : reqs) {
            try {
                this.takeBack(req);
            } catch (Exception e) {
                num -= 1;
                log.info("nft 收回失败 autoId:{},error:{}", req.getAutoId(), e.getMessage());
            }
        }
        return num;
    }

    @Override
    public Integer transferBatch(List<NftPublicBackpackDisReq> reqs) {
        Integer num = reqs.size();
        for (NftPublicBackpackDisReq req : reqs) {
            try {
                this.transfer(req);
            } catch (Exception e) {
                num -= 1;
                log.info("nft 转移失败 autoId:{},error:{}", req.getAutoId(), e.getMessage());
            }
        }
        return num;
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
     //   this.callGameDistribute(nftItem, req.getServerRoleId());

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
      //  this.callGameTakeback(nftItem);
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
//        // 调用游戏方接口，执行收回,再分发
//        // 执行收回
//        this.callGameTakeback(nftItem);
//        // 分发
//        this.callGameDistribute(nftItem, req.getServerRoleId());

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
        NftEventEquipment equipment = new NftEventEquipment();
        equipment.setEventId(nftEvent.getId());
        equipment.setImageUrl(nftItem.getImage());
        equipment.setAutoId(nftItem.getAutoId());
        equipment.setAttributes(nftItem.getAttributes());
        nftEventEquipmentService.save(equipment);
    }

    @Override
    public List<NftPublicBackpackResp> queryList(NftPublicBackpackPageReq req) {
        List<NftPublicBackpackResp> respList = new ArrayList<>();
        BigDecimal usdRate = nftMarketPlaceService.usdRate(CurrencyEnum.SOL.getCode());
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
                resp.setPrice((p.getProposedPrice().divide(usdRate, 2, BigDecimal.ROUND_HALF_UP).toString()));
                return resp;
            }).filter(i -> {
                if (!i.getType().equals(NFTEnumConstant.NftTypeEnum.HERO.getCode())) {
                    String attributes = i.getAttributes();
                    Integer durability = 0;
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(attributes);
                        durability = Integer.parseInt(jsonObject.getString("durability"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    return durability > 0;
                }
                return true;
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

    @Override
    public Map<Long, String> queryTokenAddressByAutoIds(List<Long> autoIds) {

        return list(new LambdaQueryWrapper<NftPublicBackpackEntity>()
                .in(NftPublicBackpackEntity::getAutoId, autoIds)).stream().collect(Collectors.toMap(NftPublicBackpackEntity::getAutoId, NftPublicBackpackEntity::getTokenAddress));
    }

    // lootmode 结算 nft物品所有权转移，中心化操作
    @Override
    public void ownerTransfer(OpenNftOwnershipTransferReq req) {
        NftPublicBackpackEntity entity = new NftPublicBackpackEntity();
        BeanUtils.copyProperties(req, entity);
        ServerRoleEntity serverRole = serverRoleService.getById(req.getServerRoleId());
        if (Objects.isNull(serverRole)) {
            throw new GenericException(GameErrorCodeEnum.ERR_20002_ROLE_NOT_EXIST);
        }
        entity.setUserId(serverRole.getUserId());
        try {
            GenericDto<String> result = userCenterFeignClient.getPublicAddress(serverRole.getUserId());
            String owner = result.getData();
            entity.setOwner(owner);
        } catch (Exception e) {
            log.error("nft物品所有权转移，内部请求uc获取用户公共地址失败，userId:{},error:{}", serverRole.getUserId(), e.getMessage());
        }
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
        if (NftOrderTypeEnum.BUY_NOW.getCode() == nft.getOnSale() || NftOrderTypeEnum.ON_AUCTION.getCode() == nft.getOnSale()) {
            throw new GenericException(GameErrorCodeEnum.ERR_10007_NFT_ITEM_IS_ON_SALE);
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
            HttpResponse response = HttpRequest.post(url)
                    .timeout(30 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("NFT托管成功通知返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT托管成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT Hosting Failure!");
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
//        if (backpackNft.getServerRoleId().compareTo(new Long(NFTEnumConstant.NFTTransEnum.BACKPACK.getCode())) != 0){
//            this.callGameTakeback(backpackNft);
//        }
        // 更新背包状态
        int durability = 0;
        try {
            JSONObject jsonObject = JSONObject.parseObject(backpackNft.getAttributes());
            durability = (int) jsonObject.get("durability");
            // 设置参考价
            BigDecimal usdRate = nftMarketPlaceService.usdRate(CurrencyEnum.SOL.getCode());
            backpackNft.setProposedPrice(new BigDecimal(durability).divide(usdRate,2,BigDecimal.ROUND_HALF_UP));
        } catch (Exception e) {
            e.printStackTrace();
        }
        backpackNft.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        backpackNft.setServerRoleId(0L);
        backpackNft.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
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
            HttpResponse response = HttpRequest.post(url)
                    .timeout(30 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("NFT取回成功通知返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT取回成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT retrieval failure");
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
        if (Objects.nonNull(req.getSortType())) {
            req.setSortTypeStr(NftBackpackWebPageReq.convert(req.getSortType()));
        }
        List<NftPublicBackpackWebResp> list = baseMapper.getPageForWeb(req);
        list = list.stream().map(p -> {
            NftPublicBackpackWebResp resp = new NftPublicBackpackWebResp();
            BeanUtils.copyProperties(p, resp);
            if (p.getServerRoleId().compareTo(new Long(NFTEnumConstant.NFTTransEnum.BACKPACK.getCode())) == 0) {
                resp.setServerName(NFTEnumConstant.NFTTransEnum.BACKPACK.getDesc());
            }
            ServerRegionEntity serverRegionEntity = serverRegionService.queryByServerRoleId(p.getServerRoleId());
            if (Objects.nonNull(serverRegionEntity)) {
                resp.setServerName(serverRegionEntity.getGameServerName());
            }
            return resp;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public void updateState(NftBackpakcUpdateStateReq req) {

        log.info("扫快通知,更新背包状态---->param：{}", JSONUtil.toJsonStr(req));
        // 如果是withdraw，需要通知游戏方把nft收回到背包
        if (req.getState().equals(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode())) {
            NftPublicBackpackEntity backpackNft = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getTokenAddress, req.getMintAddress()));
            if(Objects.nonNull(backpackNft) && backpackNft.getServerRoleId() != 0){
                // 调用游戏方接口，执行收回
                this.callGameTakeback(backpackNft);
                // 更新背包状态
                backpackNft.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
                backpackNft.setServerRoleId(0L);
                backpackNft.setUpdatedAt(System.currentTimeMillis());
                this.updateById(backpackNft);
            }
        }
        NftPublicBackpackEntity backpackEntity = new NftPublicBackpackEntity();
        backpackEntity.setState(req.getState());
        if (StringUtils.isNotBlank(req.getOwner())) {
            try {
                GenericDto<UcUserResp> result = userCenterFeignClient.getByPublicAddress(req.getOwner());
                backpackEntity.setUserId(result.getData().getId());
            } catch (Exception e) {
                log.error("内部请求uc获取用户信息失败");
            }
            backpackEntity.setOwner(req.getOwner());
        }
        this.update(backpackEntity, new LambdaUpdateWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getTokenAddress, req.getMintAddress()));

    }

    @Override
    public void insertCallback(MintSuccessReq req) {
        nftEventService.mintSuccessCallback(req);
    }

    @Override
    public Map<Long, BigDecimal> getTotalPrice(String autoIds) {

        List<NftPublicBackpackEntity> list = this.list(new LambdaQueryWrapper<NftPublicBackpackEntity>().in(NftPublicBackpackEntity::getAutoId, autoIds.split(",")));
        return list.stream().collect(Collectors.toMap(NftPublicBackpackEntity::getAutoId, NftPublicBackpackEntity::getProposedPrice));
    }

    @Override
    public GenericDto<Object> depositCheck(NftDepositCheckReq req) {
        NftPublicBackpackEntity one = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));
        if (Objects.nonNull(one)) {
            NftAttributeEntity attribute = attributeService.queryByNftId(one.getEqNftId());
            NftDepositCheckDto checkDto = new NftDepositCheckDto();
            checkDto.setAutoId(one.getAutoId());
            checkDto.setConfId(one.getItemId().intValue());
            checkDto.setTokenAddress(one.getTokenAddress());
            if (one.getType().equals(NFTEnumConstant.NftTypeEnum.HERO)) {
                checkDto.setWin(attribute.getVictory());
                checkDto.setDefeat(attribute.getLose());
                checkDto.setSeqWin(attribute.getMaxStreak());
                checkDto.setSeqDefeat(attribute.getMaxLose());
            } else {
                checkDto.setDurability(attribute.getDurability());
                checkDto.setRarityAttr(attribute.getRarityAttr());
            }
            try {
                GenericDto<SysGameApiEntity> dto = remoteGameService.queryByGameAndType(1L, ApiType.DEPOSIT_CHECK.getCode());
                String depositCheckUrl = dto.getData().getBaseUrl() + dto.getData().getApi();
                String params = JSONUtil.toJsonStr(checkDto);
                log.info("开始请求游戏  deposit-check 接口， url:{}， params:{}", depositCheckUrl, params);
                HttpResponse response = HttpRequest.post(depositCheckUrl)
                        .timeout(5 * 1000)
                        .header("Content-Type", "application/json")
                        .body(params)
                        .execute();
                JSONObject jsonObject = JSONObject.parseObject(response.body());
                log.info("请求游戏 deposit-check 接口返回，  result:{}", response.body());
                int ret = (int) jsonObject.get("ret");
                if (ret == 0)
                    return GenericDto.success(true);
                else {
                    return GenericDto.failure(ret, NFTEnumConstant.NftDepositCheckEnum.getMessage(ret));
                }
            } catch (Exception e) {
                log.info("rpc all seeds-admin ,queryGameApi error {}", e.getMessage());
            }
        }
        return GenericDto.failure(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY.getCode(), "invalid NFT");
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
            throw new GenericException(ERR_50001_CALL_GAME_INTERFACE_ERROR, "Failed to call game-api to distribute nft,connect timed out");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String ret = jsonObject.getString("ret");
        log.info("请求游戏 nft 分发接口返回，  result:{}", response.body());
        if (!response.isOk() || !"ok".equalsIgnoreCase(ret)) {
            updateConfig(nftItem, NftConfigurationEnum.UNASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(distributeUrl, params, ret);
            throw new GenericException(ERR_50001_CALL_GAME_INTERFACE_ERROR, "Failed to call game-api to distribute nft," + ret);
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
            throw new GenericException(ERR_50001_CALL_GAME_INTERFACE_ERROR, "Failed to call game-api to takeback nft,connect timed out");
        }

        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String ret = jsonObject.getString("ret");
        log.info("请求游戏 nft 收回接口返回，  result:{}", response.body());
        if (!"ok".equalsIgnoreCase(ret)) {
            updateConfig(nftItem, NftConfigurationEnum.ASSIGNED.getCode());
            // 记录调用错误日志
            errorLog(takebackUrl, params, ret);
            throw new GenericException(ERR_50001_CALL_GAME_INTERFACE_ERROR, "Failed to call game-api to takeback nft," + ret);
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

    private void updateAttr(NftPublicBackpackReq req) {
        NftAttributesDto attributesDto = JSONUtil.toBean(req.getAttributes(), NftAttributesDto.class);
        NftPublicBackpackEntity backpackEntity = this.getOne(new LambdaQueryWrapper<NftPublicBackpackEntity>().eq(NftPublicBackpackEntity::getAutoId, req.getAutoId()));
        NftAttributeEntity nftAttributeEntity = new NftAttributeEntity();
        if (backpackEntity.getType().equals(NFTEnumConstant.NftTypeEnum.HERO.getCode())) {
            nftAttributeEntity.setVictory(attributesDto.getWin());
            nftAttributeEntity.setLose(attributesDto.getDefeat());
            nftAttributeEntity.setMaxStreak(attributesDto.getSeqWin());
            nftAttributeEntity.setMaxLose(attributesDto.getSeqDefeat());
            nftAttributeEntity.setCapture(attributesDto.getKillPlayer());
            nftAttributeEntity.setKillingSpree(attributesDto.getSeqKill());
            nftAttributeEntity.setGoblinKill(attributesDto.getKillNpc());
            nftAttributeEntity.setSlaying(attributesDto.getKilledByPlayer());
            nftAttributeEntity.setGoblin(attributesDto.getKilledByNpc());
        } else {
            nftAttributeEntity.setDurability(attributesDto.getDurability());
            nftAttributeEntity.setRarityAttr(attributesDto.getRarityAttr());
        }
        NftAttributeEntity one = attributeService.getOne(new LambdaUpdateWrapper<NftAttributeEntity>().eq(NftAttributeEntity::getEqNftId, backpackEntity.getEqNftId()));
        if (null == one) {
            attributeService.save(nftAttributeEntity);
        } else {
            attributeService.update(nftAttributeEntity, new LambdaUpdateWrapper<NftAttributeEntity>().eq(NftAttributeEntity::getEqNftId, backpackEntity.getEqNftId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchByQueryWrapper(Collection<NftPublicBackpackEntity> entityList, Function<NftPublicBackpackEntity, LambdaQueryWrapper> wrapperFunction) {
        String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE);
        return this.executeBatch(entityList, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            MapperMethod.ParamMap param = new MapperMethod.ParamMap();
            param.put(Constants.ENTITY, entity);
            param.put(Constants.WRAPPER, wrapperFunction.apply(entity));
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    public void skinUnDeposited(NftUnDepositedReq req) {
        // NFT锁定中不能取回
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
        if (backpackNft.getServerRoleId().compareTo(new Long(NFTEnumConstant.NFTTransEnum.BACKPACK.getCode())) != 0) {
            this.callGameTakeback(backpackNft);
        }
        // 更新背包状态
        backpackNft.setIsConfiguration(NftConfigurationEnum.UNASSIGNED.getCode());
        backpackNft.setServerRoleId(0L);
        backpackNft.setState(NFTEnumConstant.NFTStateEnum.UNDEPOSITED.getCode());
        backpackNft.setUpdatedAt(System.currentTimeMillis());
        this.updateById(backpackNft);
        // NFT非归属人不能取回
        ucUserService.ownerValidation(nft.getOwner());
        // 调用api/admin/withdrawNft通知，取回NFT成功
        String url = seedsApiConfig.getBaseDomain() + seedsApiConfig.getSkinWithdraw();
        TransferNftMessageDto dto = new TransferNftMessageDto();
        BeanUtils.copyProperties(req, dto);
        dto.setMintAddress(nft.getMintAddress());
        String param = JSONUtil.toJsonStr(dto);
        log.info("NFT取回成功，开始通知， url:{}， params:{}", url, param);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .timeout(30 * 1000)
                    .header("Content-Type", "application/json")
                    .body(param)
                    .execute();
            log.info("NFT取回成功通知返回，result:{}", response.body());
            JSONObject jsonObject = JSONObject.parseObject(response.body());
            String code = jsonObject.getString("code");
            if (!"200".equalsIgnoreCase(code)) {
                throw new GenericException(jsonObject.getString("message"));
            }
        } catch (Exception e) {
            log.error("NFT取回成功通知失败，message：{}", e.getMessage());
            throw new GenericException("NFT retrieval failure");
        }

        // 发送皮肤nft withdraw 成功消息
        NftAttributeEntity attributeEntity = attributeService.getOne(new LambdaQueryWrapper<NftAttributeEntity>().eq(NftAttributeEntity::getEqNftId, backpackNft.getEqNftId()));
        SkinNftWithdrawDto withdrawDto = new SkinNftWithdrawDto();
        BeanUtils.copyProperties(attributeEntity, withdrawDto);
        withdrawDto.setNftPicId(backpackNft.getNftPicId());
        kafkaProducer.sendAsync(KafkaTopic.SKIN_NFT_WITHDRAW, JSONUtil.toJsonStr(withdrawDto));
    }

    @Override
    public void insertBackpack(List<NftPublicBackpackDto> backpackDtos) {
        log.info("皮肤mint成功,插入背包表--->：{}", JSONUtil.toJsonStr(backpackDtos));
        BigDecimal usdRate = nftMarketPlaceService.usdRate(CurrencyEnum.SOL.getCode());
        List<NftPublicBackpackEntity> entities = CglibUtil.copyList(backpackDtos, NftPublicBackpackEntity::new);
        entities.forEach(p -> {
            // 设置皮肤参考价
            p.setProposedPrice(new BigDecimal(SkinNftEnums.SkinNftPrice.SKIN_NFT_PRICE.getPrice()).divide(usdRate, 2, BigDecimal.ROUND_HALF_UP));
        });
        this.saveBatch(entities);
        // 插入属性表
        List<NftAttributeEntity> attributeEntities = backpackDtos.stream().map(p -> {
            NftAttributeEntity attributeEntity = new NftAttributeEntity();
            attributeEntity.setEqNftId(p.getEqNftId());
            attributeEntity.setTokenId(p.getTokenId());
            attributeEntity.setMintAddress(p.getTokenAddress());
            attributeEntity.setHeroType(NftHeroTypeEnum.getCode(p.getProfession()));
            attributeEntity.setRarity(NftRarityEnum.codeOfDesc(p.getRarity()));
            return attributeEntity;
        }).collect(Collectors.toList());
        attributeService.saveBatch(attributeEntities);
    }

    @Override
    public Map<String, List<SkinNftTypeResp>> getSkinNftTypeList(Integer heroType) {
        String profession = "";
        if (null != heroType) {
            profession = NftHeroTypeEnum.getProfessionByCode(heroType);
        }
        Long userId = UserContext.getCurrentUserId();
        List<SkinNftTypeResp> skinNftTypeList = baseMapper.getSkinNftTypeList(userId, profession);
        skinNftTypeList.forEach(p -> p.setImage(gameFileService.getFileUrl("game/skinPic/" + p.getSkinName().toLowerCase() + ".png")));
        Map<String, List<SkinNftTypeResp>> listMap = skinNftTypeList.stream().collect(Collectors.groupingBy(SkinNftTypeResp::getProfession));
        return listMap;
    }

    @Override
    public List<NftPublicBackpackSkinWebResp> getSkinPageForWeb(NftBackpackWebPageReq req) {
        if (Objects.nonNull(req.getSortType())) {
            req.setSortTypeStr(NftBackpackWebPageReq.convert(req.getSortType()));
        }
        List<NftPublicBackpackSkinWebResp> list = baseMapper.getSkinPageForWeb(req);

        list = list.stream().map(p -> {
            NftPublicBackpackSkinWebResp resp = new NftPublicBackpackSkinWebResp();
            BeanUtils.copyProperties(p, resp);
            if (p.getServerRoleId().compareTo(new Long(NFTEnumConstant.NFTTransEnum.BACKPACK.getCode())) == 0) {
                resp.setServerName(NFTEnumConstant.NFTTransEnum.BACKPACK.getDesc());
            }
            ServerRegionEntity serverRegionEntity = serverRegionService.queryByServerRoleId(p.getServerRoleId());
            if (Objects.nonNull(serverRegionEntity)) {
                resp.setServerName(serverRegionEntity.getGameServerName());
            }
            return resp;
        }).collect(Collectors.toList());
        return list;
    }
}
