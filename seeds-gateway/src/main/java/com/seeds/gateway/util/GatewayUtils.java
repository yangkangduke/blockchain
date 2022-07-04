package com.seeds.gateway.util;

import com.google.common.net.InetAddresses;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.InetAddress;

@Slf4j
@UtilityClass
public class GatewayUtils {

    public String getClientIp(ServerWebExchange exchange) {
        String clientIp = "";
        // server host
        String xForwardedHost = exchange.getRequest().getHeaders().getFirst("X-Forwarded-Host");
        log.debug("X-Forwarded-Host: {}", xForwardedHost);

        // proxy server ip, e.g. cloudflare
        String xRealIP = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        log.debug("X-Real-IP: {}", xRealIP);

        String xConnectingIP = exchange.getRequest().getHeaders().getFirst("x-connecting-ip");
        log.debug("x-connecting-ip: {}", xConnectingIP);
        // client ip
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        log.debug("X-Forwarded-For: {}", xForwardedFor);
        if (StringUtils.isNotBlank(xConnectingIP)) {
            clientIp = xConnectingIP;
        } else if (StringUtils.isNotBlank(xForwardedFor)) {
            String[] ss = xForwardedFor.split(",");
            if (ss.length > 0) {
                clientIp = ss[0];
            }
        } else if (StringUtils.isNotBlank(xRealIP)) {
            clientIp = xRealIP;
        } else {
            log.warn("Cannot not find any valid IP address from request, using remote address.");
            clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
//        if (isPrivateAddress(clientIp.trim())) {
//            log.warn("Got invalid (private) IP address: {}, set as empty", clientIp);
//            clientIp = "";
//        }
        log.debug("client ip is {}", clientIp);
        return checkIpAddress(clientIp.trim());
    }

    private boolean isPrivateAddress(String validIpString) {
        InetAddress addr = InetAddresses.forString(validIpString);
        return addr.isSiteLocalAddress() || addr.isLoopbackAddress();
    }

    private String checkIpAddress(String ip) {
        if (InetAddresses.isInetAddress(ip)) {
            return ip.toLowerCase();
        }
        log.warn("Got invalid IP address: {}", ip);
        return "";
    }
}
