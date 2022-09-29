package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.SystemConfigDto;
import com.seeds.account.enums.AccountSystemConfig;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.model.SystemConfig;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 全局配置 服务类
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
public interface ISystemConfigService extends IService<SystemConfig> {

    /**
     * 获取价格
     * @param accountSystemConfig
     * @return
     */
    default String getValue(AccountSystemConfig accountSystemConfig) {
        return getValue(accountSystemConfig.getType(), accountSystemConfig.getKey());
    }

    default String getValue(String type, String key) {
        SystemConfigDto systemConfigDto = get(type, key);
        return (systemConfigDto != null && systemConfigDto.getStatus() == CommonStatus.ENABLED.getCode())
                ? systemConfigDto.getValue()
                : null;
    }

    /**
     * 获取价格
     * @param accountSystemConfig
     * @param defaultValue
     * @return
     */
    default String getValue(AccountSystemConfig accountSystemConfig, String defaultValue) {
        String value = getValue(accountSystemConfig);
        return value != null ? value : defaultValue;
    }

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<SystemConfigDto> loadAll();

    /**
     * 从缓存获取
     * @return
     */
    List<SystemConfigDto> getAll();

    /**
     * 从缓存获取
     * @return
     */
    Map<String, SystemConfigDto> getAllMap();

    /**
     * 从缓存获取单个
     * @param type
     * @param key
     * @return
     */
    SystemConfigDto get(String type, String key);

    /**
     * 插入新的
     * @param systemConfigDto
     */
    void add(SystemConfigDto systemConfigDto);

    /**
     * 更新
     * @param systemConfigDto
     */
    void update(SystemConfigDto systemConfigDto);
}
