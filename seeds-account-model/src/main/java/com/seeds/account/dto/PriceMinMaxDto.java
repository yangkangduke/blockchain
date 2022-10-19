package com.seeds.account.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class PriceMinMaxDto {
    public static final BigDecimal MIN_VALUE = BigDecimal.valueOf(Integer.MAX_VALUE);
    public static final BigDecimal MAX_VALUE = BigDecimal.ZERO;

    private String symbol;
    private BigDecimal min;
    private BigDecimal max;
    private List<AssetPriceDto> cachedPrices;
    private ReadWriteLock priceLock = new ReentrantReadWriteLock(true);

    public PriceMinMaxDto(AssetPriceDto price) {
        this.symbol = price.getSymbol();
        this.max = price.getPrice();
        this.min = price.getPrice();
        this.cachedPrices = new LinkedList<>();
        cachedPrices.add(price);
    }

    public PriceMinMaxDto(PriceMinMaxDto another, long timeWindow) {
        long ts = Instant.now().toEpochMilli() - timeWindow;
        this.cachedPrices = new LinkedList<>();
        this.symbol = another.symbol;
        this.min = MIN_VALUE;
        this.max = MAX_VALUE;

        Lock lock = another.priceLock.readLock();
        lock.lock();

        try {
            another.cachedPrices.stream()
                    .filter(p -> p.getTs() >= ts)
                    .forEach(p -> {
                        this.max = this.max.max(p.getPrice());
                        this.min = this.min.min(p.getPrice());
                        this.cachedPrices.add(p);
                    });
        } finally {
            lock.unlock();
        }
    }

    public void addPrice(AssetPriceDto newPrice, long timeWindow) {
        long now = Instant.now().toEpochMilli();

        Lock lock = priceLock.writeLock();
        lock.lock();

        try {
            cachedPrices.add(newPrice);
            for (Iterator<AssetPriceDto> iterator = cachedPrices.iterator(); iterator.hasNext(); ) {
                AssetPriceDto price = iterator.next();
                if (now - price.getTs() > timeWindow) {
                    iterator.remove();
                    min = newPrice.getPrice();
                    max = newPrice.getPrice();
                } else {
                    max = max.max(price.getPrice());
                    min = min.min(price.getPrice());
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
