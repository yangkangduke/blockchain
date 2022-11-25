package com.seeds.admin.mq.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.mq.NftMintMsgDTO;
import com.seeds.admin.dto.mq.NftUpgradeMsgDTO;
import com.seeds.admin.dto.request.SysNftHonorModifyReq;
import com.seeds.admin.entity.SysNftEntity;
import com.seeds.admin.service.SysGameApiService;
import com.seeds.admin.service.SysNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.enums.ApiType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消费者
 *
 * @author hewei
 */
@Component
@Slf4j
public class NFTConsumer {

    @Resource
    private SysNftService nftService;

    @Autowired
    private SysGameApiService sysGameApiService;

    /**
     * 消费NFT保存成功消息，完成NFT上链操作
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "nft-consumer-group", topics = {KafkaTopic.GAME_NFT_SAVE_SUCCESS})
    public void gameMintNft(String msg) {
        log.info("收到消息：{}", msg);
        NftMintMsgDTO msgDTO = JSONUtil.toBean(msg, NftMintMsgDTO.class);
        Boolean result = nftService.mintNft(msgDTO);
        if (StringUtils.isNotBlank(msgDTO.getCallbackUrl()) && result) {
            // 通知游戏方NFT创建结果
            String notificationApi = sysGameApiService.queryApiByGameAndType(msgDTO.getGameId(), ApiType.NFT_NOTIFICATION.getCode());
            String notificationUrl = msgDTO.getCallbackUrl() + notificationApi;
            JSONObject param = new JSONObject();
            param.putOnce("nft_id", msgDTO.getId());
            param.putOnce("acc_id", msgDTO.getOwnerId());
            log.info("开始请求游戏nft生效通知， param:{}", param);
            HttpResponse response = HttpRequest.post(notificationUrl)
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .body(String.valueOf(param))
                    .timeout(5 * 60 * 1000)
                    .execute();
            String body = response.body();
            log.info("请求游戏nft生效通知返回，result:{}",body);
        }
    }

    /**
     * 消费NFT删除成功消息，完成NFT burn操作
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "nft-consumer-group", topics = {KafkaTopic.NFT_DELETE_SUCCESS})
    public void burnNft(String msg) {
        log.info("收到消息：{}", msg);
        List<SysNftEntity> sysNftEntities = JSONUtil.toList(JSONUtil.parseArray(msg), SysNftEntity.class);
        nftService.burnNft(sysNftEntities);
    }

    /**
     * 消费NFT战绩记录更新消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "nft-consumer-group", topics = {KafkaTopic.GAME_NFT_HONOR_MODIFY})
    public void honorModify(String msg) {
        log.info("收到消息：{}", msg);
        nftService.honorModify(JSONUtil.toList(msg, SysNftHonorModifyReq.class));
    }

    /**
     * 消费NFT升级消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "nft-consumer-group", topics = {KafkaTopic.NFT_UPGRADE_SUCCESS})
    public void upgrade(String msg) {
        log.info("收到消息：{}", msg);
        nftService.upgrade(JSONUtil.toBean(msg, NftUpgradeMsgDTO.class));
    }

}
