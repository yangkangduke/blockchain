package com.seeds.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.mq.NftUpgradeMsgDTO;
import com.seeds.admin.dto.response.*;
import com.seeds.chain.service.GameItemsService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.admin.dto.mq.NftMintMsgDTO;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.entity.*;
import com.seeds.admin.enums.*;
import com.seeds.admin.mapper.SysNftMapper;
import com.seeds.admin.mq.producer.KafkaProducer;
import com.seeds.admin.service.*;
import com.seeds.chain.config.SmartContractConfig;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.ApiType;
import com.seeds.common.enums.RequestSource;
import com.seeds.common.exception.SeedsException;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.dto.request.NFTShelvesReq;
import com.seeds.uc.dto.request.NFTSoldOutReq;
import com.seeds.uc.enums.AccountActionStatusEnum;
import com.seeds.uc.enums.CurrencyEnum;
import com.seeds.uc.enums.NFTOfferStatusEnum;
import com.seeds.uc.feign.RemoteNFTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;


/**
 * 系统NFT
 *
 * @author hang.yu
 * @date 2022/7/22
 */
@Slf4j
@Service
public class SysNftServiceImpl extends ServiceImpl<SysNftMapper, SysNftEntity> implements SysNftService {

    private static final String NUMBER_PATTER = "^[0-9]*$";

    @Autowired
    private SysGameService sysGameService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ChainNftService chainNftService;

    @Autowired
    private RemoteNFTService remoteNftService;

    @Autowired
    private SysGameApiService sysGameApiService;

    @Autowired
    private SysNftTypeService sysNftTypeService;

    @Autowired
    private SysNftHonorService sysNftHonorService;

    @Autowired
    private SmartContractConfig smartContractConfig;

    @Autowired
    private SysSequenceNoService sysSequenceNoService;

    @Autowired
    private SysNftPropertiesService sysNftPropertiesService;

    @Autowired
    private SysNftEventRecordService sysNftEventRecordService;

    @Autowired
    private SysNftPropertiesTypeService sysNftPropertiesTypeService;

