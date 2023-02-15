package com.seeds.game.service.impl;

import com.seeds.game.dto.response.ServerRoleHeroStatisticsResp;
import com.seeds.game.dto.response.ServerRoleStatisticsResp;
import com.seeds.game.entity.ServerRoleEntity;
import com.seeds.game.entity.ServerRoleHeroStatisticsEntity;
import com.seeds.game.entity.ServerRoleStatisticsEntity;
import com.seeds.game.service.IServerRoleHeroStatisticsService;
import com.seeds.game.service.IServerRoleService;
import com.seeds.game.service.IServerRoleStatisticsService;
import com.seeds.game.service.ServerStatisticsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 游戏服统计 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-08
 */
@Service
public class ServerStatisticsServiceImpl implements ServerStatisticsService {

    @Autowired
    private IServerRoleService serverRoleService;

    @Autowired
    private IServerRoleStatisticsService serverRoleStatisticsService;

    @Autowired
    private IServerRoleHeroStatisticsService serverRoleHeroStatisticsService;

    @Override
    public ServerRoleStatisticsResp roleData(Long currentUserId, Integer region, Integer server) {
        ServerRoleStatisticsResp resp = new ServerRoleStatisticsResp();
        ServerRoleEntity serverRole = serverRoleService.queryByUserIdAndRegionAndServer(currentUserId, region, server);
        if (serverRole == null) {
            return resp;
        }
        ServerRoleStatisticsEntity roleStatistics = serverRoleStatisticsService.queryByRoleId(serverRole.getId());
        if (roleStatistics == null) {
            return resp;
        }
        BeanUtils.copyProperties(roleStatistics, resp);
        resp.setWinRate(roleStatistics.getWinRate().scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP) + "%");
        return resp;
    }

    @Override
    public List<ServerRoleHeroStatisticsResp> roleHeroData(Long currentUserId, Integer region, Integer server) {
        List<ServerRoleHeroStatisticsResp> respList = new ArrayList<>();
        ServerRoleEntity serverRole = serverRoleService.queryByUserIdAndRegionAndServer(currentUserId, region, server);
        if (serverRole == null) {
            return respList;
        }
        List<ServerRoleHeroStatisticsEntity> roleHeroStatistics = serverRoleHeroStatisticsService.queryByRoleId(serverRole.getId());
        if (CollectionUtils.isEmpty(roleHeroStatistics)) {
            return respList;
        }
        roleHeroStatistics.forEach(p -> {
            ServerRoleHeroStatisticsResp resp = new ServerRoleHeroStatisticsResp();
            BeanUtils.copyProperties(p, resp);
            resp.setWinRate(p.getWinRate().scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP) + "%");
            respList.add(resp);
        });
        return respList;
    }
}
