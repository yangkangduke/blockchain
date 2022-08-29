package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysDictTypeAddReq;
import com.seeds.admin.dto.request.SysDictTypeModifyReq;
import com.seeds.admin.dto.response.SysDictTypeResp;
import com.seeds.admin.entity.SysDictTypeEntity;
import com.seeds.admin.mapper.SysDictTypeMapper;
import com.seeds.admin.service.SysDictTypeService;
import com.seeds.admin.utils.TreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 系统用户
 * @author hang.yu
 * @date 2022/7/13
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictTypeEntity> implements SysDictTypeService {


    @Override
    public List queryRespList(String name) {
        LambdaQueryWrapper<SysDictTypeEntity> query = new QueryWrapper<SysDictTypeEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(name), SysDictTypeEntity::getDictName, name)
                .orderByAsc(SysDictTypeEntity::getSort);
        return convertToResp(list(query));
    }

    //查询
    @Override
    public SysDictTypeEntity queryById(Long id) {
        LambdaQueryWrapper<SysDictTypeEntity> queryWrap = new QueryWrapper<SysDictTypeEntity>().lambda()
                .eq(SysDictTypeEntity::getId, id);
        return getOne(queryWrap);
    }

    //新增
    @Override
    public void add(SysDictTypeAddReq req) {
        SysDictTypeEntity name = new SysDictTypeEntity();
        BeanUtils.copyProperties(req, name);
        save(name);
    }

    //修改
    @Override
    public void modify(SysDictTypeModifyReq req) {
        SysDictTypeEntity sysDictType = new SysDictTypeEntity();
        BeanUtils.copyProperties(req, sysDictType);
        updateById(sysDictType);
    }

    //删除用户
    @Override
    public void delete(ListReq req) {
        removeBatchByIds(req.getIds());
    }

    @Override
    public SysDictTypeResp detail(Long id) {
        SysDictTypeResp resp = new SysDictTypeResp();
        SysDictTypeEntity dictType = queryById(id);
        if (dictType != null) {
            BeanUtils.copyProperties(dictType, resp);
        }
        return resp;
    }


    @Override
    public  SysDictTypeEntity queryByTypeCode(String code){
        LambdaQueryWrapper<SysDictTypeEntity> query = new QueryWrapper<SysDictTypeEntity>().lambda()
                .eq(SysDictTypeEntity::getDictCode, code);
        return getOne(query);
    }

    @Override
    public Long countKidsByCodes(Set<String> codes) {
        LambdaQueryWrapper<SysDictTypeEntity> query = new QueryWrapper<SysDictTypeEntity>().lambda()
                .in(SysDictTypeEntity::getParentCode, codes);
        return count(query);
    }

    @Override
    public Set<String> queryCodesByIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        LambdaQueryWrapper<SysDictTypeEntity> query = new QueryWrapper<SysDictTypeEntity>().lambda()
                .in(SysDictTypeEntity::getId, ids);
        List<SysDictTypeEntity> list = list(query);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream().map(SysDictTypeEntity::getDictCode).collect(Collectors.toSet());
    }
    private List convertToResp(List<SysDictTypeEntity> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysDictTypeResp> respList = new ArrayList<>();
        for (SysDictTypeEntity dictType : list) {
            SysDictTypeResp resp = new SysDictTypeResp();
            BeanUtils.copyProperties(dictType, resp);
            respList.add(resp);
        }

        // 根节点为空
        return TreeUtils.build(respList);
    }
}