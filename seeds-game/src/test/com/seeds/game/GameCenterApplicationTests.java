package com.seeds.game;


import cn.hutool.json.JSONUtil;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.game.dto.request.OpenNftPublicBackpackCreateUpdateReq;
import com.seeds.game.dto.request.internal.DeleteReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
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
        req.setId(100100101L);
        kafkaProducer.sendAsync(KafkaTopic.SERVER_ROLE_DELETE, JSONUtil.toJsonStr(req));
    }

    // 新增公共背包数据
    @Test
    void insertBackpack() {
        OpenNftPublicBackpackCreateUpdateReq req = new OpenNftPublicBackpackCreateUpdateReq();
        req.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        req.setName("测试皮肤");
        req.setDesc("testDesc");
        req.setIcon("testIcon");
        req.setImage("testImage");
        req.setType(3);
        req.setItemId(1001L);
        req.setAutoId(1001001001);
        req.setServerRoleId(100100L);
        req.setContractAddress("0xxabaa...");
        req.setChain("sonala");
        req.setTokenId("1");
        req.setTokenStandard("testS");
        req.setIsConfiguration(0);
        req.setState(0);
        req.setAttributes("{\"attr\": \"动态属性\"}");
        kafkaProducer.sendAsync(KafkaTopic.NFT_BACKPACK_INSERT, JSONUtil.toJsonStr(req));
    }

    @Test
    void updateBackpack() {
        OpenNftPublicBackpackCreateUpdateReq req = new OpenNftPublicBackpackCreateUpdateReq();
        req.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        req.setId(51L);
        req.setName("测试皮肤1");
        req.setDesc("testDesc1");
        req.setIcon("testIcon1");
        req.setImage("testImage1");
        req.setType(3);
        req.setItemId(1001L);
        req.setAutoId(1001001001);
        req.setServerRoleId(100100L);
        req.setContractAddress("0xxabaa1...");
        req.setChain("sonala1");
        req.setTokenId("1");
        req.setTokenStandard("testS");
        req.setIsConfiguration(0);
        req.setState(0);
        req.setAttributes("{\"attr\": \"动态属性1\"}");
        kafkaProducer.sendAsync(KafkaTopic.NFT_BACKPACK_UPDATE, JSONUtil.toJsonStr(req));
    }

    // 分发
    @Test
    void distribute() {
        NftPublicBackpackDisReq disReq = new NftPublicBackpackDisReq();
        disReq.setId(51L);
        disReq.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        disReq.setServerRoleId(100100100L);
        kafkaProducer.sendAsync(KafkaTopic.NFT_BACKPACK_DISTRIBUTE, JSONUtil.toJsonStr(disReq));
    }


    // 收回
    @Test
    void tackBack() {
        NftPublicBackpackTakeBackReq takeBackReq = new NftPublicBackpackTakeBackReq();
        takeBackReq.setId(51L);
        takeBackReq.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        kafkaProducer.sendAsync(KafkaTopic.NFT_BACKPACK_TAKE_BACK, JSONUtil.toJsonStr(takeBackReq));
    }

    // 转移
    @Test
    void transfer() {
        NftPublicBackpackDisReq disReq = new NftPublicBackpackDisReq();
        disReq.setId(51L);
        disReq.setUcToken("68fe8281f4bd6c49e58ffb76343f56633eff0888");
        disReq.setServerRoleId(100100100L);
        kafkaProducer.sendAsync(KafkaTopic.NFT_BACKPACK_TRANSFER, JSONUtil.toJsonStr(disReq));
    }

}
