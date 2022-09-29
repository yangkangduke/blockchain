package com.seeds.account.chain.service.impl.tron;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.seeds.account.chain.dto.TronRpcConfigDto;
import com.seeds.account.service.SystemConfigService;
import com.seeds.account.util.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.trident.core.ApiWrapper;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author ray
 */
@Component
@Data
@Slf4j
public class TRONGridClient {

    private String dummyPrivateKey = "ab8f79d299999999999990000000000000000000000999999999999e2b29a905";

    final static String TYPE = "chain";
    final static String KEY = "tron_rpc";

    @Autowired
    SystemConfigService systemConfigService;

    AtomicLong counter = new AtomicLong(0);
    String lastConfig = null;
    List<TronRpcConfigDto> tronRPCs = null;
    List<ApiWrapper> clients = Lists.newArrayList();

    @PostConstruct
    public void init() {
        reload();
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    private void reload() {
        String config = systemConfigService.getValue(TYPE, KEY);
        if (Objects.equals(config, this.lastConfig)) {
            return;
        }
        log.info("tron client config={}", config);

        List<TronRpcConfigDto> tronRPCs = (config != null && config.length() > 0)
                ? JsonUtils.readValue(config, new TypeReference<List<TronRpcConfigDto>>() { })
                : Lists.newArrayList();

        List<ApiWrapper> oldClients = this.clients;
        List<ApiWrapper> newClients = tronRPCs.stream().map(this::createClient).collect(Collectors.toList());

        this.lastConfig = config;
        this.tronRPCs = tronRPCs;
        this.clients = newClients;

        // 关闭旧的Client
        oldClients.forEach(ApiWrapper::close);
    }

    private ApiWrapper createClient(TronRpcConfigDto r) {
        switch (r.getNetwork()) {
            case "SHASTA":
                return ApiWrapper.ofShasta(dummyPrivateKey);
            case "NILE":
                return ApiWrapper.ofNile(dummyPrivateKey);
            case "MAIN":
                // 默认连接主网
                // ** private key用来为trans签名， 这里我们不需要，签名使用wallet service完成。 这里API需要一个dummy的即可。
                return ApiWrapper.ofMainnet(dummyPrivateKey, r.getAuth());
            case "SELF":
                // 自搭节点
                // ** private key用来为trans签名， 这里我们不需要，签名使用wallet service完成。 这里API需要一个dummy的即可。
                return new ApiWrapper(r.getEndPoint(), r.getEndPointSolidity(), dummyPrivateKey, r.getAuth());
            default:
                return null;
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("ApiWrapper shutting down rpc clients");
        clients.forEach(ApiWrapper::close);
        log.info("ApiWrapper shutting down rpc clients done");
    }

    public ApiWrapper cli() {
        int index = (int) (counter.incrementAndGet() % clients.size());
        return clients.get(index);
    }

    public TronRpcConfigDto getChainRpcConfig() {
        return this.tronRPCs.size() > 0 ? this.tronRPCs.get(0) : null;
    }
}
