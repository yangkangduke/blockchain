package com.seeds.notification.server.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @author: hewei
 */

@Configuration
@Data
public class ServerConfigUtil {

    @Value("${webSocket.origin}")
    private String webSocketOrigin;
    @Value("${webSocket.port}")
    private Integer webSocketPort;
    @Value("${webSocket.host}")
    private String host;
    @Value("${webSocket.pingTimeout}")
    private int pingTimeout;
    @Value("${webSocket.pingInterval}")
    private int pingInterval;
}
