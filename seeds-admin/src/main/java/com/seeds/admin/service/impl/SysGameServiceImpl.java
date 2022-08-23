package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
import com.seeds.admin.dto.response.SysGameBriefResp;
import com.seeds.admin.dto.response.SysGameResp;
import com.seeds.admin.entity.SysGameEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysGameMapper;
import com.seeds.admin.service.SysGameService;
import com.seeds.admin.service.SysMerchantGameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统游戏
 *
 * @author hang.yu
 * @date 2022/7/21
 */
@Service
public class SysGameServiceImpl extends ServiceImpl<SysGameMapper, SysGameEntity> implements SysGameService {

    @Autowired
    private SysMerchantGameService sysMerchantGameService;

    @Autowired
    private SysGameMapper sysGameMapper;

    @Override
    public IPage<SysGameResp> queryPage(SysGamePageReq query) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysGameEntity::getName, query.getName());
        Page<SysGameEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysGameEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysGameResp resp = new SysGameResp();
            BeanUtils.copyProperties(p, resp);
            // 图片
            resp.setPicture(p.getPictureUrl());
            return resp;
        });
    }

    @Override
    public List<SysGameResp> select(Long merchantId) {
        List<SysGameEntity> games = new ArrayList<>();
        if (merchantId != null) {
            // 查询商家下的游戏
            Set<Long> gameIds = sysMerchantGameService.queryGameIdByMerchantId(merchantId);
            if (!CollectionUtils.isEmpty(gameIds)) {
                games = listByIds(gameIds);
            }
        } else {
            games = list();
        }
        List<SysGameResp> respList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(games)) {
            games.forEach(p -> {
                SysGameResp resp = new SysGameResp();
                BeanUtils.copyProperties(p, resp);
                respList.add(resp);
            });
        }
        return respList;
    }

    @Override
    public SysGameEntity queryById(Long id) {
        return sysGameMapper.queryById(id);
    }

    @Override
    public void add(SysGameAddReq req) {
        SysGameEntity game = new SysGameEntity();
        BeanUtils.copyProperties(req, game);
        save(game);
    }

    @Override
    public SysGameResp detail(Long id) {
        SysGameEntity sysGame = getById(id);
        SysGameResp resp = new SysGameResp();
        if (sysGame != null) {
            BeanUtils.copyProperties(sysGame, resp);
            // 图片
            resp.setPicture(sysGame.getPictureUrl());
            // 视频
            resp.setVideo(sysGame.getVideoUrl());
        }
        return resp;
    }

    @Override
    public void modify(SysGameModifyReq req) {
        SysGameEntity sysGame = new SysGameEntity();
        BeanUtils.copyProperties(req, sysGame);
        updateById(sysGame);
    }

    @Override
    public void batchDelete(ListReq req) {
        // 删除游戏
        removeBatchByIds(req.getIds());
        // 删除商家和游戏的关联
        sysMerchantGameService.deleteByGameIds(req.getIds());
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysGameEntity sysGame = new SysGameEntity();
            sysGame.setId(p.getId());
            sysGame.setStatus(p.getStatus());
            updateById(sysGame);
        });
    }

    @Override
    public Map<Long, String> queryMapByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        // 被删除的游戏也需要展示
        List<SysGameEntity> sysGames = sysGameMapper.queryListByIds(ids);
        if (CollectionUtils.isEmpty(sysGames)) {
            return Collections.emptyMap();
        }
        return sysGames.stream().collect(Collectors.toMap(SysGameEntity::getId, SysGameEntity::getName));
    }

    @Override
    public SysGameEntity queryByOfficialUrl(String officialUrl) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(SysGameEntity::getOfficialUrl, officialUrl);
        return getOne(queryWrap);
    }

    @Override
    public IPage<SysGameBriefResp> dropdownPage(SysGamePageReq query) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysGameEntity::getName, query.getName())
                .eq(SysGameEntity::getStatus, WhetherEnum.YES.value());
        Page<SysGameEntity>  page = page(new Page<>(query.getCurrent(), query.getSize()), queryWrap);
        List<SysGameEntity> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysGameBriefResp resp = new SysGameBriefResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }
}

