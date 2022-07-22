package com.seeds.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.SysNftPageReq;
import com.seeds.admin.dto.response.SysNftResp;
import com.seeds.admin.entity.SysNftEntity;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.mapper.SysNftMapper;
import com.seeds.admin.service.SysGameService;
import com.seeds.admin.service.SysNftService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 系统NFT
 *
 * @author hang.yu
 * @date 2022/7/22
 */
@Service
public class SysNftServiceImpl extends ServiceImpl<SysNftMapper, SysNftEntity> implements SysNftService {

    @Autowired
    private SysGameService sysGameService;

    @Override
    public IPage<SysNftResp> queryPage(SysNftPageReq query) {
        QueryWrapper<SysNftEntity> queryWrap = new QueryWrapper<>();
        queryWrap.likeRight(StringUtils.isNotBlank(query.getName()), "name", query.getName());
        queryWrap.eq(query.getGameId() != null, "game_id", query.getGameId());
        queryWrap.eq(query.getGameId() != null, "status", query.getStatus());
        if (query.getNftTypeId() != null) {
            // todo 类别过滤
        }
        queryWrap.eq("delete_flag", WhetherEnum.NO.value());
        Page<SysNftEntity> page = new Page<>(query.getCurrent(), query.getSize());
        List<SysNftEntity> records = page(page, queryWrap).getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page.convert(p -> null);
        }
        Set<Long> gameIds = records.stream().map(SysNftEntity::getGameId).collect(Collectors.toSet());
        Map<Long, String> gameMap = sysGameService.queryMapByIds(gameIds);
        return page.convert(p -> {
            SysNftResp resp = new SysNftResp();
            BeanUtils.copyProperties(p, resp);
            resp.setGameName(gameMap.get(p.getGameId()));
            return resp;
        });
    }
}

