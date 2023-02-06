package com.seeds.game.mq.listener;

import cn.hutool.json.JSONUtil;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.constant.redis.RedisKeys;
import com.seeds.game.dto.request.internal.NftPublicBackpackDisReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackReq;
import com.seeds.game.dto.request.internal.NftPublicBackpackTakeBackReq;
import com.seeds.game.enums.GameErrorCodeEnum;
import com.seeds.game.exception.GenericException;
import com.seeds.game.service.INftPublicBackpackService;
import com.seeds.uc.dto.redis.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author: hewei
 * @date 2023/2/3
 */

@Component
@Slf4j
public class NftBackpackListener {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private INftPublicBackpackService nftPublicBackpackService;

    /**
     * 消费 新增公共背包数据 消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.NFT_BACKPACK_INSERT})
    public void insert(String msg) {
        log.info("收到【新增公共背包数据】消息：{}", msg);
        NftPublicBackpackReq req = JSONUtil.toBean(msg, NftPublicBackpackReq.class);
        try {
            Long userId = this.checkUcToken(req.getUcToken());
            req.setUserId(userId);
            nftPublicBackpackService.create(req);
        } catch (Exception e) {
            log.info("消费【新增公共背包数据】消息，错误：{}", e.getMessage());
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    /**
     * 消费 更新公共背包数据 消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.NFT_BACKPACK_UPDATE})
    public void update(String msg) {
        log.info("收到【更新公共背包数据】消息：{}", msg);
        NftPublicBackpackReq req = JSONUtil.toBean(msg, NftPublicBackpackReq.class);
        try {
            Long userId = this.checkUcToken(req.getUcToken());
            req.setUserId(userId);
            nftPublicBackpackService.update(req);
        } catch (Exception e) {
            log.error("消费【更新公共背包数据】消息，错误：{}", e.getMessage());
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    /**
     * 消费 分发NFT 消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.NFT_BACKPACK_DISTRIBUTE})
    public void distribute(String msg) {

        log.info("收到【分发NFT】消息：{}", msg);
        NftPublicBackpackDisReq disReq = JSONUtil.toBean(msg, NftPublicBackpackDisReq.class);
        try {
            Long userId = this.checkUcToken(disReq.getUcToken());
            disReq.setUserId(userId);
            nftPublicBackpackService.distribute(disReq);
        } catch (Exception e) {
            log.error("消费【分发NFT】消息，错误：{}", e.getMessage());
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    /**
     * 消费 收回NFT 消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.NFT_BACKPACK_TAKE_BACK})
    public void takeBack(String msg) {

        log.info("收到【收回NFT】消息：{}", msg);
        NftPublicBackpackTakeBackReq takeBackReq = JSONUtil.toBean(msg, NftPublicBackpackTakeBackReq.class);
        try {
            Long userId = this.checkUcToken(takeBackReq.getUcToken());
            takeBackReq.setUserId(userId);
            nftPublicBackpackService.takeBack(takeBackReq);
        } catch (Exception e) {
            log.error("消费【收回NFT】消息，错误：{}", e.getMessage());
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    /**
     * 消费 转移NFT 消息
     *
     * @param msg 消息
     */
    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.NFT_BACKPACK_TRANSFER})
    public void transfer(String msg) {

        log.info("收到【转移NFT】消息：{}", msg);
        NftPublicBackpackDisReq disReq = JSONUtil.toBean(msg, NftPublicBackpackDisReq.class);
        try {
            Long userId = this.checkUcToken(disReq.getUcToken());
            disReq.setUserId(userId);
            nftPublicBackpackService.transfer(disReq);
        } catch (Exception e) {
            log.error("消费【转移NFT】消息，错误：{}", e.getMessage());
            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
        }
    }

    private Long checkUcToken(String ucToken) {
        if (Objects.isNull(ucToken)) {
            throw new GenericException(GameErrorCodeEnum.ERR_505_MISSING_UCTOKEN);
        }
        LoginUserDTO user = redissonClient.<LoginUserDTO>getBucket(RedisKeys.getUcTokenKey(ucToken)).get();
        if (user == null || user.getUserId() == null) {
            throw new GenericException(GameErrorCodeEnum.ERR_506_INVALID_UCTOKEN);
        }
        return user.getUserId();
    }
}
