//package com.seeds.notification.server.util;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * 获取配置文件内容工具类
// *
// * @author: hewei
// */
//@Slf4j
//@Data
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class ServerConfigUtils {
//
//    @Autowired
//    private ApplicationContext context;
//
//    public static ServerConfigUtils instance;
//
//    @Value("${webSocket.origin}")
//    private String webSocketOrigin;
//    @Value("${webSocket.port}")
//    private Integer webSocketPort;
//
//    @PostConstruct
//    public void init() {
//        try {
//            instance = (ServerConfigUtils) context.getBean(Class.forName("com.seeds.notification.server.util.ServerConfigUtils"));
//        } catch (Exception e) {
//            log.error("ServerConfigUtils - init - error", e);
//        }
//    }
//}
