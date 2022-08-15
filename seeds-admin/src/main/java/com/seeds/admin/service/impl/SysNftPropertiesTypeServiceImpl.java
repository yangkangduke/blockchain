package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysNftPropertiesTypeAddReq;
import com.seeds.admin.dto.request.SysNftPropertiesTypePageReq;
import com.seeds.admin.dto.request.SysNftTypeModifyReq;
import com.seeds.admin.dto.response.SysNftPropertiesTypeResp;
import com.seeds.admin.entity.SysNftPropertiesTypeEntity;
import com.seeds.admin.mapper.SysNftPropertiesTypeMapper;
import com.seeds.admin.service.SysNftPropertiesTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * NFT属性
 *
 * @author hang.yu
 * @date 2022/8/13
 */
@Service
public class SysNftPropertiesTypeServiceImpl extends ServiceImpl<SysNftPropertiesTypeMapper, SysNftPropertiesTypeEntity> implements SysNftPropertiesTypeService {

    @Override
    public SysNftPropertiesTypeEntity queryByCode(String code) {
        LambdaQueryWrapper<SysNftPropertiesTypeEntity> query = new QueryWrapper<SysNftPropertiesTypeEntity>().lambda()
                .eq(SysNftPropertiesTypeEntity::getCode, code);
        return getOne(query);
    }

    @Override
    public void add(SysNftPropertiesTypeAddReq req) {
        SysNftPropertiesTypeEntity propertiesType = new SysNftPropertiesTypeEntity();
        BeanUtils.copyProperties(req, propertiesType);
        save(propertiesType);
    }

    @Override
    public void modify(SysNftTypeModifyReq req) {
        SysNftPropertiesTypeEntity propertiesType = new SysNftPropertiesTypeEntity();
        BeanUtils.copyProperties(req, propertiesType);
        updateById(propertiesType);
    }

    @Override
    public void batchDelete(ListReq req) {
        Set<Long> ids = req.getIds();
        removeBatchByIds(ids);
    }

    @Override
    public Map<Long, SysNftPropertiesTypeEntity> queryMapByIds(Collection<Long> ids) {
        List<SysNftPropertiesTypeEntity> list = listByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(SysNftPropertiesTypeEntity::getId, p -> p));
    }


    @Override
    public SysNftPropertiesTypeResp detail(Long id) {
        SysNftPropertiesTypeEntity propertiesType = getById(id);
        SysNftPropertiesTypeResp resp = new SysNftPropertiesTypeResp();
        if (propertiesType != null) {
            BeanUtils.copyProperties(propertiesType, resp);
        }
        return resp;
    }

    @Override
    public IPage<SysNftPropertiesTypeResp> queryPage(SysNftPropertiesTypePageReq query) {
        LambdaQueryWrapper<SysNftPropertiesTypeEntity> queryWrap = new QueryWrapper<SysNftPropertiesTypeEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysNftPropertiesTypeEntity::getName, query.getName());
        Page<SysNftPropertiesTypeEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysNftPropertiesTypeEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysNftPropertiesTypeResp resp = new SysNftPropertiesTypeResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }
}

