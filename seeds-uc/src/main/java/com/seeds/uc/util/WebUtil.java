package com.seeds.uc.util;

import com.google.common.net.InetAddresses;
import com.seeds.common.web.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yk
 * @date 2020/8/27
 */
@Slf4j
public class WebUtil {
    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IP1 = "127.0.0.1";

    private WebUtil() {
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_TOKEN);
    }

    public static String getIpAddr(HttpServletRequest request) {
        String clientIp = "";
        // server host
        String xForwardedHost = request.getHeader("X-Forwarded-Host");
        log.debug("X-Forwarded-Host: {}", xForwardedHost);

        // proxy server ip, e.g. cloudflare
        String xRealIP = request.getHeader("X-Real-IP");
        log.debug("X-Real-IP: {}", xRealIP);

        String xConnectingIP = request.getHeader("x-connecting-ip");
        log.debug("x-connecting-ip: {}", xConnectingIP);
        // client ip
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        log.debug("X-Forwarded-For: {}", xForwardedFor);
        if (!StringUtils.isEmpty(xConnectingIP)) {
            clientIp = xConnectingIP;
        } else if (!StringUtils.isEmpty(xForwardedFor)) {
            String[] ss = xForwardedFor.split(",");
            if (ss.length > 0) {
                clientIp = ss[0];
            }
        } else if (!StringUtils.isEmpty(xRealIP)) {
            clientIp = xRealIP;
        }
        clientIp = clientIp.equals(LOCALHOST_IP) ? LOCALHOST_IP1 : clientIp;
        return checkIpAddress(clientIp.trim());
    }

    private static String checkIpAddress(String ip) {
        if (InetAddresses.isInetAddress(ip)) {
            return ip.toLowerCase();
        }
        log.warn("Got invalid IP address: {}", ip);
        return "";
    }
}
