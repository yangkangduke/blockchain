package com.seeds.account.utils;

import com.google.common.util.concurrent.AtomicDouble;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author ray
 */
@Slf4j
public class MetricsGaugeUtils {

    private static final Map<String, AtomicDouble> GAUGE_CACHE = new ConcurrentHashMap<>();

    public static void gauge(String name, double value) {
        gauge(name, Tags.empty(), value);
    }

    public static void gauge(String name, Tags tags, double value) {
        try {
            String gaugeKey = gaugeKey(name, tags);
            if (!GAUGE_CACHE.containsKey(gaugeKey)) {
                GAUGE_CACHE.put(gaugeKey, new AtomicDouble());
            }
            Objects.requireNonNull(Metrics.gauge(name, tags, GAUGE_CACHE.get(gaugeKey))).set(value);
        } catch (Exception e) {
            log.error("failed in MetricsGaugeUtils gauge, ", e);
        }
    }

    private static String gaugeKey(String name, Tags tags) {
        if (tags != null && tags.stream().count() > 0) {
            return name + ":" + tags.stream().map(tag -> tag.getKey() + tag.getValue()).collect(Collectors.joining(""));
        } else {
            return name;
        }
    }
}
