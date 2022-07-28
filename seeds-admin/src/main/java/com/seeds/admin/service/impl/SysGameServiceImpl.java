package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.*;
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

    @Override
    public IPage<SysGameResp> queryPage(SysGamePageReq query) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .likeRight(StringUtils.isNotBlank(query.getName()), SysGameEntity::getName, query.getName())
                .eq(SysGameEntity::getDeleteFlag, WhetherEnum.NO.value());
        Page<SysGameEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysGameEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        return page.convert(p -> {
            SysGameResp resp = new SysGameResp();
            BeanUtils.copyProperties(p, resp);
            return resp;
        });
    }

    @Override
    public List<SysGameResp> select(Long merchantId) {
        List<SysGameEntity> games;
        if (merchantId != null) {
            // 查询商家下的游戏
            Set<Long> gameIds = sysMerchantGameService.queryGameIdByMerchantId(merchantId);
            games = queryByIds(gameIds);
        } else {
            LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                    .eq(SysGameEntity::getDeleteFlag, WhetherEnum.NO.value());
            games = list(queryWrap);
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
    public List<SysGameEntity> queryByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(SysGameEntity::getDeleteFlag, WhetherEnum.NO.value())
                .in(SysGameEntity::getId, ids);
        return list(queryWrap);
    }

    @Override
    public SysGameEntity queryById(Long id) {
        LambdaQueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<SysGameEntity>().lambda()
                .eq(SysGameEntity::getId, id)
                .eq(SysGameEntity::getDeleteFlag, WhetherEnum.NO.value());
        return getOne(queryWrap);
    }

    @Override
    public void add(SysGameAddReq req) {
        SysGameEntity game = new SysGameEntity();
        BeanUtils.copyProperties(req, game);
        save(game);
    }

    @Override
    public SysGameResp detail(Long id) {
        SysGameEntity sysGame = queryById(id);
        SysGameResp resp = new SysGameResp();
        if (sysGame != null) {
            BeanUtils.copyProperties(sysGame, resp);
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
        req.getIds().forEach(p -> {
            SysGameEntity sysGame = new SysGameEntity();
            sysGame.setId(p);
            sysGame.setDeleteFlag(WhetherEnum.YES.value());
            updateById(sysGame);
        });
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
        List<SysGameEntity> sysGames = listByIds(ids);
        if (CollectionUtils.isEmpty(sysGames)) {
            return Collections.emptyMap();
        }
        return sysGames.stream().collect(Collectors.toMap(SysGameEntity::getId, SysGameEntity::getName));
    }
}

