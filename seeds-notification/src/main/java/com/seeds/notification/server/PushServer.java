package com.seeds.notification.server;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.seeds.common.enums.TargetSource;
import com.seeds.notification.dto.NotificationDto;
import com.seeds.notification.server.util.ServerConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
            String source = client.getHandshakeData().getSingleUrlParam("source");
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            UUID sessionId = client.getSessionId();
            if (StringUtils.isEmpty(userId)) {
                client.disconnect();
            }
            String sourceUserId;
            if (String.valueOf(TargetSource.ADMIN.getCode()).equals(source)) {
                sourceUserId = TargetSource.ADMIN.name() + "|" + userId;
            } else {
                // UC端
                sourceUserId = TargetSource.UC.name() + "|" + userId;
            }

            clientCache.saveClient(sourceUserId, sessionId, client);
            log.info("Connection established successfully，sourceUserId:{},sessionId:{}", sourceUserId, sessionId);
        });

        // 客户端断开监听器
        server.addDisconnectListener(client -> {
            String sourceUserId;
            String source = client.getHandshakeData().getSingleUrlParam("source");
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            if (String.valueOf(TargetSource.ADMIN.getCode()).equals(source)) {
                sourceUserId = TargetSource.ADMIN.name() + "|" + userId;
            } else {
                // UC端
                sourceUserId = TargetSource.UC.name() + "|" + userId;
            }
            clientCache.deleteSessionClient(sourceUserId, client.getSessionId());
            client.disconnect();
            log.info("disconnected....sourceUserId:{}", sourceUserId);
        });

    }

    /**
     * 向指定客户端推送消息
     *
     * @param notice
     */
    public void push(NotificationDto notice, String userSource) {
        String json = JSONUtil.toJsonStr(notice);
        List<Long> ucUserIds = notice.getReceivers();
        //loop to send messages to all connected users
        for (Long userId : ucUserIds) {
            String sourceUserId = userSource + "|" + userId;
            HashMap<UUID, SocketIOClient> userClient = clientCache.getUserClient(sourceUserId);
            try {
                if (userClient != null) {
                    userClient.forEach((uuid, socketIOClient) -> {
                        socketIOClient.sendEvent("new-notice", json);
                        log.info("push message to user, sourceUserId:{} successfully", sourceUserId);
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
