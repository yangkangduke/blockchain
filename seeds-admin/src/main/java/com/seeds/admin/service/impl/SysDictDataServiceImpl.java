package com.seeds.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysDictDataResp;
import com.seeds.admin.entity.SysDictDataEntity;
import com.seeds.admin.mapper.SysDictDataMapper;
import com.seeds.admin.service.SysDictDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yang.deng
 * @date 2022-08-15
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictDataEntity> implements SysDictDataService {

    //查询
    @Override
    public SysDictDataEntity queryById(Long id) {
        LambdaQueryWrapper<SysDictDataEntity> queryWrap = new QueryWrapper<SysDictDataEntity>().lambda()
                .eq(SysDictDataEntity::getId, id);
        return getOne(queryWrap);
    }

    //新增
    @Override
    public void add(SysDictDataAddReq req) {
        SysDictDataEntity sysDictData = new SysDictDataEntity();
        BeanUtils.copyProperties(req, sysDictData);
        save(sysDictData);
    }

    //修改
    @Override
    public void modify(SysDictDataModifyReq req) {
        SysDictDataEntity sysDictData = new SysDictDataEntity();
        BeanUtils.copyProperties(req, sysDictData);
        updateById(sysDictData);
    }

    //删除
    @Override
    public void delete(ListReq req) {
        removeBatchByIds(req.getIds());
    }

    @Override
    public IPage<SysDictDataResp> queryPage(SysDictDataPageReq req) {

        LambdaQueryWrapper<SysDictDataEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(req.getDictLabel()), SysDictDataEntity::getDictValue, req.getDictLabel());
        Page<SysDictDataEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysDictDataEntity> records = page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        return page.convert(p -> {
            SysDictDataResp resp = new SysDictDataResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public SysDictDataResp detail(Long id) {
        SysDictDataResp resp = new SysDictDataResp();
        SysDictDataEntity entity =queryById(id);
        if (entity != null) {
            BeanUtils.copyProperties(entity, resp);
        }
        return resp;
    }

    @Override
    public SysDictDataEntity queryByDictTypeIdAndLabel(Long DictTypeId, String dictLable) {
        LambdaQueryWrapper<SysDictDataEntity> query = new QueryWrapper<SysDictDataEntity>().lambda()
                .eq(SysDictDataEntity::getDictTypeId, DictTypeId)
                .eq(SysDictDataEntity::getDictLabel, dictLable);
        return getOne(query);
    }

    @Override
    public List<SysDictDataResp> queryRespList(String name) {
        List<SysDictDataResp> dictDataResp = new ArrayList<>();
        LambdaQueryWrapper<SysDictDataEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isEmpty(name)) {
            queryWrapper.like(SysDictDataEntity::getDictLabel, name);
        }
        List<SysDictDataEntity> dictDataList = list(queryWrapper);

        if (!CollectionUtils.isEmpty(dictDataList)) {
            dictDataResp = dictDataList.stream().map(item -> {
                SysDictDataResp sysDictDataResp = new SysDictDataResp();
                BeanUtils.copyProperties(item, sysDictDataResp);
                return sysDictDataResp;
            }).collect(Collectors.toList());

            // 转成树结构
            dictDataResp = this.listWithTree(dictDataResp);
        }

        return dictDataResp;
    }
    private List<SysDictDataResp> listWithTree(List<SysDictDataResp> list) {
        //组装树形结构
        return list.stream()
                .filter(t -> Long.compare(t.getDictTypeId(), 0) == 0)
                .sorted(Comparator.comparingInt(a -> Math.toIntExact(a.getSort() == null ? 0 : (Long) a.getSort())))
                .collect(Collectors.toList());
    }
}
