package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.SysGameEntity;
import com.seeds.admin.entity.SysNftEntity;
import com.seeds.admin.entity.SysNftPropertiesEntity;
import com.seeds.admin.entity.SysNftTypeEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysNftMapper;
import com.seeds.admin.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统NFT
 *
 * @author hang.yu
 * @date 2022/7/22
 */
@Service
public class SysNftServiceImpl extends ServiceImpl<SysNftMapper, SysNftEntity> implements SysNftService {

    @Autowired
    private SysGameService sysGameService;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysNftTypeService sysNftTypeService;

    @Autowired
    private SysSequenceNoService sysSequenceNoService;

    @Autowired
    private SysNftPropertiesService sysNftPropertiesService;

    @Override
    public IPage<SysNftResp> queryPage(SysNftPageReq query) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysNftEntity::getName, query.getName())
                .eq(query.getGameId() != null, SysNftEntity::getGameId, query.getGameId())
                .eq(query.getStatus() != null, SysNftEntity::getStatus, query.getStatus())
                .eq(query.getNftTypeId() != null, SysNftEntity::getNftTypeId, query.getNftTypeId())
                .eq(SysNftEntity::getDeleteFlag, WhetherEnum.NO.value());
        Page<SysNftEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysNftEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> gameIds = records.stream().map(SysNftEntity::getGameId).collect(Collectors.toSet());
        Map<Long, String> gameMap = sysGameService.queryMapByIds(gameIds);
        Set<Long> nftTypeIds = records.stream().map(SysNftEntity::getNftTypeId).collect(Collectors.toSet());
        Map<Long, String> nftTypeMap = sysNftTypeService.queryNameMapByIds(nftTypeIds);
        return page.convert(p -> {
            SysNftResp resp = new SysNftResp();
            BeanUtils.copyProperties(p, resp);
            resp.setGameName(gameMap.get(p.getGameId()));
            resp.setTypeName(nftTypeMap.get(p.getNftTypeId()));
            // 图片
            if (StringUtils.isNotBlank(p.getObjectName())) {
                resp.setPicture(sysFileService.getFile(p.getObjectName()));
            }
            return resp;
        });
    }

    @Override
    public SysNftEntity queryById(Long id) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .eq(SysNftEntity::getId, id)
                .eq(SysNftEntity::getDeleteFlag, WhetherEnum.NO.value());
        return getOne(queryWrap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysNftAddReq req) {
        // 添加NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        // 生成NFT编号
        sysNft.setNumber(sysSequenceNoService.generateNftNo());
        save(sysNft);
        // 添加NFT属性
        addNftProperties(sysNft.getId(), req.getPropertiesList());
    }

    @Override
    public SysNftDetailResp detail(Long id) {
        SysNftEntity sysNft = queryById(id);
        SysNftDetailResp resp = new SysNftDetailResp();
        if (sysNft != null) {
            // NFT信息
            BeanUtils.copyProperties(sysNft, resp);
            // 游戏信息
            SysGameEntity sysGame = sysGameService.getById(sysNft.getId());
            if (sysGame != null) {
                resp.setGameName(sysGame.getName());
            }
            // NFT类别信息
            SysNftTypeEntity sysNftType = sysNftTypeService.getById(sysNft.getNftTypeId());
            if (sysNftType != null) {
                resp.setTypeName(sysNftType.getName());
            }
            // NFT属性信息
            List<SysNftPropertiesEntity> propertiesList = sysNftPropertiesService.queryByNftId(id);
            List<NftProperties> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(propertiesList)) {
                propertiesList.forEach(p -> {
                    NftProperties res = new NftProperties();
                    BeanUtils.copyProperties(p, res);
                    list.add(res);
                });
            }
            resp.setPropertiesList(list);
            // 图片
            if (StringUtils.isNotBlank(sysNft.getObjectName())) {
                resp.setPicture(sysFileService.getFile(sysNft.getObjectName()));
            }
        }
        return resp;
    }

    @Override
    public void modify(SysNftModifyReq req) {
        // 添加NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        updateById(sysNft);
        // 修改NFT属性
        addNftProperties(sysNft.getId(), req.getPropertiesList());
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        // 上架/下架NFT
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysNftEntity nft = new SysNftEntity();
            nft.setId(p.getId());
            nft.setStatus(p.getStatus());
            updateById(nft);
        });
    }

    @Override
    public void batchDelete(ListReq req) {
        Set<Long> ids = req.getIds();
        // 删除NFT
        removeBatchByIds(ids);
        // 删除和NFT属性的关联
        sysNftPropertiesService.deleteByNftIs(ids);
    }

    @Override
    public SysNftEntity queryByContractAddress(String contractAddress) {
        LambdaQueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<SysNftEntity>().lambda()
                .eq(SysNftEntity::getContractAddress, contractAddress)
                .eq(SysNftEntity::getDeleteFlag, WhetherEnum.NO.value());
        return getOne(queryWrap);
    }

    private void addNftProperties(Long nftId, List<NftProperties> propertiesList) {
        if (!CollectionUtils.isEmpty(propertiesList)) {
            List<SysNftPropertiesEntity> nftPropertiesList = new ArrayList<>();
            propertiesList.forEach(p -> {
                SysNftPropertiesEntity nftProperties = new SysNftPropertiesEntity();
                BeanUtils.copyProperties(p, nftProperties);
                nftPropertiesList.add(nftProperties);
            });
            sysNftPropertiesService.saveOrUpdate(nftId, nftPropertiesList);
        }
    }
}

