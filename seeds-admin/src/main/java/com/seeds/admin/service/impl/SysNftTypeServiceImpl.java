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
import com.seeds.admin.utils.TreeUtils;
import com.seeds.admin.mapper.SysNftTypeMapper;
import com.seeds.admin.service.SysNftTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SysNftTypeMapper sysNftTypeMapper;

    @Override
    public Map<Long, SysNftTypeEntity> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        // 包含已删除的数据
        List<SysNftTypeEntity> list = sysNftTypeMapper.queryListByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(SysNftTypeEntity::getId, p -> p));
    }

    @Override
    public SysNftTypeEntity queryById(Long id) {
        return sysNftTypeMapper.queryById(id);
    }

    @Override
    public List<SysNftTypeResp> queryRespList(String name) {
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(name), SysNftTypeEntity::getName, name)
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
        SysNftTypeEntity nftType = getById(id);
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
                .eq(SysNftTypeEntity::getCode, code);
        return getOne(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(ListReq req) {
        Set<Long> ids = req.getIds();
        // 删除NFT类别
        removeBatchByIds(ids);
    }

    @Override
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

    @Override
    public Long countKidsByCodes(Set<String> codes) {
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda()
                .in(SysNftTypeEntity::getParentCode, codes);
        return count(query);
    }

    @Override
    public Set<String> queryCodesByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda()
                .in(SysNftTypeEntity::getId, ids);
        List<SysNftTypeEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysNftTypeEntity::getCode).collect(Collectors.toSet());
    }

    @Override
    public List<SysNftTypeResp> queryRespByParentCode(String parentCode) {
        LambdaQueryWrapper<SysNftTypeEntity> query = new QueryWrapper<SysNftTypeEntity>().lambda();
        if (StringUtils.isEmpty(parentCode)) {
            query.isNull(SysNftTypeEntity::getParentCode);
        } else {
            query.eq(SysNftTypeEntity::getParentCode, parentCode);
        }
        query.orderByAsc(SysNftTypeEntity::getSort);
        return convertToResp(list(query));
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
        return TreeUtils.build(respList);
    }
}

