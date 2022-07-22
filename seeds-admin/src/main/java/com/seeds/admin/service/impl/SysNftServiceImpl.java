package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.SysNftEntity;
import com.seeds.admin.entity.SysNftPropertiesEntity;
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
    private SysNftTypeService sysNftTypeService;

    @Autowired
    private SysNftPropertiesService sysNftPropertiesService;

    @Override
    public IPage<SysNftResp> queryPage(SysNftPageReq query) {
        QueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<>();
        queryWrap.likeRight(StringUtils.isNotBlank(query.getName()), "name", query.getName());
        queryWrap.eq(query.getGameId() != null, "game_id", query.getGameId());
        queryWrap.eq(query.getGameId() != null, "status", query.getStatus());
        queryWrap.eq(query.getNftTypeId() != null, "nft_type_id", query.getNftTypeId());
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
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
            return resp;
        });
    }

    @Override
    public SysNftEntity queryById(Long id) {
        QueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq("id", id);
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(queryWrap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysNftAddReq req) {
        // 修改NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        updateById(sysNft);
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
        }
        return resp;
    }

    @Override
    public void modify(SysNftModifyReq req) {
        // 添加NFT
        SysNftEntity sysNft = new SysNftEntity();
        BeanUtils.copyProperties(req, sysNft);
        save(sysNft);
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
        ids.forEach(p -> {
            SysNftEntity nft = new SysNftEntity();
            nft.setId(p);
            nft.setDeleteFlag(WhetherEnum.YES.value());
            updateById(nft);
        });
        // 删除和NFT属性的关联
        sysNftPropertiesService.deleteByNftIs(ids);
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
