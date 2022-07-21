package com.seeds.admin.web.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.common.ListReq;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.game.request.SysNftTypeAddReq;
import com.seeds.admin.dto.game.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.game.response.SysNftTypeResp;
import com.seeds.admin.entity.game.SysNftTypeEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.utils.TreeUtils;
import com.seeds.admin.web.game.mapper.SysNftTypeMapper;
import com.seeds.admin.web.game.service.SysNftTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Service
public class SysNftTypeServiceImpl extends ServiceImpl<SysNftTypeMapper, SysNftTypeEntity> implements SysNftTypeService {

    @Override
    public List<SysNftTypeEntity> queryList() {
        QueryWrapper<SysNftTypeEntity> query = new QueryWrapper<>();
        query.eq("delete_flag", WhetherEnum.NO.value());
        query.orderByAsc("sort");
        return list(query);
    }

    @Override
    public SysNftTypeEntity queryById(Long id) {
        QueryWrapper<SysNftTypeEntity> query = new QueryWrapper<>();
        query.eq("id", id);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    public List<SysNftTypeResp> queryRespList() {
        return convertToResp(queryList());
    }

    @Override
    public void add(SysNftTypeAddReq req) {
        SysNftTypeEntity nftType = new SysNftTypeEntity();
        BeanUtils.copyProperties(req, nftType);
        save(nftType);
    }

    @Override
    public SysNftTypeResp detail(Long id) {
        SysNftTypeResp resp = new SysNftTypeResp();
        SysNftTypeEntity nftType = queryById(id);
        if (nftType != null) {
            BeanUtils.copyProperties(nftType, resp);
        }
        return resp;
    }

    @Override
    public void modify(SysNftTypeModifyReq req) {
        SysNftTypeEntity nftType = new SysNftTypeEntity();
        BeanUtils.copyProperties(req, nftType);
        updateById(nftType);
    }

    @Override
    public SysNftTypeEntity queryByTypeCode(String code) {
        QueryWrapper<SysNftTypeEntity> query = new QueryWrapper<>();
        query.eq("code", code);
        query.eq("delete_flag", WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(ListReq req) {
        Set<Long> ids = req.getIds();
        // 删除NFT类别
        ids.forEach(p -> {
            SysNftTypeEntity nftType = new SysNftTypeEntity();
            nftType.setId(p);
            nftType.setDeleteFlag(WhetherEnum.YES.value());
            updateById(nftType);
        });
        removeBatchByIds(ids);
        // todo 删除NFT类别和NFT的关联
        // todo 删除NFT
        // todo 删除NFT和游戏的关联

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableOrDisable(List<SwitchReq> req) {
        // 停用/启用NFT类别
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysNftTypeEntity nftType = new SysNftTypeEntity();
            nftType.setId(p.getId());
            nftType.setStatus(p.getStatus());
            updateById(nftType);
        });
        // todo 停用/启用NFT类别和NFT的关联
        // todo 停用/启用NFT
        // todo 停用/启用NFT和游戏的关联

    }

    private List<SysNftTypeResp> convertToResp(List<SysNftTypeEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysNftTypeResp> respList = new ArrayList<>();
        for (SysNftTypeEntity nftType : list) {
            SysNftTypeResp resp = new SysNftTypeResp();
            BeanUtils.copyProperties(nftType, resp);
            respList.add(resp);
        }
        // 根节点为空
        return TreeUtils.build(respList, "");
    }
}

