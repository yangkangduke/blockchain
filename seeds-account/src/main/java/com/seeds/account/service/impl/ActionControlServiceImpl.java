package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.ActionControlDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.mapper.ActionControlMapper;
import com.seeds.account.model.ActionControl;
import com.seeds.account.service.IActionControlService;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 操作控制 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-09-29
 */
@Service
@Slf4j
public class ActionControlServiceImpl extends ServiceImpl<ActionControlMapper, ActionControl> implements IActionControlService {


    final static String ALL = "all";

    @Autowired
    ActionControlMapper actionControlMapper;

    LoadingCache<String, ListMap<ActionControlDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<ActionControlDto> list = loadAll();
                Map<String, ActionControlDto> map = list.stream().collect(Collectors.toMap(e -> toKey(e.getType(), e.getKey()), e -> e));
                return ListMap.init(list, map);
            });

    @Override
    public List<ActionControlDto> loadAll() {
        return actionControlMapper.selectAll()
                .stream()
                .map(e -> ObjectUtils.copy(e, new ActionControlDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ActionControlDto> getAll() {
        return Objects.requireNonNull(rules.get(ALL)).getList();
    }

    @Override
    public Map<String, ActionControlDto> getAllMap() {
        return Objects.requireNonNull(rules.get(ALL)).getMap();
    }

    @Override
    public ActionControlDto get(String type, String key) {
        return getAllMap().get(toKey(type, key));
    }

    private String toKey(String type, String key) {
        return type + ":" + key;
    }


    @Override
    public void add(ActionControlDto actionControlDto) {
        log.info("insert actionControlDto={}", actionControlDto);
        ActionControl actionControl = ObjectUtils.copy(actionControlDto, ActionControl.builder().build());
        actionControl.setCreateTime(System.currentTimeMillis());
        actionControl.setUpdateTime(System.currentTimeMillis());
        actionControl.setVersion(AccountConstants.DEFAULT_VERSION);
        actionControl.setStatus(CommonStatus.ENABLED.getCode());
        actionControlMapper.insert(actionControl);
    }

    @Override
    public void update(ActionControlDto actionControlDto) {
        log.info("update actionControlDto={}", actionControlDto);
        ActionControl actionControl = actionControlMapper.getByTypeAndKey(actionControlDto.getType(), actionControlDto.getKey());
        if (actionControl == null) {
            throw new MissingElementException();
        }
        actionControl.setValue(actionControlDto.getValue());
        actionControl.setUpdateTime(System.currentTimeMillis());
        actionControl.setVersion(actionControl.getVersion() + 1);
        actionControl.setStatus(actionControlDto.getStatus());
        actionControlMapper.updateByPrimaryKey(actionControl);
    }

}
