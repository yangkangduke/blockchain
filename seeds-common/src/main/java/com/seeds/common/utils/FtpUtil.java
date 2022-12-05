package com.seeds.common.utils;

import cn.hutool.extra.ftp.Ftp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;


/**
 * @author yk
 */
@Slf4j
@Component
public class FtpUtil {

    static Ftp ftp;
    /** FTP 服务器地址IP地址*/
    static String host;
    /** FTP 登录用户名*/
    static String username;
    /** FTP 登录密码*/
    static String password;
    /** FTP 端口*/
    static int port;
    /** FTP 源目录*/
    static String basePath;

    public String getHost() {
        return host;
    }

    @Value("${ftp.hostname}")
    public void setHost(String host) {
        FtpUtil.host = host;
    }

    public String getUsername() {
        return username;
    }

    @Value("${ftp.username}")
    public void setUsername(String username1) {
        FtpUtil.username = username1;
    }

    public String getPassword() {
        return password;
    }

    @Value("${ftp.password}")
    public void setPassword(String password1) {
        FtpUtil.password = password1;
    }

    public int getPort() {
        return port;
    }

    @Value("${ftp.port}")
    public void setPort(int port) {
        FtpUtil.port = port;
    }

    public static String getBasePath() {
        return basePath;
    }

    @Value("${ftp.basePath}")
    public static void setBasePath(String basePath) {
        FtpUtil.basePath = basePath;
    }

    public FtpUtil() {}

    private FtpUtil(String host, int port, String username, String password) {
        this(host, port, username, password, Charset.defaultCharset());
    }

    private FtpUtil(String host, int port, String username, String password, Charset charset) {
        ftp = new Ftp(host, port, username, password, charset);
        FtpUtil.host = StringUtils.isEmpty(host) ? "localhost" : host;
        FtpUtil.port = (port <= 0) ? 21 : port;
        FtpUtil.username = StringUtils.isEmpty(username) ? "anonymous" : username;
        FtpUtil.password = password;
    }

    /**
     * 创建默认的ftp客户端
     * @return ftp
     */
    public static FtpUtil createFtpCli() {
        return new FtpUtil(host, port, username, password);
    }

    /**
     * 创建默认的ftp客户端
     * @param host 地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @return ftp
     */
    public static FtpUtil createFtpCli(String host, int port, String username, String password) {
        return new FtpUtil(host, port, username, password);
    }

    /**
     * 创建自定义属性的ftp客户端
     * @param host 地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @param charset 编码
     * @return ftp
     */
    public static FtpUtil createFtpCli(String host, int port, String username, String password, Charset charset) {
        return new FtpUtil(host, port, username, password, charset);
    }

    /**
     * 设置超时时间
     * @param defaultTimeout 默认超时时间
     * @param connectTimeout 连接超时时间
     * @param dataTimeout 超时时间
     */
    public static void setTimeout(int defaultTimeout, int connectTimeout, int dataTimeout) {
        FTPClient ftpClient = ftp.getClient();
        ftpClient.setDefaultTimeout(defaultTimeout);
        ftpClient.setConnectTimeout(connectTimeout);
        ftpClient.setDataTimeout(dataTimeout);
    }
}
