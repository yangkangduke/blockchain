package com.seeds.game;


import cn.hutool.json.JSONUtil;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.game.dto.request.internal.DeleteReq;
import com.seeds.game.dto.request.internal.ServerRoleCreateUpdateReq;
import com.seeds.game.mq.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class GameCenterApplicationTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    void contextLoads() {

    }

    //新增游戏服角色信息
    @Test
    void insertServerRole() {
        ServerRoleCreateUpdateReq req = new ServerRoleCreateUpdateReq();
        req.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        req.setId(100100101L);
        req.setRegion(1);
        req.setGameServer(1);
        req.setName("testRole");
        req.setLevel(10);
        kafkaProducer.sendAsync(KafkaTopic.SERVER_ROLE_INSERT, JSONUtil.toJsonStr(req));
    }

    //更新游戏服角色信息
    @Test
    void updateServerRole() {
        ServerRoleCreateUpdateReq req = new ServerRoleCreateUpdateReq();
        req.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        req.setId(100100101L);
        req.setRegion(2);
        req.setGameServer(2);
        req.setName("testRole2");
        req.setLevel(12);
        kafkaProducer.sendAsync(KafkaTopic.SERVER_ROLE_UPDATE, JSONUtil.toJsonStr(req));
    }

    //删除游戏服角色信息
    @Test
    void deleteServerRole() {
        DeleteReq req = new DeleteReq();
        req.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        req.setId(1001001L);
        kafkaProducer.sendAsync(KafkaTopic.SERVER_ROLE_DELETE, JSONUtil.toJsonStr(req));
    }

}
