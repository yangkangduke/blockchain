package com.seeds.account.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MetricConfig {

    @Autowired
    MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        Metrics.addRegistry(meterRegistry);
    }
}
