//package com.seeds.account.service.impl;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.seeds.account.AccountConstants;
//import com.seeds.account.dto.AssetPriceDto;
//import com.seeds.account.dto.PriceMinMaxDto;
//import com.seeds.account.enums.AccountSystemConfig;
//import com.seeds.account.enums.PriceUpdateEvent;
//import com.seeds.account.ex.PriceNotUpdateException;
//import com.seeds.account.listener.PriceUpdateListener;
//import com.seeds.account.service.IPriceService;
//import com.seeds.account.service.ISystemConfigService;
//import com.seeds.account.util.JsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Assert;
//
//import javax.annotation.PreDestroy;
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author yk
// */
//@Slf4j
//@Service
//public class PriceServiceImpl implements IPriceService {
//
//    private static final long INDIVIDUAL_TS_PSEUDO_ID = 0L;
//    private static final long OVERALL_TS_PSEUDO_ID = 1000L;
//
//    private final ThreadPoolTaskExecutor executor;
//    private final List<PriceUpdateListener> listeners = Lists.newCopyOnWriteArrayList();
//    private final Map<String, PriceMinMaxDto> priceMinMaxMap = new HashMap<>();
//    private long lastUpdateTime = 0;
//    private Map<String, BigDecimal> latestPriceMap = Maps.newHashMap();
//    private Map<String, AssetPriceDto> latestAssetPrice = Maps.newHashMap();
//    private BigDecimal kinePrice = BigDecimal.ZERO;
//
//    @Autowired
//    private ISystemConfigService systemConfigService;
//
//    public PriceServiceImpl() {
//        executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(1);
//        executor.setMaxPoolSize(1);
//        executor.setQueueCapacity(1024);
//        executor.initialize();
//        executor.setRejectedExecutionHandler((r, exe) -> log.error("Price service executor queue is full"));
//    }
//
//    @PreDestroy
//    public void shutdown() {
//        executor.shutdown();
//    }
//
//    @Override
//    public void register(PriceUpdateListener listener) {
//        Assert.isTrue(listener != null, "invalid listener");
//        if (listeners.contains(listener)) {
//            return;
//        }
//        listeners.add(listener);
//    }
//
//    @Override
//    public void remove(PriceUpdateListener listener) {
//        Assert.isTrue(listener != null, "invalid listener");
//        if (!listeners.contains(listener)) {
//            return;
//        }
//        listeners.remove(listener);
//    }
//
//    @Override
//    public Map<String, BigDecimal> getLatestPriceMap() {
//        return latestPriceMap;
//    }
//
//    @Override
//    public long getLastUpdateTime() {
//        return lastUpdateTime;
//    }
//
//    @Override
//    public BigDecimal getLatestPrice(String symbol) {
//        return latestPriceMap.get(symbol);
//    }
//
//    @Override
//    public Map<String, BigDecimal> getLatestPriceMapOnCheck() {
//        checkUpdatedTime();
//        return latestPriceMap;
//    }
//
//    private long getValidTime() {
//        return Long.parseLong(systemConfigService.getValue(AccountSystemConfig.PRICE_VALID_TIME));
//    }
//
//    private void checkUpdatedTime() {
//        if (System.currentTimeMillis() - lastUpdateTime > getValidTime()) {
//            throw new PriceNotUpdateException("price not updated");
//        }
//    }
//
//    /**
//     * 从中间件获取到了数据
//     *
//     * @param aggregatedPrice
//     */
//    @Override
//    public void onAggregatedPrice(AggregatedPrice aggregatedPrice) {
//        if (aggregatedPrice == null || aggregatedPrice.getPrices() == null || aggregatedPrice.getPrices().size() == 0) {
//            log.warn("invalid aggregatedPrice={}", aggregatedPrice);
//            return;
//        }
//        long ts = System.currentTimeMillis();
//        if (aggregatedPrice.getTs() + getValidTime() <= ts) {
//            log.warn("price expired aggregatedPrice={} now={} elapsed={}", aggregatedPrice, ts, ts - aggregatedPrice.getTs());
//            return;
//        }
//        Map<String, BigDecimal> priceMap = toPriceMap(aggregatedPrice);
//        this.latestPriceMap = priceMap;
//        this.latestAssetPrice = toAssetPriceMap(aggregatedPrice);
//        cachePriceMinMax(latestAssetPrice);
//        this.lastUpdateTime = ts;
//        if (priceMap.containsKey(AccountConstants.KINE)) {
//            this.kinePrice = priceMap.get(AccountConstants.KINE);
//        }
//        // 构造价格通知事件
//        PriceUpdateEvent event = PriceUpdateEvent.builder().ts(aggregatedPrice.getTs()).priceMap(priceMap).build();
//        triggerEvent(event);
//    }
//
//    @Override
//    public BigDecimal getLastKinePrice() {
//        return kinePrice;
//    }
//
//    private Map<String, BigDecimal> toPriceMap(AggregatedPrice aggregatedPrice) {
//        Map<String, BigDecimal> priceMap = Maps.newLinkedHashMap();
//        aggregatedPrice.getPrices().forEach(e -> {
//            String symbol = e.getSymbol();
//            BigDecimal price = new BigDecimal(e.getPrice());
//            if (price.signum() <= 0) {
//                log.info("invalid symbol={} price={}", symbol, price);
//                return;
//            }
//            priceMap.put(symbol, price);
//            int pos = symbol.lastIndexOf(AccountConstants.USD);
//            if (pos != -1) {
//                String currency = symbol.substring(0, pos);
//                priceMap.put(currency, price);
//            }
//        });
//        return priceMap;
//    }
//
//    private Map<String, AssetPriceDto> toAssetPriceMap(AggregatedPrice aggregatedPrice) {
//        Map<String, AssetPriceDto> assetPriceMap = Maps.newLinkedHashMap();
//        aggregatedPrice.getPrices().forEach(e -> {
//            String symbol = e.getSymbol();
//            BigDecimal price = new BigDecimal(e.getPrice());
//            if (price.signum() <= 0) {
//                log.info("invalid symbol={} price={}", symbol, price);
//                return;
//            }
//            assetPriceMap.put(symbol, AssetPriceDto.builder().symbol(symbol).price(price).ts(e.getTs()).build());
//            int pos = symbol.lastIndexOf(AccountConstants.USD);
//            if (pos != -1) {
//                String currency = symbol.substring(0, pos);
//                assetPriceMap.put(currency, AssetPriceDto.builder().symbol(currency).price(price).ts(e.getTs()).build());
//            }
//        });
//        return assetPriceMap;
//    }
//
//    @Override
//    public Map<String, AssetPriceDto> getLatestAssetPrice() {
//        return latestAssetPrice;
//    }
//
//    @Override
//    public BigDecimal getLatestPriceOnCheck(String symbol) {
//        AssetPriceDto assetPriceDto = latestAssetPrice.get(symbol);
//        if (assetPriceDto == null || System.currentTimeMillis() - assetPriceDto.getTs() > getValidTime()) {
//            throw new PriceNotUpdateException("price not updated");
//        }
//        return assetPriceDto.getPrice();
//    }
//
//    private void triggerEvent(PriceUpdateEvent event) {
//        Assert.isTrue(event != null, "invalid event");
//        log.debug("triggerEvent event={} elapsed={}", event, System.currentTimeMillis() - event.getTs());
//
//        listeners.forEach(listener -> executor.submit(() -> listener.onEvent(event)));
//    }
//
//    @Override
//    public PriceMinMaxDto getCachedMinMax(long userId, String symbol) {
//        Map<Long, Long> windowMap = getTimeWindowMap();
//        long defaultWindow = windowMap.getOrDefault(INDIVIDUAL_TS_PSEUDO_ID, 500L);
//        PriceMinMaxDto priceMinMaxDto = priceMinMaxMap.get(symbol);
//        if (priceMinMaxDto != null) {
//            return new PriceMinMaxDto(priceMinMaxDto, windowMap.getOrDefault(userId, defaultWindow));
//        }
//        return null;
//    }
//
//    @Override
//    public PriceMinMaxDto getCachedMinMax(String symbol, long timeWindow) {
//        PriceMinMaxDto priceMinMaxDto = priceMinMaxMap.get(symbol);
//        if (priceMinMaxDto != null) {
//            return new PriceMinMaxDto(priceMinMaxDto, timeWindow);
//        }
//        return null;
//    }
//
//    private Map<Long, Long> getTimeWindowMap() {
//        try {
//            return JsonUtils.readValue(systemConfigService.getValue(AccountSystemConfig.PRICE_RECORD_MS),
//                    new TypeReference<Map<Long, Long>>() {
//                    });
//        } catch (Exception e) {
//            log.warn("Error while getting price time window map", e);
//            return Collections.emptyMap();
//        }
//    }
//
//    private void cachePriceMinMax(Map<String, AssetPriceDto> latestAssetPrice) {
//        Map<Long, Long> windowMap = getTimeWindowMap();
//        long window = windowMap.getOrDefault(OVERALL_TS_PSEUDO_ID, 5000L);
//        latestAssetPrice.forEach((symbol, price) -> {
//            PriceMinMaxDto minMaxDto = priceMinMaxMap.get(symbol);
//            if (minMaxDto == null) {
//                priceMinMaxMap.put(symbol, new PriceMinMaxDto(price));
//            } else {
//                minMaxDto.addPrice(price, window);
//            }
//        });
//    }
//}
