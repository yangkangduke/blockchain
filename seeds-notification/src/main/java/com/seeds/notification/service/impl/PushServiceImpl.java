package com.seeds.notification.service.impl;

import com.seeds.notification.dto.NotificationDto;
import com.seeds.notification.server.PushServer;
import com.seeds.notification.service.IPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushServiceImpl implements IPushService {

    /**
     * 向页面推送消息
     */
    @Override
    public void push(NotificationDto message) {
        try {
            long l1 = System.currentTimeMillis();
            PushServer.pushServer.push(message);
            final long l2 = System.currentTimeMillis() - l1;
            log.info("push耗时,time={}ms", l2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
