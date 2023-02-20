package com.seeds.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.game.dto.response.ServerRegionResp;
import com.seeds.game.entity.ServerRegionEntity;
import com.seeds.game.mapper.ServerRegionMapper;
import com.seeds.game.service.IServerRegionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏服对局记录 服务实现类
 * </p>
 *
 * @author hang.yu
 * @since 2023-02-13
 */
@Service
public class ServerRegionServiceImpl extends ServiceImpl<ServerRegionMapper, ServerRegionEntity> implements IServerRegionService {

    @Override
    public List<ServerRegionResp> queryList() {
        List<ServerRegionEntity> list = list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<ServerRegionResp> respList = new ArrayList<>();
        Map<Integer, List<ServerRegionEntity>> map = list.stream().collect(Collectors.groupingBy(ServerRegionEntity::getRegion));
        for (Integer key : map.keySet()) {
            List<ServerRegionResp.GameServer> serverList = new ArrayList<>();
            List<ServerRegionEntity> serverRegions = map.get(key);
            for (ServerRegionEntity serverRegion : serverRegions) {
                ServerRegionResp.GameServer gameServer = new ServerRegionResp.GameServer();
                gameServer.setServer(serverRegion.getGameServer());
                gameServer.setServerName(serverRegion.getGameServerName());
                serverList.add(gameServer);
            }
            ServerRegionResp resp = new ServerRegionResp();
            resp.setRegion(key);
            resp.setRegionName(serverRegions.get(0).getRegionName());
            resp.setGameServers(serverList);
            respList.add(resp);
        }
        return respList;
    }
}
