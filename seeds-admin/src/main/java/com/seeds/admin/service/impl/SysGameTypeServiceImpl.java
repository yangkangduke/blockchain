package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysGameTypeAddReq;
import com.seeds.admin.dto.request.SysGameTypeModifyReq;
import com.seeds.admin.dto.response.SysGameTypeResp;
import com.seeds.admin.entity.SysGameTypeEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.mapper.SysGameTypeMapper;
import com.seeds.admin.service.SysGameTypeService;
import com.seeds.admin.utils.TreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统NFT类别
 *
 * @author hang.yu
 * @date 2022/8/25
 */
@Service
public class SysGameTypeServiceImpl extends ServiceImpl<SysGameTypeMapper, SysGameTypeEntity> implements SysGameTypeService {

    @Autowired
    private SysGameTypeMapper sysGameTypeMapper;

    @Override
    public Map<Long, SysGameTypeEntity> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        // 包含已删除的数据
        List<SysGameTypeEntity> list = sysGameTypeMapper.queryListByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(SysGameTypeEntity::getId, p -> p));
    }

    @Override
    public SysGameTypeEntity queryById(Long id) {
        return sysGameTypeMapper.queryById(id);
    }

    @Override
    public List<SysGameTypeResp> queryRespList(String name) {
        LambdaQueryWrapper<SysGameTypeEntity> query = new QueryWrapper<SysGameTypeEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(name), SysGameTypeEntity::getName, name)
                .orderByAsc(SysGameTypeEntity::getSort);
        return convertToResp(list(query));
    }

    @Override
    public void add(SysGameTypeAddReq req) {
        SysGameTypeEntity gameType = new SysGameTypeEntity();
        BeanUtils.copyProperties(req, gameType);
        save(gameType);
    }

    @Override
    public SysGameTypeResp detail(Long id) {
        SysGameTypeResp resp = new SysGameTypeResp();
        SysGameTypeEntity gameType = getById(id);
        if (gameType != null) {
            BeanUtils.copyProperties(gameType, resp);
        }
        return resp;
    }

    @Override
    public void modify(SysGameTypeModifyReq req) {
        SysGameTypeEntity gameType = new SysGameTypeEntity();
        BeanUtils.copyProperties(req, gameType);
        updateById(gameType);
    }

    @Override
    public SysGameTypeEntity queryByTypeCode(String code) {
        LambdaQueryWrapper<SysGameTypeEntity> query = new QueryWrapper<SysGameTypeEntity>().lambda()
                .eq(SysGameTypeEntity::getCode, code);
        return getOne(query);
    }

    @Override
    public void batchDelete(ListReq req) {
        Set<Long> ids = req.getIds();
        // 删除游戏类别
        removeBatchByIds(ids);
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        // 停用/启用游戏类别
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysGameTypeEntity gameType = new SysGameTypeEntity();
            gameType.setId(p.getId());
            gameType.setStatus(p.getStatus());
            updateById(gameType);
        });
    }

    @Override
    public Long countKidsByCodes(Set<String> codes) {
        LambdaQueryWrapper<SysGameTypeEntity> query = new QueryWrapper<SysGameTypeEntity>().lambda()
                .in(SysGameTypeEntity::getParentCode, codes);
        return count(query);
    }

    @Override
    public Set<String> queryCodesByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        LambdaQueryWrapper<SysGameTypeEntity> query = new QueryWrapper<SysGameTypeEntity>().lambda()
                .in(SysGameTypeEntity::getId, ids);
        List<SysGameTypeEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysGameTypeEntity::getCode).collect(Collectors.toSet());
    }

    @Override
    public List<SysGameTypeResp> queryRespByParentCode(String parentCode) {
        LambdaQueryWrapper<SysGameTypeEntity> query = new QueryWrapper<SysGameTypeEntity>().lambda();
        if (StringUtils.isEmpty(parentCode)) {
            query.isNull(SysGameTypeEntity::getParentCode);
        } else {
            query.eq(SysGameTypeEntity::getParentCode, parentCode);
        }
        query.orderByAsc(SysGameTypeEntity::getSort);
        return convertToResp(list(query));
    }

    private List<SysGameTypeResp> convertToResp(List<SysGameTypeEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysGameTypeResp> respList = new ArrayList<>();
        for (SysGameTypeEntity nftType : list) {
            SysGameTypeResp resp = new SysGameTypeResp();
            BeanUtils.copyProperties(nftType, resp);
            respList.add(resp);
        }
        // 根节点为空
        return TreeUtils.build(respList);
    }
}

