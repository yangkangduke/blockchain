package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.ChainMintNftResp;
import com.seeds.admin.dto.response.NftPropertiesResp;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.*;
import com.seeds.admin.enums.NftInitStatusEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysNftMapper;
import com.seeds.admin.service.*;
import com.seeds.chain.config.SmartContractConfig;
import com.seeds.common.exception.SeedsException;
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
                .eq(query.getGameId() != null, SysNftEntity::getGameId, query.getGameId());
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
            // 价格
            resp.setPrice(p.getPrice() + p.getUnit());
            return resp;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MultipartFile image, SysNftAddReq req) {
        // 添加NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        // 生成NFT编号
        sysNft.setNumber(sysSequenceNoService.generateNftNo());
        sysNft.setInitStatus(NftInitStatusEnum.CREATING.getCode());
        save(sysNft);
        // 添加NFT属性
        addNftProperties(sysNft.getId(), req.getPropertiesList());
        // NFT上链
        executorService.submit(() -> {
            mintNft(image, req, sysNft.getId());
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
            SysGameEntity sysGame = sysGameService.queryById(sysNft.getId());
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
            throw new SeedsException("NFT is on sale and cannot be modified");
        }
        // 修改NFT状态
        sysNft.setInitStatus(NftInitStatusEnum.UPDATING.getCode());
        updateById(sysNft);
        // 修改NFT属性
        addNftProperties(sysNft.getId(), req.getPropertiesList());
        // 修改链上NFT
        executorService.submit(() -> {
            modifyNft(sysNft, req);
        });
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        Set<Long> set = new HashSet<>();
        // 上架/下架NFT
        req.forEach(p -> {
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
        });
        List<SysNftEntity> sysNftEntities = listByIds(set);
        if (CollectionUtils.isEmpty(sysNftEntities)) {
            return;
        }
        Set<Long> modifyIds = sysNftEntities.stream().filter(p -> NftInitStatusEnum.NORMAL.getCode() == p.getInitStatus()).map(SysNftEntity::getGameId).collect(Collectors.toSet());
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
        Set<Long> ids = req.getIds();
        // 删除链上的NFT
        List<SysNftEntity> sysNftEntities = listByIds(ids);
        if (!CollectionUtils.isEmpty(sysNftEntities)) {
            sysNftEntities.forEach(p -> {
                chainNftService.burnNft(p.getTokenId());
            });
        }
        // 删除NFT
        removeBatchByIds(ids);
        // 删除和NFT属性的关联
        sysNftPropertiesService.deleteByNftIs(ids);
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
        req.forEach(p -> {
            SysNftEntity entity = new SysNftEntity();
            BeanUtils.copyProperties(p, entity);
            updateById(entity);
        });
        // todo NFT transfer
    }

    @Override
    public Page<SysNftResp> ucPage(SysNftPageReq query) {
        Page<SysNftResp> respPage = new Page<>(query.getCurrent(), query.getSize());
        IPage<SysNftResp> page = queryPage(query);
        BeanUtils.copyProperties(page, respPage);
        respPage.setRecords(page.getRecords());
        return respPage;
    }

    @Override
    public SysNftDetailResp ucDetail(Long id) {
        return detail(id);
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

    private void mintNft(MultipartFile image, SysNftAddReq req, Long id) {
        int initStatus = NftInitStatusEnum.NORMAL.getCode();
        String imageFileHash = null;
        SysNftEntity nft = getById(id);
        try {
            // 上传NFT图片
            imageFileHash = chainNftService.uploadImage(image);
            // 上传Metadata
            String metadataFileHash = chainNftService.uploadMetadata(imageFileHash,
                    ChainMintNftReq.builder()
                            .name(req.getName())
                            .description(req.getDescription())
                            .attributes(buildNftProperties(req.getPropertiesList()))
                            .build());
            // 在链上创建NFT
            ChainMintNftResp chainMintNftResp = chainNftService.mintNft(metadataFileHash);
            BeanUtils.copyProperties(chainMintNftResp, nft);
        } catch (Exception e) {
            log.error("NFT创建失败， id={}, msg={}", id, e.getMessage());
            initStatus = NftInitStatusEnum.CREATE_FAILED.getCode();
        }
        // 更新NFT
        nft.setInitStatus(initStatus);
        nft.setUrl(smartContractConfig.getIpfsUrl() + imageFileHash);
        updateById(nft);
    }

    private void modifyNft(SysNftEntity sysNft, SysNftModifyReq req) {
        int initStatus = NftInitStatusEnum.NORMAL.getCode();
        try {
            // 修改链上数据
            String imageFileHash = chainNftService.getMetadataFileImageHash(sysNft.getTokenId());
            String metadataFileHash = chainNftService.updateMetadata(imageFileHash,
                    ChainUpdateNftReq.builder()
                            .name(req.getName())
                            .description(req.getDescription())
                            .attributes(buildNftProperties(req.getPropertiesList()))
                            .build());
            chainNftService.updateNftAttribute(sysNft.getTokenId(), metadataFileHash);
            // 更新NFT
            BeanUtils.copyProperties(req, sysNft);
        } catch (Exception e) {
            log.error("NFT修改失败， id={}, msg={}", sysNft.getId(), e.getMessage());
            initStatus = NftInitStatusEnum.UPDATE_FAILED.getCode();
        }
        sysNft.setInitStatus(initStatus);
        updateById(sysNft);
    }
}

