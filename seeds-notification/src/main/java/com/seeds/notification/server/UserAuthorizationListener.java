package com.seeds.notification.server;

import cn.hutool.extra.spring.SpringUtil;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.dto.redis.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 前端与websocket建立连接需要先校验token
 * 已经登录的用户才允许建立连接
 *
 * @author: hewei
 */
@Slf4j
public class UserAuthorizationListener implements AuthorizationListener {


    @Autowired
    private RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);

    @Override
    public boolean isAuthorized(HandshakeData data) {
        String token = data.getSingleUrlParam("token");
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        LoginUserDTO loginUserDTO = redissonClient.<LoginUserDTO>getBucket(UcRedisKeysConstant.getUcTokenKey(token)).get();
        if (null == loginUserDTO) {
            log.error("auth failed");
            return false;
        }
        log.info("auth success userId: {},userName: { }", loginUserDTO.getUserId(), loginUserDTO.getLoginName());
        return true;
    }

}
