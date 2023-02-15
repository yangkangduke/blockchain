package com.seeds.game.service.impl;

import com.seeds.game.dto.request.OpenHeroRecordReq;
import com.seeds.game.dto.request.OpenMatchRecordReq;
import com.seeds.game.entity.ServerRoleHeroStatisticsEntity;
import com.seeds.game.entity.ServerRoleStatisticsEntity;
import com.seeds.game.service.IServerRoleHeroStatisticsService;
import com.seeds.game.service.IServerRoleStatisticsService;
import com.seeds.game.service.ServerPushDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 游戏服推数据 服务类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-13
 */
@Service
public class ServerPushDataServiceImpl implements ServerPushDataService {

    @Autowired
    private IServerRoleStatisticsService serverRoleStatisticsService;

    @Autowired
    private IServerRoleHeroStatisticsService serverRoleHeroStatisticsService;

    @Override
    public void matchRecord(OpenMatchRecordReq matchRecord) {
        Long accId = Long.valueOf(matchRecord.getAccId());
        // data statistics
        ServerRoleStatisticsEntity roleStatistics = serverRoleStatisticsService.queryByRoleId(accId);
        if (roleStatistics == null) {
            roleStatistics = new ServerRoleStatisticsEntity();
            // 游戏角色id
            roleStatistics.setRoleId(accId);
            BeanUtils.copyProperties(matchRecord, roleStatistics);
            roleStatistics.setOverallScore(convertScoreToRating(matchRecord.getComprehensiveScore()));
            serverRoleStatisticsService.save(roleStatistics);
        } else {
            BeanUtils.copyProperties(matchRecord, roleStatistics);
            roleStatistics.setOverallScore(convertScoreToRating(matchRecord.getComprehensiveScore()));
            serverRoleStatisticsService.updateById(roleStatistics);
        }
    }

    @Override
    public void heroRecord(OpenHeroRecordReq heroRecordReq) {
        Long accId = Long.valueOf(heroRecordReq.getAccId());
        // common hero
        ServerRoleHeroStatisticsEntity roleHeroStatistics = serverRoleHeroStatisticsService.queryByRoleIdAndHeroId(accId, heroRecordReq.getHeroId());
        BigDecimal winRate = new BigDecimal(heroRecordReq.getWinNum()).divide(new BigDecimal(heroRecordReq.getFightNum()), 4, RoundingMode.HALF_UP);
        if (roleHeroStatistics == null) {
            roleHeroStatistics = new ServerRoleHeroStatisticsEntity();
            BeanUtils.copyProperties(heroRecordReq, roleHeroStatistics);
            roleHeroStatistics.setWinRate(winRate);
            roleHeroStatistics.setHeroId(heroRecordReq.getHeroId());
            roleHeroStatistics.setRoleId(accId);
            roleHeroStatistics.setProficiencyLvl(heroRecordReq.getFightNum() + heroRecordReq.getWinNum() * 2);
            serverRoleHeroStatisticsService.save(roleHeroStatistics);
        } else {
            BeanUtils.copyProperties(heroRecordReq, roleHeroStatistics);
            roleHeroStatistics.setWinRate(winRate);
            roleHeroStatistics.setProficiencyLvl(heroRecordReq.getFightNum() + heroRecordReq.getWinNum() * 2);
            serverRoleHeroStatisticsService.updateById(roleHeroStatistics);
        }
    }

    private String convertScoreToRating(Integer score) {
        if (score <= 20) {
            return "D";
        } else if (score <= 40) {
            return "C";
        } else if (score <= 60) {
            return "B";
        } else if (score <= 80) {
            return "A";
        } else if (score <= 90) {
            return "S";
        } else if (score <= 95) {
            return "SS";
        } else {
            return "SSS";
        }
    }
}
