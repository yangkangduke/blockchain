package com.seeds.notification.server;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.seeds.notification.dto.NotificationDto;
import com.seeds.notification.enums.NoticeErrorCodeEnum;
import com.seeds.notification.server.util.ServerConfigUtil;
import com.seeds.uc.constant.UcRedisKeysConstant;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PushServer {
    @Autowired
    private ClientCache clientCache = SpringUtil.getBean(ClientCache.class);

    @Autowired
    private ServerConfigUtil serverConfig = SpringUtil.getBean(ServerConfigUtil.class);

    public static final PushServer pushServer = new PushServer();

    private SocketIOServer server;

    private PushServer() {

        final Configuration configuration = new Configuration();
        configuration.setAuthorizationListener(new UserAuthorizationListener());
        configuration.setPort(serverConfig.getWebSocketPort());
        configuration.setOrigin(serverConfig.getWebSocketOrigin());
        configuration.setHostname(serverConfig.getHost());
        configuration.setPingInterval(serverConfig.getPingInterval());
        configuration.setPingTimeout(serverConfig.getPingTimeout());

        // 可重用地址，防止处于TIME_WAIT的socket影响服务启动
        final SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        configuration.setSocketConfig(socketConfig);
        server = new SocketIOServer(configuration);


        server.addConnectListener(client -> {
            Long userId = Long.parseLong(client.getHandshakeData().getSingleUrlParam("userId"));
            UUID sessionId = client.getSessionId();
            if (null == userId) {
                client.disconnect();
            }
            clientCache.saveClient(userId, sessionId, client);
            log.info("Connection established successfully，userid:{},sessionId:{}", userId, sessionId);
        });

        // 客户端断开监听器
        server.addDisconnectListener(client -> {

            Long userId = Long.parseLong(client.getHandshakeData().getSingleUrlParam("userId"));

            clientCache.deleteSessionClient(userId, client.getSessionId());
            client.disconnect();
            log.info("disconnected....", client);
        });

    }

    /**
     * 向指定客户端推送消息
     *
     * @param notice
     */
    public void push(NotificationDto notice) {
        String json = JSONUtil.toJsonStr(notice);
        List<Long> ucUserIds = notice.getReceivers();
        //loop to send messages to all connected users
        for (Long userId : ucUserIds) {
            HashMap<UUID, SocketIOClient> userClient = clientCache.getUserClient(userId);
            try {
                if (userClient != null) {
                    userClient.forEach((uuid, socketIOClient) -> {
                        socketIOClient.sendEvent("new-notice", json);
                        log.info("push message to user:{} successfully", userId);
                    });
                }
            } catch (Exception e) {
                log.error("push message failed", e);
            }
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
