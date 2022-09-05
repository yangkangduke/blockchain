package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.ChainMintNftResp;
import com.seeds.admin.dto.response.NftPropertiesResp;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.*;
import com.seeds.admin.enums.*;
import com.seeds.admin.mapper.SysNftMapper;
import com.seeds.admin.service.*;
import com.seeds.chain.config.SmartContractConfig;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.exception.SeedsException;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.enums.AccountActionStatusEnum;
import com.seeds.uc.feign.RemoteNFTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private SysGameService sysGameService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private ChainNftService chainNftService;

    @Autowired
    private RemoteNFTService remoteNftService;

    @Autowired
    private SysNftTypeService sysNftTypeService;

    @Autowired
    private SmartContractConfig smartContractConfig;

    @Autowired
    private SysSequenceNoService sysSequenceNoService;

    @Autowired
    private SysNftPropertiesService sysNftPropertiesService;

    @Autowired
    private SysNftPropertiesTypeService sysNftPropertiesTypeService;

    @Override
    public IPage<SysNftResp> queryPage(SysNftPageReq query) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysNftEntity::getName, query.getName())
                .eq(query.getStatus() != null, SysNftEntity::getStatus, query.getStatus())
                .eq(query.getNftTypeId() != null, SysNftEntity::getNftTypeId, query.getNftTypeId())
                .eq(query.getUserId() != null, SysNftEntity::getOwnerId, query.getUserId())
                .eq(query.getInitStatus() != null, SysNftEntity::getInitStatus, query.getInitStatus())
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
            resp.setPicture(p.getUrl());
            return resp;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MultipartFile image, SysNftAddReq req) {
        // 添加NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        // 默认停售
        sysNft.setStatus(WhetherEnum.NO.value());
        sysNft.setInitStatus(NftInitStatusEnum.CREATING.getCode());
        // 上传NFT图片
        String imageFileHash = chainNftService.uploadImage(image);
        sysNft.setUrl(smartContractConfig.getIpfsUrl() + imageFileHash);
        // 生成NFT编号
        sysNft.setNumber(sysSequenceNoService.generateNftNo());
        // 保存NFT
        save(sysNft);
        // NFT上链
        executorService.submit(() -> {
            mintNft(req, sysNft.getId(), imageFileHash);
        });
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
            resp.setPicture(sysNft.getUrl());
        }
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysNftModifyReq req) {
        SysNftEntity sysNft = getById(req.getId());
        if (WhetherEnum.YES.value() == sysNft.getStatus()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_40006_NFT_ON_SALE_CAN_NOT_BE_MODIFIED.getDescEn());
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
        Set<Long> ids = req.stream().map(SwitchReq::getId).collect(Collectors.toSet());
        List<SysNftEntity> nftList = listByIds(ids);
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
    public void batchDelete(ListReq req) {
        List<SysNftEntity> sysNftEntities = listByIds(req.getIds());
        // 排除已上架的NFT
        List<SysNftEntity> deleteList = new ArrayList<>();
        sysNftEntities.forEach(p ->{
            // 创建失败的直接删除
            if (NftInitStatusEnum.CREATE_FAILED.getCode() == p.getInitStatus()) {
                removeById(p);
            } else {
                if (WhetherEnum.NO.value() == p.getStatus()) {
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
        executorService.submit(() -> {
            burnNft(deleteList);
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
                .eq(SysNftEntity::getInitStatus, NftInitStatusEnum.NORMAL.getCode());
        return list(queryWrap);
    }

    private void addNftProperties(Long nftId, List<NftPropertiesReq> propertiesList) {
        if (!CollectionUtils.isEmpty(propertiesList)) {
            List<SysNftPropertiesEntity> nftPropertiesList = new ArrayList<>();
            propertiesList.forEach(p -> {
                SysNftPropertiesEntity nftProperties = new SysNftPropertiesEntity();
                nftProperties.setNftId(nftId);
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

    private void mintNft(SysNftAddReq req, Long id, String imageFileHash) {
        int initStatus = NftInitStatusEnum.NORMAL.getCode();
        String errorMsg;
        SysNftEntity nft = getById(id);
        try {
            // 上传Metadata
            String metadataFileHash = chainNftService.uploadMetadata(imageFileHash,
                    ChainMintNftReq.builder()
                            .name(req.getName())
                            .description(req.getDescription())
                            .attributes(buildNftProperties(req.getPropertiesList()))
                            .build());
            // 在链上创建NFT
            ChainMintNftResp chainMintNftResp = chainNftService.mintNft(metadataFileHash);
            if (chainMintNftResp == null) {
                throw new SeedsException(AdminErrorCodeEnum.ERR_90001_FAIL_TO_EXECUTE_ON_CHAIN.getDescEn());
            }
            nft.setBlockChain(chainMintNftResp.getBlockchain());
            BeanUtils.copyProperties(chainMintNftResp, nft);
            nft.setStatus(req.getStatus());
            // 添加NFT属性
            addNftProperties(id, req.getPropertiesList());
        } catch (Exception e) {
            log.error("NFT创建失败， id={}, msg={}", id, e.getMessage());
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

    private void burnNft(List<SysNftEntity> sysNftEntities) {
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
                .toAddress(req.getToAddress())
                .ownerType(req.getOwnerType())
                .build();
        if (!remoteNftService.buyNFTCallback(callback).isSuccess()) {
            throw new SeedsException(AdminErrorCodeEnum.ERR_500_SYSTEM_BUSY.getDescEn());
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
}

