package com.seeds.account.service.impl;

import com.seeds.account.dto.SystemConfigDto;
import com.seeds.account.mapper.SystemConfigMapper;
import com.seeds.account.model.SystemConfig;
import com.seeds.account.service.ISystemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 全局配置 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements ISystemConfigService {


    @Override
    public List<SystemConfigDto> loadAll() {
        return null;
    }

    @Override
    public List<SystemConfigDto> getAll() {
        return null;
    }

    @Override
    public Map<String, SystemConfigDto> getAllMap() {
        return null;
    }

    @Override
    public SystemConfigDto get(String type, String key) {
        return null;
    }

    @Override
    public void add(SystemConfigDto systemConfigDto) {

    }

    @Override
    public void update(SystemConfigDto systemConfigDto) {

    }
}
