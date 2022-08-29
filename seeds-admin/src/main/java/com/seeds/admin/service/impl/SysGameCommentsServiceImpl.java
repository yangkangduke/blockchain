package com.seeds.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.ListReq;
import com.seeds.admin.dto.request.SwitchReq;
import com.seeds.admin.dto.request.SysGameCommentsAddOrModifyReq;
import com.seeds.admin.dto.request.SysGameCommentsPageReq;
import com.seeds.admin.dto.response.SysGameCommentsResp;
import com.seeds.admin.entity.SysGameCommentsEntity;
import com.seeds.admin.enums.AdminErrorCodeEnum;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.exceptions.GenericException;
import com.seeds.admin.mapper.SysGameCommentsMapper;
import com.seeds.admin.service.SysGameCommentsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author hewei
 * @date 2022-08-08 15:14:11
 */
@Service
public class SysGameCommentsServiceImpl extends ServiceImpl<SysGameCommentsMapper, SysGameCommentsEntity> implements SysGameCommentsService {

    @Override
    public IPage<SysGameCommentsResp> queryPage(SysGameCommentsPageReq req) {

        LambdaQueryWrapper<SysGameCommentsEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.isNotBlank(req.getKeyWords()), SysGameCommentsEntity::getComments, req.getKeyWords())
                .eq(!ObjectUtils.isEmpty(req.getGameId()), SysGameCommentsEntity::getGameId, req.getGameId())
                .eq(!ObjectUtils.isEmpty(req.getStatus()), SysGameCommentsEntity::getStatus, req.getStatus());

        Page<SysGameCommentsEntity> page = new Page<>(req.getCurrent(), req.getSize());
        List<SysGameCommentsEntity> records = page(page, wrapper).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }

        return page.convert(p -> {
            SysGameCommentsResp resp = new SysGameCommentsResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public void add(SysGameCommentsAddOrModifyReq req) {

        LambdaQueryWrapper<SysGameCommentsEntity> wrapper = new QueryWrapper<SysGameCommentsEntity>().lambda()
                .eq(SysGameCommentsEntity::getGameId, req.getGameId())
                .eq(SysGameCommentsEntity::getUcUserId, req.getUcUserId());

        SysGameCommentsEntity one = getOne(wrapper);
        if (null != one) {
            // 用户已经评价过该游戏
            throw new GenericException(AdminErrorCodeEnum.ERR_80001_GAME_COMMENTS_ALREADY_EXIST);
        }

        SysGameCommentsEntity entity = new SysGameCommentsEntity();
        BeanUtils.copyProperties(req, entity);
        entity.setCommentsTime(new Date().getTime());
        save(entity);
    }

    @Override
    public SysGameCommentsResp detail(Long id) {
        SysGameCommentsResp resp = new SysGameCommentsResp();
        SysGameCommentsEntity entity = getById(id);
        if (!ObjectUtils.isEmpty(entity)) {
            BeanUtils.copyProperties(entity, resp);
        }
        return resp;
    }

    @Override
    public void modify(SysGameCommentsAddOrModifyReq req) {
        SysGameCommentsEntity entity = new SysGameCommentsEntity();
        BeanUtils.copyProperties(req, entity);
        updateById(entity);
    }

    @Override
    public void delete(ListReq req) {
        Set<Long> ids = req.getIds();
        removeBatchByIds(ids);
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {

        if (CollectionUtils.isEmpty(req)) {
            return;
        }
        req.forEach(item -> {
            SysStatusEnum.from(item.getStatus());
            SysGameCommentsEntity entity = new SysGameCommentsEntity();
            entity.setId(item.getId());
            entity.setStatus(item.getStatus());
            updateById(entity);
        });
    }
}




