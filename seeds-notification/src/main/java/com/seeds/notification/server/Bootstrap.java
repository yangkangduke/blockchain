package com.seeds.notification.server;

import com.seeds.notification.server.check.HttpCheckServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

/**
 * @author: hewei
 * @date 2022/9/19
 */
@Slf4j
public class Bootstrap {
    static {
        PushServer.pushServer.start(); //启动失败，系统会自动关闭
        final HttpCheckServer checkServer = new HttpCheckServer(11221);
        checkServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                PushServer.pushServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                checkServer.stop();
            } catch (Exception e) {
                log.error("checkServer.stop() - e", e);
            }
        }));
    }
}
