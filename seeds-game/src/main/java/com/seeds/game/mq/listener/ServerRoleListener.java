//package com.seeds.game.mq.listener;
//
//import cn.hutool.json.JSONUtil;
//import com.seeds.common.constant.mq.KafkaTopic;
//import com.seeds.common.constant.redis.RedisKeys;
//import com.seeds.game.dto.request.internal.DeleteReq;
//import com.seeds.game.dto.request.internal.ServerRoleCreateUpdateReq;
//import com.seeds.game.enums.GameErrorCodeEnum;
//import com.seeds.game.exception.GenericException;
//import com.seeds.game.service.IServerRoleService;
//import com.seeds.uc.dto.redis.LoginUserDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
///**
// * @author: hewei
// * @date 2023/2/3
// */
//
//@Component
//@Slf4j
//public class ServerRoleListener {
//
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Autowired
//    private IServerRoleService serverRoleService;
//
//    /**
//     * 消费 新增游戏服角色 消息
//     *
//     * @param msg 消息
//     */
//    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.SERVER_ROLE_INSERT})
//    public void insertServerRole(String msg) {
//        log.info("收到【新增游戏服角色】消息：{}", msg);
//        ServerRoleCreateUpdateReq req = JSONUtil.toBean(msg, ServerRoleCreateUpdateReq.class);
//        try {
//            Long userId = this.checkUcToken(req.getUcToken());
//            req.setUserId(userId);
//            serverRoleService.createRole(req);
//        } catch (Exception e) {
//            log.error("消费【新增游戏服角色】消息，错误：{}", e.getMessage());
//            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
//        }
//    }
//
//    /**
//     * 消费 更新游戏服角色 消息
//     *
//     * @param msg 消息
//     */
//    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.SERVER_ROLE_UPDATE})
//    public void updateServerRole(String msg) {
//        log.info("收到【更新游戏服角色】消息：{}", msg);
//        ServerRoleCreateUpdateReq req = JSONUtil.toBean(msg, ServerRoleCreateUpdateReq.class);
//        try {
//            Long userId = this.checkUcToken(req.getUcToken());
//            req.setUserId(userId);
//            serverRoleService.updateRole(req);
//        } catch (Exception e) {
//            log.error("消费【更新游戏服角色】消息，错误：{}", e.getMessage());
//            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
//        }
//    }
//
//    /**
//     * 消费 删除游戏服角色 消息
//     *
//     * @param msg 消息
//     */
//    @KafkaListener(groupId = "#{groupIdGenerator.randomId()}", topics = {KafkaTopic.SERVER_ROLE_DELETE})
//    public void deleteServerRole(String msg) {
//
//        log.info("收到【删除游戏服角色】消息：{}", msg);
//        DeleteReq req = JSONUtil.toBean(msg, DeleteReq.class);
//        try {
//            Long userId = this.checkUcToken(req.getUcToken());
//            req.setUserId(userId);
//            serverRoleService.delete(req);
//        } catch (Exception e) {
//            log.error("消费【删除游戏服角色】消息，错误：{}", e.getMessage());
//            throw new GenericException(GameErrorCodeEnum.ERR_500_SYSTEM_BUSY);
//        }
//    }
//
//    private Long checkUcToken(String ucToken) {
//        if (Objects.isNull(ucToken)) {
//            throw new GenericException(GameErrorCodeEnum.ERR_505_MISSING_UCTOKEN);
//        }
//        LoginUserDTO user = redissonClient.<LoginUserDTO>getBucket(RedisKeys.getUcTokenKey(ucToken)).get();
//        if (user == null || user.getUserId() == null) {
//            throw new GenericException(GameErrorCodeEnum.ERR_506_INVALID_UCTOKEN);
//        }
//        return user.getUserId();
//    }
//}
