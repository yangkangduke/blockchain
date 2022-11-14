package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.dto.req.BlackListAddressSaveOrUpdateReq;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.ConfigException;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.mapper.BlacklistAddressMapper;
import com.seeds.account.model.BlacklistAddress;
import com.seeds.account.model.DepositRule;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.service.IBlacklistAddressService;
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

import static com.seeds.common.enums.ErrorCode.ILLEGAL_BLACK_LIST_CONFIG;


/**
 * <p>
 * Ethereum黑地址 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Slf4j
@Service
public class BlacklistAddressServiceImpl extends ServiceImpl<BlacklistAddressMapper, BlacklistAddress> implements IBlacklistAddressService {
    final static String ALL = "all";

    @Autowired
    BlacklistAddressMapper blacklistAddressMapper;

    LoadingCache<String, ListMap<BlacklistAddressDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(2, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<BlacklistAddressDto> list = loadAll();
                Map<String, BlacklistAddressDto> map = list.stream()
                        .collect(Collectors.toMap(e -> e.getType() + ":" + e.getUserId() + ":" + e.getAddress(), e -> e));
                return ListMap.init(list, map);
            });

    @Override
    public List<BlacklistAddressDto> loadAll() {
        return blacklistAddressMapper.selectList(new QueryWrapper<>())
                .stream()
                .map(e -> ObjectUtils.copy(e, new BlacklistAddressDto()))
                .collect(Collectors.toList());
    }

    @Override
    public BlacklistAddressDto get(Integer type, Long userId, String address) {
        // milo 由于要兼容大小写，所以不能直接使用map
        return getAll().stream()
                .filter(e -> Objects.equals(e.getType(), type) &&
                        e.getStatus() == CommonStatus.ENABLED.getCode() &&
                        (Objects.equals(e.getUserId(), userId) ||
                                ObjectUtils.isAddressEquals(e.getAddress(), address)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BlacklistAddressDto> getAll() {
        return Objects.requireNonNull(rules.get(ALL)).getList();
    }

    @Override
    public Boolean add(BlackListAddressSaveOrUpdateReq req) {

        // 该黑地址已经存在，无法添加
        this.checkEnableBlacklistAddress(req.getAddress());

        BlacklistAddress blacklistAddress = ObjectUtils.copy(req, BlacklistAddress.builder().build());
        blacklistAddress.setCreateTime(System.currentTimeMillis());
        blacklistAddress.setUpdateTime(System.currentTimeMillis());
        blacklistAddress.setVersion(AccountConstants.DEFAULT_VERSION);
        blacklistAddress.setStatus(CommonStatus.ENABLED.getCode());
        return save(blacklistAddress);
    }

    @Override
    public Boolean update(BlackListAddressSaveOrUpdateReq req) {

        BlacklistAddress Address = getById(req.getId());
        BlacklistAddress blacklistAddress = ObjectUtils.copy(req, BlacklistAddress.builder().build());
        blacklistAddress.setUpdateTime(System.currentTimeMillis());
        blacklistAddress.setVersion(Address.getVersion() + 1);
        blacklistAddress.setStatus(req.getStatus());
        return updateById(blacklistAddress);
    }

    private void checkEnableBlacklistAddress(String address) {

        LambdaQueryWrapper<BlacklistAddress> queryWrap = new QueryWrapper<BlacklistAddress>().lambda()
                .eq(BlacklistAddress::getAddress, address);
        BlacklistAddress one = getOne(queryWrap);
        if (one != null) {
            throw new ConfigException(ILLEGAL_BLACK_LIST_CONFIG);
        }
    }


    @Override
    public Boolean delete(SwitchReq req) {
        BlacklistAddress disableBlacklistAddress = BlacklistAddress.builder().status(CommonStatus.DISABLED.getCode()).build();
        BlacklistAddress Address = getById(req.getId());
        disableBlacklistAddress.setChain(Address.getChain());

        this.update(disableBlacklistAddress, new LambdaUpdateWrapper<BlacklistAddress>().eq(BlacklistAddress::getChain, Address.getChain()).ne(BlacklistAddress::getId, req.getId()));
        BlacklistAddress blacklistAddress = BlacklistAddress.builder()
                .id(req.getId())
                .status(req.getStatus())
                .build();
        return updateById(blacklistAddress);
    }

}



