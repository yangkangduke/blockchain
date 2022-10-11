package com.seeds.admin.mq.consumer;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.mq.NftUpgradeMsgDTO;
import com.seeds.admin.dto.request.SysNftHonorModifyReq;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.admin.dto.mq.NftMintMsgDTO;
import com.seeds.admin.entity.SysNftEntity;
import com.seeds.admin.service.SysNftService;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 消费NFT保存成功消息，完成NFT上链操作
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "nft-consumer-group", topics = {KafkaTopic.NFT_SAVE_SUCCESS})
    public void mintNft(String msg) {
        log.info("收到消息：{}", msg);
        NftMintMsgDTO msgDTO = JSONUtil.toBean(msg, NftMintMsgDTO.class);
        nftService.mintNft(msgDTO);
    }

    /**
     * 消费NFT保存成功消息，完成NFT上链操作
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "nft-consumer-group", topics = {KafkaTopic.GAME_NFT_SAVE_SUCCESS})
    public void gameMintNft(String msg) {
        log.info("收到消息：{}", msg);
        NftMintMsgDTO msgDTO = JSONUtil.toBean(msg, NftMintMsgDTO.class);
        nftService.mintNft(msgDTO);
        // todo 通知游戏方NFT创建结果
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
