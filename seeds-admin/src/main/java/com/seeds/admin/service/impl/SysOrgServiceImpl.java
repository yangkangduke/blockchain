package com.seeds.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SysOrgAddOrModifyReq;
import com.seeds.admin.dto.response.SysOrgResp;
import com.seeds.admin.entity.SysOrgEntity;
import com.seeds.admin.mapper.SysOrgMapper;
import com.seeds.admin.service.SysOrgService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hewei
 * @createDate 2022-07-22 14:49:34
 */
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrgEntity> implements SysOrgService {

    @Override
    public void add(SysOrgAddOrModifyReq req) {
        SysOrgEntity sysOrgEntity = new SysOrgEntity();
        BeanUtils.copyProperties(req, sysOrgEntity);
        // 生成唯一的组织id
        sysOrgEntity.setOrgId(IdUtil.createSnowflake(1, 1).nextId());
        save(sysOrgEntity);
    }

    @Override
    public void modify(SysOrgAddOrModifyReq req) {
        SysOrgEntity sysOrgEntity = new SysOrgEntity();
        BeanUtils.copyProperties(req, sysOrgEntity);
        updateById(sysOrgEntity);
    }

    @Override
    public void delete(ListReq req) {
        Set<Long> ids = req.getIds();
        removeBatchByIds(ids);
    }

    @Override
    public SysOrgResp detail(Long id) {
        SysOrgResp resp = new SysOrgResp();
        SysOrgEntity orgEntity = getById(id);
        if (orgEntity != null) {
            BeanUtils.copyProperties(orgEntity, resp);
            LambdaQueryWrapper<SysOrgEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysOrgEntity::getOrgId, orgEntity.getParentOrgId());
            SysOrgEntity one = getOne(queryWrapper);
            if (!ObjectUtils.isEmpty(one)) {
                resp.setParentOrgName(one.getOrgName());
            }
        }
        return resp;
    }

    @Override
    public SysOrgEntity queryByOrgId(Long orgId) {
        LambdaQueryWrapper<SysOrgEntity> queryWrap = new QueryWrapper<SysOrgEntity>().lambda()
                .eq(SysOrgEntity::getOrgId, orgId);
        return getOne(queryWrap);
    }

    @Override
    public List<SysOrgResp> queryRespList(String orgName) {
        List<SysOrgResp> orgResp = new ArrayList<>();
        LambdaQueryWrapper<SysOrgEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (!StrUtil.isEmpty(orgName)) {
            queryWrapper.like(SysOrgEntity::getOrgName, orgName);
        }
        queryWrapper.orderByDesc(SysOrgEntity::getCreatedAt);
        List<SysOrgEntity> orgList = list(queryWrapper);

        if (!CollectionUtils.isEmpty(orgList)) {
            orgResp = orgList.stream().map(item -> {
                SysOrgResp sysOrgResp = new SysOrgResp();
                BeanUtils.copyProperties(item, sysOrgResp);
                return sysOrgResp;
            }).collect(Collectors.toList());

            // 转成树结构
            orgResp = this.listWithTree(orgResp);
        }

        return orgResp;
    }

    @Override
    public Map<Long, String> queryNameByOrgIds(Collection<Long> orgIds) {
        LambdaQueryWrapper<SysOrgEntity> queryWrap = new QueryWrapper<SysOrgEntity>().lambda()
                .in(SysOrgEntity::getOrgId, orgIds);
        List<SysOrgEntity> list = list(queryWrap);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(SysOrgEntity::getOrgId, SysOrgEntity::getOrgName));
    }


    private List<SysOrgResp> listWithTree(List<SysOrgResp> list) {
        //组装树形结构
        return list.stream()
                .filter(t -> Long.compare(t.getParentOrgId(), 0) == 0)
                .peek((org) -> org.setChildren(this.getChildren(org, list)))
                .sorted(Comparator.comparingInt(a -> (a.getSort() == null ? 0 : a.getSort())))
                .collect(Collectors.toList());
    }

    /**
     * 递归查找当前组织的子组织
     * <p>
     * Long 类型不能直接用 == 比较，
     * Long 型比较最好用Long.compare(a,b) == 0, 或者a.longValue() = b.longValue(), 否则可能会有问题
     */
    private List<SysOrgResp> getChildren(SysOrgResp root, List<SysOrgResp> list) {

        return list.stream()
                .filter(t -> t.getParentOrgId().longValue() == root.getOrgId().longValue())
                .peek(i -> i.setChildren(this.getChildren(i, list)))
                .sorted(Comparator.comparingInt(a -> (a.getSort() == null ? 0 : a.getSort())))
                .collect(Collectors.toList());
    }


}




