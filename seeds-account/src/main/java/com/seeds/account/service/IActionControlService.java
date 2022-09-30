package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.ActionControlDto;
import com.seeds.account.enums.AccountActionControl;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.model.ActionControl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 操作控制 服务类
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
public interface IActionControlService extends IService<ActionControl> {
    /**
     * 获取一个操作是否可以执行
     * @param ac
     * @return
     */
    default boolean isEnabled(AccountActionControl ac) {
        ActionControlDto actionControlDto = get(ac.getType(), ac.getKey());
        return actionControlDto == null ||
                (Objects.equals(actionControlDto.getValue(), "true")
                        && actionControlDto.getStatus() == CommonStatus.ENABLED.getCode());
    }

    default boolean isEnabled(String type, String key) {
        ActionControlDto actionControlDto = get(type, key);
        return actionControlDto == null ||
                (Objects.equals(actionControlDto.getValue(), "true")
                        && actionControlDto.getStatus() == CommonStatus.ENABLED.getCode());
    }

    /**
     * 直接从数据库读取所有的合约配置
     * @return
     */
    List<ActionControlDto> loadAll();

    /**
     * 从缓存获取
     * @return
     */
    List<ActionControlDto> getAll();

    /**
     * 从缓存获取
     * @return
     */
    Map<String, ActionControlDto> getAllMap();

    /**
     * 从缓存获取单个
     * @param type
     * @param key
     * @return
     */
    ActionControlDto get(String type, String key);

    /**
     * 插入新的
     * @param systemConfigDto
     */
    void add(ActionControlDto systemConfigDto);

    /**
     * 更新
     * @param systemConfigDto
     */
    void update(ActionControlDto systemConfigDto);
}