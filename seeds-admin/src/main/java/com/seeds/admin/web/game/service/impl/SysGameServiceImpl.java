package com.seeds.admin.web.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.common.SwitchReq;
import com.seeds.admin.dto.game.request.SysGameAddReq;
import com.seeds.admin.dto.game.request.SysGamePageReq;
import com.seeds.admin.dto.game.response.SysGameResp;
import com.seeds.admin.dto.merchant.request.SysMerchantModifyReq;
import com.seeds.admin.entity.game.SysGameEntity;
import com.seeds.admin.enums.SysStatusEnum;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.web.game.mapper.SysGameMapper;
import com.seeds.admin.web.game.service.SysGameService;
import com.seeds.admin.web.merchant.service.SysMerchantGameService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


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
        QueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<>();
        queryWrap.likeRight(StringUtils.isNotBlank(query.getName()), "name", query.getName());
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
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
    public List<SysGameResp> queryList(Long merchantId) {
        List<SysGameEntity> games;
        if (merchantId != null) {
            // 查询商家下的游戏
            Set<Long> gameIds = sysMerchantGameService.queryGameIdByMerchantId(merchantId);
            games = queryByIds(gameIds);
        } else {
            QueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<>();
            queryWrap.eq("delete_flag", WhetherEnum.NO.value());
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
        QueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        queryWrap.in("id", ids);
        return list(queryWrap);
    }

    @Override
    public SysGameEntity queryById(Long id) {
        QueryWrapper<SysGameEntity> queryWrap = new QueryWrapper<>();
        queryWrap.eq("id", id);
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
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
    public void modify(SysMerchantModifyReq req) {
        SysGameEntity sysGame = new SysGameEntity();
        BeanUtils.copyProperties(req, sysGame);
        updateById(sysGame);
    }

    @Override
    public void delete(Long id) {
        SysGameEntity sysGame = new SysGameEntity();
        sysGame.setId(id);
        sysGame.setDeleteFlag(WhetherEnum.YES.value());
        updateById(sysGame);
    }

    @Override
    public void enableOrDisable(List<SwitchReq> req) {
        List<SysGameEntity> sysGames = new ArrayList<>();
        req.forEach(p -> {
            // 校验状态
            SysStatusEnum.from(p.getStatus());
            SysGameEntity sysGame = new SysGameEntity();
            sysGame.setId(p.getId());
            sysGame.setStatus(p.getStatus());
            sysGames.add(sysGame);
        });
        updateBatchById(sysGames);
    }
}
