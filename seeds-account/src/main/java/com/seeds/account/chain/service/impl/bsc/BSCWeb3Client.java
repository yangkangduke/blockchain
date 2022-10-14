package com.seeds.account.chain.service.impl.bsc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.seeds.account.chain.dto.ChainRpcConfigDto;
import com.seeds.account.config.ProxyConfig;
import com.seeds.account.service.ISystemConfigService;
import com.seeds.account.util.JsonUtils;
import io.micrometer.core.instrument.Metrics;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 *  @author yk
 *  @author daixu
 *
 */
@Component
@Slf4j
@Data
public class BSCWeb3Client {

    final static String TYPE = "chain";
    final static String KEY = "bsc_rpc";

    @Autowired
    private ProxyConfig proxyConfig;

    @Autowired
    ISystemConfigService systemConfigService;

    AtomicLong counter = new AtomicLong(0);
    String lastConfig = null;
    List<ChainRpcConfigDto> web3RPCs = null;
    List<Web3j> readClients = Lists.newArrayList();
    List<Web3j> writeClients = Lists.newArrayList();

    @PostConstruct
    public void init() {
        reload();
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void reload() {
        String config = systemConfigService.getValue(TYPE, KEY);
        if (Objects.equals(config, this.lastConfig)) {
            return;
        }
        log.info("web3j config={}", config);

        List<ChainRpcConfigDto> web3RPCs = (config != null && config.length() > 0)
                ? JsonUtils.readValue(config, new TypeReference<List<ChainRpcConfigDto>>() {  })
                : Lists.newArrayList();

        List<Web3j> oldReadClients = this.readClients;
        List<Web3j> oldWriteClients = this.writeClients;

        // 查询节点
        List<Web3j> newReadClients = web3RPCs.stream().filter(e -> e.getType() == 1).map(this::createClient).collect(Collectors.toList());
        // 发送交易节点
        List<Web3j> newWriteClients = web3RPCs.stream().filter(e -> e.getType() == 2).map(this::createClient).collect(Collectors.toList());

        this.lastConfig = config;
        this.web3RPCs = web3RPCs;
        this.readClients = newReadClients;
        this.writeClients = newWriteClients;

        // 关闭旧的Client
        oldReadClients.forEach(Web3j::shutdown);
        oldWriteClients.forEach(Web3j::shutdown);
    }

    private Web3j createClient(ChainRpcConfigDto r) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (r.getAuth() != null && r.getAuth().length() > 0) {
            builder.addInterceptor(
                    chain -> {
                        Request request = chain.request();
                        Request authenticatedRequest = request.newBuilder()
                                .header("Authorization", "Basic " + new String(Base64.encodeBase64((":" + r.getAuth()).getBytes(StandardCharsets.ISO_8859_1))))
                                .build();
                        return chain.proceed(authenticatedRequest);
                    }
            );
        }
        builder.addInterceptor(
                chain -> {
                    try {
                        return chain.proceed(chain.request());
                    } catch (SocketTimeoutException socketTimeoutException) {
                        // 把错误报告给监控系统
                        Metrics.counter("account.rpc.timeout.error").increment();
                        throw socketTimeoutException;
                    }
                }
        );

        if (proxyConfig.isEnable()) {
            builder.proxy(new Proxy(
                    Proxy.Type.HTTP,
                    new InetSocketAddress(proxyConfig.getProxyHost(), proxyConfig.getProxyPort())
            ));
        }
        return Web3j.build(new HttpService(
                r.getRpc(),
                builder.retryOnConnectionFailure(true).build()
        ));
    }

    @PreDestroy
    public void destroy() {
        log.info("Web3ClientService shutting down rpc clients");
        readClients.forEach(Web3j::shutdown);
        writeClients.forEach(Web3j::shutdown);
        log.info("Web3ClientService shutting down rpc clients done");
    }

    public Web3j readCli() {
        int index = (int) (counter.incrementAndGet() % readClients.size());
        return readClients.get(index);
    }

    public Web3j writeCli() {
        int index = (int) (counter.incrementAndGet() % writeClients.size());
        return writeClients.get(index);
    }

    public ChainRpcConfigDto getChainRpcConfig() {
        return this.web3RPCs.size() > 0 ? this.web3RPCs.get(0) : null;
    }
}