    @Autowired
    private GameItemsService gameItemsService;

    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public IPage<SysNftResp> queryPage(SysNftPageReq query) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysNftEntity::getName, query.getName())
                .eq(query.getStatus() != null, SysNftEntity::getStatus, query.getStatus())
                .eq(query.getNftTypeId() != null, SysNftEntity::getNftTypeId, query.getNftTypeId())
                .eq(query.getUserId() != null, SysNftEntity::getOwnerId, query.getUserId())
                .eq(query.getInitStatus() != null, SysNftEntity::getInitStatus, query.getInitStatus())
                .eq(query.getLockFlag() != null, SysNftEntity::getLockFlag, query.getLockFlag())
                .eq(query.getGameId() != null, SysNftEntity::getGameId, query.getGameId())
                .ge(query.getMinPrice() != null, SysNftEntity::getPrice, query.getMinPrice())
                .le(query.getMaxPrice() != null, SysNftEntity::getPrice, query.getMaxPrice());
                buildOrderBy(query, queryWrap);
        Page<SysNftEntity> page = page(new Page<>(query.getCurrent(), query.getSize()), queryWrap);
        List<SysNftEntity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> gameIds = records.stream().map(SysNftEntity::getGameId).collect(Collectors.toSet());
        Map<Long, String> gameMap = sysGameService.queryMapByIds(gameIds);
        Set<Long> nftTypeIds = records.stream().map(SysNftEntity::getNftTypeId).collect(Collectors.toSet());
        Map<Long, SysNftTypeEntity> nftTypeMap = sysNftTypeService.queryMapByIds(nftTypeIds);
        return page.convert(p -> {
            SysNftResp resp = new SysNftResp();
            BeanUtils.copyProperties(p, resp);
            resp.setGameName(gameMap.get(p.getGameId()));
            SysNftTypeEntity nftType = nftTypeMap.get(p.getNftTypeId());
            if (nftType != null) {
                resp.setTypeCode(nftType.getCode());
                resp.setTypeName(nftType.getName());
            }
            // 图片
            resp.setPicture(smartContractConfig.getIpfsUrl() + p.getUrl());
            return resp;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(String imageFileHash, SysNftAddReq req, String metadataHash) {
        // 校验基础属性缺失或重复
        List<NftPropertiesReq> propertiesList = req.getPropertiesList();
        List<NftPropertiesReq> enduranceList = propertiesList.stream().filter(p -> p.getTypeId() != null && p.getTypeId() == 1L).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(enduranceList) || enduranceList.size() > 1) {
            throw new SeedsException(String.format(AdminErrorCodeEnum.ERR_40010_DUPLICATE_OR_MISSING_BASE_ATTRIBUTES_OF_NFT.getDescEn(), 1));
        }
        NftPropertiesReq properties = enduranceList.get(0);
        if (!properties.getValue().matches(NUMBER_PATTER)) {
            throw new SeedsException(String.format(AdminErrorCodeEnum.ERR_40011_NFT_PROPERTY_VALUE_IS_NOT_IN_THE_CORRECT_FORMAT.getDescEn(), 1));
        }
        // 添加NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        // 默认停售
        sysNft.setStatus(WhetherEnum.NO.value());
        sysNft.setInitStatus(NftInitStatusEnum.CREATING.getCode());
        // 上传NFT图片
        sysNft.setUrl(imageFileHash);
        // 生成NFT编号
        sysNft.setNumber(sysSequenceNoService.generateNftNo());
        sysNft.setMetadataHash(metadataHash);
        // 保存NFT
        save(sysNft);

        return sysNft.getId();
    }

    @Override
    public void addConfirm(SysNftAddConfirmReq req) {
        SysNftEntity nft = getById(req.getNftId());
        nft.setInitStatus(req.getInitStatus());
        nft.setTokenId(req.getNewItemId());
        nft.setContractAddress(smartContractConfig.getGameAddress());
        nft.setTokenStandard(smartContractConfig.getTokenStandard());
        nft.setBlockChain(smartContractConfig.getBlockchain());
        nft.setMetadata(smartContractConfig.getMetadataMode());
        nft.setCreatorFees(smartContractConfig.getCreatorFees());
        String errorMsg = req.getMessage();
        if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 255) {
            errorMsg = errorMsg.substring(0, 255);
        }
        nft.setErrorMsg(errorMsg);
        // 添加NFT属性
        updateById(nft);
    }

    @Override
    public SysNftAddResp addUpload(MultipartFile image, SysNftAddReq req) {
        // 上传NFT图片
        String imageFileHash = chainNftService.uploadImage(image);
        // 上传Metadata
        String metadataFileHash = chainNftService.uploadMetadata(imageFileHash,
                ChainMintNftReq.builder()
                        .name(req.getName())
                        .description(req.getDescription())
                        .attributes(buildNftProperties(req.getPropertiesList()))
                        .build());

        String metadataHash = "ipfs://" + metadataFileHash;
        SysNftAddResp resp = new SysNftAddResp();
        resp.setNftId(add(imageFileHash, req, metadataHash));
        resp.setMetadataHash(metadataHash);
        return resp;
    }

    @Override
    public Long createSend(SysNftCreateReq req, String topic) {
        SysNftEntity nft = queryByNumber(req.getNftNo());
        if (nft == null) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40016_This_type_of_NFT_has_not_yet_been_issued.getDescEn());
        }
        String imageFileHash = nft.getUrl();
        Long nftId = add(imageFileHash, req, nft.getMetadataHash());
        // 发NFT创建消息
        NftUpgradeMsgDTO msgDTO = new NftUpgradeMsgDTO();
        BeanUtils.copyProperties(req, msgDTO);
        msgDTO.setId(nftId);
        msgDTO.setImageFileHash(imageFileHash);

        kafkaProducer.send(topic, JSONUtil.toJsonStr(msgDTO));
        return nftId;
    }

    @Override
    public SysNftDetailResp detail(Long id) {
        SysNftEntity sysNft = getById(id);
        SysNftDetailResp resp = new SysNftDetailResp();
        if (sysNft != null) {
            // NFT信息
            BeanUtils.copyProperties(sysNft, resp);
            // 游戏信息
            SysGameEntity sysGame = sysGameService.queryById(sysNft.getGameId());
            if (sysGame != null) {
                resp.setGameName(sysGame.getName());
            }
            // NFT类别信息
            SysNftTypeEntity sysNftType = sysNftTypeService.queryById(sysNft.getNftTypeId());
            if (sysNftType != null) {
                resp.setTypeName(sysNftType.getName());
            }
            // NFT属性信息
            List<SysNftPropertiesEntity> propertiesList = sysNftPropertiesService.queryByNftId(id);
            List<NftPropertiesResp> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(propertiesList)) {
                Set<Long> typeIds = propertiesList.stream().map(SysNftPropertiesEntity::getTypeId).collect(Collectors.toSet());
                Map<Long, SysNftPropertiesTypeEntity> propertiesTypeMap = sysNftPropertiesTypeService.queryMapByIds(typeIds);
                propertiesList.forEach(p -> {
                    NftPropertiesResp res = new NftPropertiesResp();
                    BeanUtils.copyProperties(p, res);
                    SysNftPropertiesTypeEntity type = propertiesTypeMap.get(p.getTypeId());
                    if (type != null) {
                        res.setTypeId(type.getId());
                        res.setCode(type.getCode());
                        res.setName(type.getName());
                    }
                    list.add(res);
                });
            }
            resp.setPropertiesList(list);
            // 图片
            resp.setPicture(smartContractConfig.getIpfsUrl() + sysNft.getUrl());
        }
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysNftModifyReq req) {
        SysNftEntity sysNft = getById(req.getId());
        // 已上架的NFT不能修改
        if (WhetherEnum.YES.value() == sysNft.getStatus()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40006_NFT_ON_SALE_CAN_NOT_BE_MODIFIED.getDescEn());
        }
        // 已锁定的NFT不能修改
        if (WhetherEnum.YES.value() == sysNft.getLockFlag()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40007_NFT_LOCKED_CAN_NOT_BE_MODIFIED.getDescEn());
        }
        // 修改NFT属性
        addNftProperties(sysNft.getId(), req.getPropertiesList());
        // 更新NFT
        BeanUtils.copyProperties(req, sysNft);
        updateById(sysNft);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upOrDown(List<SwitchReq> req) {
        // 排除已锁定的NFT
        Set<Long> ids = req.stream().map(SwitchReq::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .eq(SysNftEntity::getLockFlag, WhetherEnum.NO.value())
                .in(SysNftEntity::getId, ids);
        List<SysNftEntity> nftList = list(queryWrap);
        if (CollectionUtils.isEmpty(nftList)) {
            return;
        }
        // uc端客户拥有的nft不能在admin端上架或者下架
        Set<Long> finalIds = nftList.stream().filter(p -> SysOwnerTypeEnum.UC_USER.getCode() != p.getOwnerType()).map(SysNftEntity::getId).collect(Collectors.toSet());
        Set<Long> set = new HashSet<>();
        // 上架/下架NFT
        req.forEach(p -> {
            if (finalIds.contains(p.getId())) {
                // 校验状态
                SysStatusEnum.from(p.getStatus());
                SysNftEntity nft = new SysNftEntity();
                nft.setId(p.getId());
                nft.setStatus(p.getStatus());
                // 上架
                if (WhetherEnum.YES.value() == p.getStatus()) {
                    set.add(nft.getId());
                } else {
                    updateById(nft);
                }
            }
        });
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        Set<Long> modifyIds = nftList.stream()
                .filter(p -> set.contains(p.getId()) && NftInitStatusEnum.NORMAL.getCode() == p.getInitStatus())
                .map(SysNftEntity::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(modifyIds)) {
            return;
        }
        Set<Long> modifySet = set.stream().filter(modifyIds::contains).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(modifySet)) {
            return;
        }
        modifySet.forEach(p -> {
            SysNftEntity nft = new SysNftEntity();
            nft.setId(p);
            nft.setStatus(WhetherEnum.YES.value());
            updateById(nft);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<NftDeleteListReq> req) {
        Map<Long, NftDeleteListReq> nftMap = req.stream().collect(Collectors.toMap(NftDeleteListReq::getNftId, p -> p));
        List<SysNftEntity> sysNftEntities = listByIds(new HashSet<>(nftMap.keySet()));
        List<SysNftEntity> deleteList = new ArrayList<>();
        sysNftEntities.forEach(p ->{
            // 创建失败的直接删除
            if (NftInitStatusEnum.CREATE_FAILED.getCode() == p.getInitStatus()) {
                removeById(p);
            } else {
                // 排除已上架和已锁定的NFT
                if (WhetherEnum.NO.value() == p.getStatus() && WhetherEnum.NO.value() == p.getLockFlag()) {
                    p.setInitStatus(NftInitStatusEnum.DELETING.getCode());
                    updateById(p);
                    deleteList.add(p);
                }
            }
        });
        if (CollectionUtils.isEmpty(deleteList)) {
            return;
        }
        // 删除链上数据
        //executorService.submit(() -> {
        //   burnNft(deleteList);
        //});
        deleteList.forEach(p -> {
            NftDeleteListReq nftReq = nftMap.get(p.getId());
            if (NftInitStatusEnum.DELETE_FAILED.getCode() != nftReq.getInitStatus()) {
                // 删除NFT
                removeById(p.getId());
                // 删除和NFT属性的关联
                sysNftPropertiesService.deleteByNftId(p.getId());
            }
            String errorMsg = nftReq.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 255) {
                errorMsg = errorMsg.substring(0, 255);
            }
            p.setErrorMsg(errorMsg);
            p.setInitStatus(nftReq.getInitStatus());
            updateById(p);
        });
    }

    @Override
    public void propertiesValueModify(List<NftPropertiesValueModifyReq> req) {
        req.forEach(p -> {
            SysNftPropertiesEntity entity = new SysNftPropertiesEntity();
            BeanUtils.copyProperties(p, entity);
            sysNftPropertiesService.updateById(entity);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ownerChange(List<NftOwnerChangeReq> req) {
        Set<Long> nftIds = req.stream().map(NftOwnerChangeReq::getId).collect(Collectors.toSet());
        List<SysNftEntity> list = listByIds(nftIds);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 回调通知uc
        req.forEach(p -> {
            transferNft(p, list);
        });
    }

    @Override
    public SysNftDetailResp ucDetail(Long id) {
        return detail(id);
    }

    @Override
    public void ucCollection(Long id) {
        LambdaUpdateWrapper<SysNftEntity> queryWrap = new UpdateWrapper<SysNftEntity>().lambda()
                .setSql("`collections`=`collections`+1")
                .eq(SysNftEntity::getId, id);
        update(queryWrap);
    }

    @Override
    public void ucView(Long id) {
        LambdaUpdateWrapper<SysNftEntity> queryWrap = new UpdateWrapper<SysNftEntity>().lambda()
                .setSql("`views`=`views`+1")
                .eq(SysNftEntity::getId, id);
        update(queryWrap);
    }

    @Override
    public void ucUpOrDown(UcSwitchReq req) {
        List<UpOrDownReq> reqs = req.getReqs();
        // 过滤出正常且未锁定的NFT
        List<SysNftEntity> sysNftEntities = queryNormalByOwnerId(req.getUcUserId());
        if (CollectionUtils.isEmpty(sysNftEntities)) {
            return;
        }
        Set<Long> ids = sysNftEntities.stream().map(SysNftEntity::getId).collect(Collectors.toSet());
        reqs = reqs.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(reqs)) {
            return;
        }
        // 上架/下架NFT
        reqs.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysNftEntity nft = new SysNftEntity();
            // 上架
            if (SysStatusEnum.ENABLED.value() == p.getStatus()) {
                nft.setUnit(p.getUnit());
                nft.setPrice(p.getPrice());
            }
            nft.setId(p.getId());
            nft.setStatus(p.getStatus());
            updateById(nft);
        });
    }

    @Override
    public List<SysNftEntity> queryNormalByOwnerId(Long ownerId) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .eq(SysNftEntity::getOwnerId, ownerId)
                .eq(SysNftEntity::getLockFlag, WhetherEnum.NO.value())
                .eq(SysNftEntity::getInitStatus, NftInitStatusEnum.NORMAL.getCode());
        return list(queryWrap);
    }

    private void addNftProperties(Long nftId, List<NftPropertiesReq> propertiesList) {
        if (!CollectionUtils.isEmpty(propertiesList)) {
            List<SysNftPropertiesEntity> nftPropertiesList = new ArrayList<>();
            propertiesList.forEach(p -> {
                SysNftPropertiesEntity nftProperties = new SysNftPropertiesEntity();
                nftProperties.setNftId(nftId);
                p.setTypeId(p.getTypeId() == null ? -1 : p.getTypeId());
                BeanUtils.copyProperties(p, nftProperties);
                nftPropertiesList.add(nftProperties);
            });
            sysNftPropertiesService.saveOrUpdate(nftId, nftPropertiesList);
        }
    }

    private List<ChainNftAttributes> buildNftProperties(List<NftPropertiesReq> propertiesList) {
        List<ChainNftAttributes> attributes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(propertiesList)) {
            propertiesList.forEach(p -> {
                ChainNftAttributes attribute = new ChainNftAttributes();
                attribute.setTraitType(p.getName());
                attribute.setValue(p.getValue());
                attributes.add(attribute);
            });
        }
        return attributes;
    }

    @Override
    public Boolean mintNft(NftMintMsgDTO msgDTO) {
        int initStatus = NftInitStatusEnum.NORMAL.getCode();
        String errorMsg;
        SysNftEntity nft = getById(msgDTO.getId());
        try {
            // 上传Metadata
            String metadataFileHash = chainNftService.uploadMetadata(msgDTO.getImageFileHash(),
                    ChainMintNftReq.builder()
                            .name(msgDTO.getName())
                            .description(msgDTO.getDescription())
                            .attributes(buildNftProperties(msgDTO.getPropertiesList()))
                            .build());
            // 在链上创建NFT
            ChainMintNftResp chainMintNftResp = chainNftService.mintNft(metadataFileHash);
            if (chainMintNftResp == null) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn());
            }
            nft.setBlockChain(chainMintNftResp.getBlockchain());
            BeanUtils.copyProperties(chainMintNftResp, nft);
            nft.setStatus(msgDTO.getStatus());
            nft.setMetadataHash("ipfs://" + metadataFileHash);
            // 添加NFT属性
            addNftProperties(msgDTO.getId(), msgDTO.getPropertiesList());
        } catch (Exception e) {
            log.error("NFT创建失败， id={}, msg={}", msgDTO.getId(), e.getMessage());
            initStatus = NftInitStatusEnum.CREATE_FAILED.getCode();
            errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 255) {
                errorMsg = errorMsg.substring(0, 255);
            }
            nft.setErrorMsg(errorMsg);
        }
        // 更新NFT
        nft.setInitStatus(initStatus);
        updateById(nft);
        return NftInitStatusEnum.NORMAL.getCode() == initStatus;
    }

    @Deprecated
    private void modifyNft(SysNftEntity sysNft, SysNftModifyReq req) {
        int initStatus = NftInitStatusEnum.NORMAL.getCode();
        String errorMsg;
        try {
            // 修改链上数据
            String imageFileHash = chainNftService.getMetadataFileImageHash(sysNft.getTokenId());
            String metadataFileHash = chainNftService.updateMetadata(imageFileHash,
                    ChainUpdateNftReq.builder()
                            .name(req.getName())
                            .description(req.getDescription())
                            .attributes(buildNftProperties(req.getPropertiesList()))
                            .build());
            // 删除链上的NFT
            if (!chainNftService.updateNftAttribute(sysNft.getTokenId(), metadataFileHash)) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn());
            }
            // 更新NFT
            BeanUtils.copyProperties(req, sysNft);
        } catch (Exception e) {
            log.error("NFT修改失败， id={}, msg={}", sysNft.getId(), e.getMessage());
            initStatus = NftInitStatusEnum.UPDATE_FAILED.getCode();
            errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 255) {
                errorMsg = errorMsg.substring(0, 255);
            }
            sysNft.setErrorMsg(errorMsg);
        }
        sysNft.setInitStatus(initStatus);
        updateById(sysNft);
    }

    @Override
    public void burnNft(List<SysNftEntity> sysNftEntities) {
        if (CollectionUtils.isEmpty(sysNftEntities)) {
            return;
        }
        sysNftEntities.forEach(p -> {
            String errorMsg;
            try {
                // 删除链上的NFT
                if (!chainNftService.burnNft(p.getTokenId())) {
                    throw new SeedsException(AdminErrorCodeEnum.ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn());
                }
                // 删除和NFT属性的关联
                sysNftPropertiesService.deleteByNftId(p.getId());
                // 删除NFT
                removeById(p.getId());
            } catch (Exception e) {
                log.error("NFT删除失败， id={}, msg={}", p.getId(), e.getMessage());
                errorMsg = e.getMessage();
                if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 255) {
                    errorMsg = errorMsg.substring(0, 255);
                }
                p.setErrorMsg(errorMsg);
                p.setInitStatus(NftInitStatusEnum.DELETE_FAILED.getCode());
                updateById(p);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void honorModify(List<SysNftHonorModifyReq> req) {
        Set<Long> nftIds = req.stream().map(SysNftHonorModifyReq::getNftId).collect(Collectors.toSet());
        sysNftHonorService.removeByNftIds(nftIds);
        req.forEach(p -> {
            // 战绩记录更新
            SysNftHonorEntity nftHonor = new SysNftHonorEntity();
            BeanUtils.copyProperties(p, nftHonor);
            sysNftHonorService.save(nftHonor);
            // 记录触发事件
            if (!CollectionUtils.isEmpty(p.getEventList())) {
                p.getEventList().forEach(e -> {
                    SysNftEventRecordEntity eventRecord  = new SysNftEventRecordEntity();
                    eventRecord.setNftId(p.getNftId());
                    BeanUtils.copyProperties(e, eventRecord);
                    sysNftEventRecordService.save(eventRecord);
                });
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long upgradeSend(SysNftUpgradeReq req) {
        // 校验NFT
        upgradeValidate(req.getNftIdList(), req.getUserId(), req.getNftId());
        return createSend(req, KafkaTopic.NFT_UPGRADE_SUCCESS);
    }

    @Override
    public void upgrade(NftUpgradeMsgDTO req) {
        // 校验NFT
        List<Long> nftIdList = req.getNftIdList();
        upgradeValidate(nftIdList, req.getUserId(), req.getNftId());
        // NFT上链
        Boolean result = mintNft(req);
        // 成功
        if (result) {
            // 销毁消耗的NFT
            removeBatchByIds(nftIdList);
            // 更新战绩记录
            sysNftHonorService.successionByNftId(req.getNftId(), req.getId());
            // 更新事件记录
            sysNftEventRecordService.successionByNftId(req.getNftId(), req.getId());
        }
        String url = sysGameApiService.queryUrlByGameAndType(req.getGameId(), ApiType.NFT_NOTIFICATION.getCode());
        // todo 通知游戏方NFT升级结果
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lock(SysNftLockReq req) {
        Long nftId = req.getNftId();
        SysNftEntity nft = getById(nftId);
        // 判断NFT是否正常
        if (NftInitStatusEnum.NORMAL.getCode() != nft.getInitStatus()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 校验归属人
        if (!Objects.equals(req.getUserId(), nft.getOwnerId())) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40012_NFT_ATTRIBUTED_PERSON_MISMATCH.getDescEn());
        }
        // 判断NFT是否上架
        if (WhetherEnum.YES.value() == nft.getStatus()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40008_NFT_ON_SALE_CAN_NOT_LOCKED.getDescEn());
        }
        Integer endurance = req.getEndurance();
        if (endurance > 0) {
            // 判断NFT耐久值是否够用, 耐久是内置id为1的属性类别
            SysNftPropertiesEntity properties = sysNftPropertiesService.queryByTypeAndNftId(1L, nftId);
            if (properties == null) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
            }
            int newEndurance = (StringUtils.isEmpty(properties.getValue()) ? 0 : Integer.parseInt(properties.getValue())) - endurance;
            if (newEndurance < 0) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_40009_INSUFFICIENT_DURABILITY_VALUE_OF_NFT.getDescEn());
            }
            // 更新耐久值
            properties.setValue(Integer.toString(newEndurance));
            sysNftPropertiesService.updateById(properties);
        }
        // 锁定NFT
        nft.setLockFlag(WhetherEnum.YES.value());
        updateById(nft);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settlement(SysNftSettlementReq req) {
        // 归属人变更结算
        List<SysNftSettlementReq.NftSettlement> settlementList = req.getSettlementList();
        if (!CollectionUtils.isEmpty(settlementList)) {
            Map<Long, SysNftSettlementReq.NftSettlement> settlementMap = settlementList.stream().collect(Collectors.toMap(SysNftSettlementReq.NftSettlement::getNftId, p -> p));
            Set<Long> set = settlementMap.keySet();
            List<SysNftEntity> nftList = listByIds(set);
            if (CollectionUtils.isEmpty(nftList) || nftList.size() != set.size()) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
            }
            nftList.forEach(p -> {
                SysNftSettlementReq.NftSettlement nftSettlement = settlementMap.get(p.getId());
                // 判断NFT是否正常
                if (NftInitStatusEnum.NORMAL.getCode() != p.getInitStatus()) {
                    throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
                }
                // 校验归属人
                if (!Objects.equals(nftSettlement.getOldOwnerId(), p.getOwnerId())) {
                    throw new SeedsException(AdminErrorCodeEnum.ERR_40012_NFT_ATTRIBUTED_PERSON_MISMATCH.getDescEn());
                }
                p.setOwnerId(nftSettlement.getNewOwnerId());
                p.setOwnerName(nftSettlement.getNewOwnerName());
                p.setOwnerType(SysOwnerTypeEnum.UC_USER.getCode());
            });
            updateBatchById(nftList);
        }
        // 解除锁定NFT
        LambdaUpdateWrapper<SysNftEntity> queryWrap = new UpdateWrapper<SysNftEntity>().lambda()
                .set(SysNftEntity::getLockFlag, WhetherEnum.NO.value())
                .in(SysNftEntity::getId, req.getNftIds());
        update(queryWrap);
    }

    @Override
    public SysNftDetailResp detailApi(Long id) {
        return detail(id);
    }

    @Override
    public void shelves(NFTShelvesReq req) {
        Long nftId = req.getNftId();
        SysNftEntity nft = getById(nftId);
        // 判断NFT是否正常
        if (NftInitStatusEnum.NORMAL.getCode() != nft.getInitStatus()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 已锁定的NFT不可上架
        if (WhetherEnum.YES.value() == nft.getLockFlag()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40014_NFT_LOCKED_AND_CANNOT_BE_OPERATED.getDescEn());
        }
        // 判断NFT归属是否一致
        if (!req.getUserId().equals(nft.getOwnerId())) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40012_NFT_ATTRIBUTED_PERSON_MISMATCH.getDescEn());
        }
        //上架
        nft.setUnit(req.getUnit());
        nft.setPrice(req.getPrice());
        nft.setStatus(WhetherEnum.YES.value());
        updateById(nft);
    }

    @Override
    public void soldOut(NFTSoldOutReq req) {
        Long nftId = req.getNftId();
        SysNftEntity nft = getById(nftId);

        // 判断NFT归属是否一致
        if (!req.getUserId().equals(nft.getOwnerId())) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40012_NFT_ATTRIBUTED_PERSON_MISMATCH.getDescEn());
        }
        nft.setStatus(WhetherEnum.NO.value());
        updateById(nft);
    }

    @Override
    public SysNftGasFeesResp gasFees(String nftNo) {
        SysNftEntity nft = queryByNumber(nftNo);
        if (nft == null) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40016_This_type_of_NFT_has_not_yet_been_issued.getDescEn());
        }
        BigDecimal price = gameItemsService.gasFees(nft.getMetadataHash());
        SysNftGasFeesResp resp = new SysNftGasFeesResp();
        resp.setPrice(price);
        resp.setUnit(CurrencyEnum.USDT.getCode());
        return resp;
    }

    @Override
    public SysNftEntity queryByNumber(String number) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .eq(SysNftEntity::getNumber, number)
                .eq(SysNftEntity::getInitStatus, NftInitStatusEnum.NORMAL.getCode());
        return getOne(queryWrap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferNft(NftOwnerChangeReq req, List<SysNftEntity> list) {
        Map<Long, SysNftEntity> map = list.stream().collect(Collectors.toMap(SysNftEntity::getId, p -> p));
        SysNftEntity entity = new SysNftEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setOwnerType(SysOwnerTypeEnum.UC_USER.getCode());
        entity.setStatus(WhetherEnum.NO.value());
        updateById(entity);
        SysNftEntity nft = map.get(req.getId()) == null ? new SysNftEntity() : map.get(req.getId());
        NFTBuyCallbackReq callback = NFTBuyCallbackReq.builder()
                .fromUserId(nft.getOwnerId())
                .fromAddress(req.getFromAddress())
                .toUserId(req.getOwnerId())
                .amount(nft.getPrice())
                .tokenId(nft.getTokenId())
                .nftId(req.getId())
                .actionHistoryId(req.getActionHistoryId())
                .actionStatusEnum(AccountActionStatusEnum.SUCCESS)
                .offerId(req.getOfferId())
                .offerStatusEnum(NFTOfferStatusEnum.ACCEPTED)
                .toAddress(req.getToAddress())
                .ownerType(req.getOwnerType())
                .build();
        try {
            GenericDto<Object> result = remoteNftService.buyNFTCallback(callback);
            if (!result.isSuccess()) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
            }
        } catch (Exception e) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        // 请求来源是游戏方，交易完成通知游戏方
        if (RequestSource.GAME == req.getSource()) {
            String url = sysGameApiService.queryUrlByGameAndType(nft.getGameId(), ApiType.TRADE_NOTIFICATION.getCode());
            // todo 通知游戏方交易完成
        }
    }

    private void buildOrderBy(SysNftPageReq query, LambdaQueryWrapper<SysNftEntity> queryWrap) {
        if (query.getSortType() == null) {
            queryWrap.orderByDesc(SysNftEntity::getCreatedAt);
        } else {
            int descFlag = query.getDescFlag() == null ? 0 : query.getDescFlag();
            SortTypeEnum sortType = SortTypeEnum.from(query.getSortType());
            if (WhetherEnum.YES.value() == descFlag) {
                queryWrap.orderByDesc(getOrderType(sortType));
            } else {
                queryWrap.orderByAsc(getOrderType(sortType));
            }
        }
    }

    private SFunction<SysNftEntity, ?> getOrderType(SortTypeEnum sortType) {
        switch (sortType) {
            case PRICE:
                return SysNftEntity::getPrice;
            case RANK:
                return SysNftEntity::getCollections;
            default:
                return SysNftEntity::getCreatedAt;
        }
    }

    private void upgradeValidate(List<Long> nftIds, Long userId, Long nftId) {
        // 校验NFT是否正常存在
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .eq(SysNftEntity::getInitStatus, NftInitStatusEnum.NORMAL.getCode())
                .in(SysNftEntity::getId, nftIds);
        List<SysNftEntity> nftList = list(queryWrap);
        if (CollectionUtils.isEmpty(nftList) || nftIds.size() != nftList.size() || !nftIds.contains(nftId)) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
        }
        for (SysNftEntity nft : nftList) {
            // 校验归属人
            if (!Objects.equals(userId, nft.getOwnerId())) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
            }
            // 校验nft是否上架中
            if (WhetherEnum.YES.value() == nft.getStatus()) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_40006_NFT_ON_SALE_CAN_NOT_BE_MODIFIED.getDescEn());
            }
            // 校验nft是否已锁定
            if (WhetherEnum.YES.value() == nft.getLockFlag()) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_40014_NFT_LOCKED_AND_CANNOT_BE_OPERATED.getDescEn());
            }
        }
    }
}

