package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.SystemConfigDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.mapper.SystemConfigMapper;
import com.seeds.account.model.SystemConfig;
import com.seeds.account.service.ISystemConfigService;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.JsonUtils;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.redis.account.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局配置 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@Service
@Slf4j
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements ISystemConfigService {


    final static String ALL = "all";

    @Autowired
    SystemConfigMapper systemConfigMapper;

    @Autowired
    RedissonClient redissonClient;

    LoadingCache<String, ListMap<SystemConfigDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<SystemConfigDto> list = loadAll();
                Map<String, SystemConfigDto> map = list.stream().collect(Collectors.toMap(e -> e.getType() + ":" + e.getKey(), e -> e));
                return ListMap.init(list, map);
            });

    @Override
    public List<SystemConfigDto> loadAll() {
        return systemConfigMapper.selectAll()
                .stream()
                .map(e -> ObjectUtils.copy(e, new SystemConfigDto()))
                .peek(config -> redissonClient.getBucket(RedisKeys.getSystemConfigKey(config.getType(), config.getKey()), StringCodec.INSTANCE)
                        .set(JsonUtils.writeValue(config)))
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemConfigDto> getAll() {
        return rules.get(ALL).getList();
    }

    @Override
    public Map<String, SystemConfigDto> getAllMap() {
        return rules.get(ALL).getMap();
    }

    @Override
    public SystemConfigDto get(String type, String key) {
        return getAllMap().get(type + ":" + key);
    }

    @Override
    public void add(SystemConfigDto systemConfigDto) {
        log.info("insert systemConfigDto={}", systemConfigDto);
        SystemConfig systemConfig = ObjectUtils.copy(systemConfigDto, SystemConfig.builder().build());
        systemConfig.setCreateTime(System.currentTimeMillis());
        systemConfig.setUpdateTime(System.currentTimeMillis());
        systemConfig.setVersion(AccountConstants.DEFAULT_VERSION);
        systemConfig.setStatus(CommonStatus.ENABLED.getCode());
        systemConfigMapper.insert(systemConfig);
    }

    @Override
    public void update(SystemConfigDto systemConfigDto) {
        SystemConfig systemConfig = systemConfigMapper.getByTypeAndKey(systemConfigDto.getType(), systemConfigDto.getKey());
        log.info("update systemConfigDto={} systemConfig={}", systemConfigDto, systemConfig);
        if (systemConfig == null) {
            throw new MissingElementException();
        }
        systemConfig.setValue(systemConfigDto.getValue());
        systemConfig.setComments(systemConfigDto.getComments() != null
                ? systemConfigDto.getComments()
                : systemConfig.getComments() != null
                ? systemConfig.getComments()
                : "");
        systemConfig.setUpdateTime(System.currentTimeMillis());
        systemConfig.setVersion(systemConfig.getVersion() + 1);
        systemConfigMapper.updateByPrimaryKey(systemConfig);
    }
}
