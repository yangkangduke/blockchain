package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysNftTypeAddReq;
import com.seeds.admin.dto.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.response.SysNftTypeResp;
import com.seeds.admin.entity.SysNftTypeEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.utils.TreeUtils;
import com.seeds.admin.mapper.SysNftTypeMapper;
import com.seeds.admin.service.SysNftTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Service
public class SysNftTypeServiceImpl extends ServiceImpl<SysNftTypeMapper, SysNftTypeEntity> implements SysNftTypeService {

    @Override
    public Map<Long, String> queryNameMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        // 包含已删除的数据
        List<SysNftTypeEntity> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(SysNftTypeEntity::getId, SysNftTypeEntity::getName));
    }

    @Override
    public SysNftTypeEntity queryById(Long id) {
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda()
                .eq(SysNftTypeEntity::getId, id)
                .eq(SysNftTypeEntity::getDeleteFlag, WhetherEnum.NO.value());
        return getOne(query);
    }

    @Override
    public List<SysNftTypeResp> queryRespList(String name) {
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(name), SysNftTypeEntity::getName, name)
                .eq(SysNftTypeEntity::getDeleteFlag, WhetherEnum.NO.value())
                .orderByAsc(SysNftTypeEntity::getSort);
        return convertToResp(list(query));
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
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda()
                .eq(SysNftTypeEntity::getCode, code)
                .eq(SysNftTypeEntity::getDeleteFlag, WhetherEnum.NO.value());
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

