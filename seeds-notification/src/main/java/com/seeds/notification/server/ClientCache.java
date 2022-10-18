package com.seeds.notification.server;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储客户端连接信息
 *
 * @author: hewei
 * @date 2022/9/19
 */
@Component
public class ClientCache {

    //本地缓存
    private static Map<Long, HashMap<UUID, SocketIOClient>> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 存入本地缓存
     *
     * @param userId         用户ID
     * @param sessionId      连接的sessionID
     * @param socketIOClient 对应的通道连接信息
     */
    public void saveClient(Long userId, UUID sessionId, SocketIOClient socketIOClient) {
        HashMap<UUID, SocketIOClient> sessionIdClientCache = concurrentHashMap.get(userId);
        if (sessionIdClientCache == null) {
            sessionIdClientCache = new HashMap<>();
        }
        sessionIdClientCache.put(sessionId, socketIOClient);
        concurrentHashMap.put(userId, sessionIdClientCache);
    }

    /**
     * 根据用户ID获取所有通道信息
     *
     * @param userId
     * @return
     */
    public HashMap<UUID, SocketIOClient> getUserClient(Long userId) {
        return concurrentHashMap.get(userId);
    }


    /**
     * 删除链接信息
     */
    public void deleteSessionClient(Long userId, UUID sessionId) {
        concurrentHashMap.get(userId).remove(sessionId);
    }
}