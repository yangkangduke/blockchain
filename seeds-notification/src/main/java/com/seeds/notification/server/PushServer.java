package com.seeds.notification.server;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.seeds.notification.dto.NoticeDTO;
import com.seeds.notification.enums.NoticeErrorCodeEnum;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
public class PushServer {

    @Autowired
    private RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class); // 直接使用 @Autowired 无法注入redissonClient

    @Autowired
    private ClientCache clientCache = SpringUtil.getBean(ClientCache.class);

    public static final PushServer pushServer = new PushServer();
    private SocketIOServer server;

    private PushServer() {

        final Configuration configuration = new Configuration();
        configuration.setAuthorizationListener(new UserAuthorizationListener());
        configuration.setPort(8899);
        configuration.setOrigin("http://127.0.0.1");

        // 可重用地址，防止处于TIME_WAIT的socket影响服务启动
        final SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        configuration.setSocketConfig(socketConfig);
        server = new SocketIOServer(configuration);


        server.addConnectListener(client -> {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            LoginUserDTO loginUserDTO = redissonClient.<LoginUserDTO>getBucket(UcRedisKeysConstant.getUcTokenKey(token)).get();
            if (null == loginUserDTO) {
                throw new GenericException(NoticeErrorCodeEnum.ERR_1001_WEBSOCKET_AUTH_FAILED.getDescEn());
            }
            Long userId = loginUserDTO.getUserId();
            UUID sessionId = client.getSessionId();

            if (null == userId) {
                client.disconnect();
            }
            clientCache.saveClient(userId, client);
            log.info("Connection established successfully，userid:{},sessionId:{}", userId, sessionId);
        });

        // 客户端断开监听器
        server.addDisconnectListener(client -> {
            clientCache.deleteSessionClient(client);
            client.disconnect();
            log.info("Connection Disconnected", client);
        });

    }

    /**
     * 向指定客户端推送消息
     *
     * @param notice
     */
    public void push(NoticeDTO notice) {
        String json = JSONUtil.toJsonStr(notice);
        SocketIOClient userClient = clientCache.getUserClient(notice.getUcUserId());
        try {
            if (userClient != null) {
                userClient.sendEvent("new-notice", json);
            }
        } catch (Exception e) {
            log.error("push message failed", e);
        }
    }

    /**
     * 同步启动服务；
     */
    public void start() {
        try {
            server.start();
            log.info("Push server started ");
        } catch (Exception e) {
            log.error("Push server start failed!", e);
            System.exit(-1);
        }
    }

    /**
     * 停止服务
     */
    public void stop() {
        server.stop();
    }


}
