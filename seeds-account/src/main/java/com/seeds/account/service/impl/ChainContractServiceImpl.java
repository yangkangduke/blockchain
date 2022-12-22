package com.seeds.account.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.seeds.account.AccountConstants;
import com.seeds.account.dto.ChainContractDto;
import com.seeds.account.enums.CommonStatus;
import com.seeds.account.ex.MissingElementException;
import com.seeds.account.mapper.ChainContractMapper;
import com.seeds.account.model.ChainContract;
import com.seeds.account.service.IChainContractService;
import com.seeds.account.tool.ListMap;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.enums.Chain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yk
 */
@Slf4j
@Service
public class ChainContractServiceImpl implements IChainContractService {

    final static String ALL = "all";

    @Autowired
    ChainContractMapper chainContractMapper;

    LoadingCache<String, ListMap<ChainContractDto>> rules = Caffeine.newBuilder()
            .refreshAfterWrite(15, TimeUnit.SECONDS)
            .recordStats()
            .build(k -> {
                List<ChainContractDto> list = loadAll();
                log.info("---List<ChainContractDto>{}---", list);
                Map<String, ChainContractDto> map = list.stream().collect(Collectors.toMap(e -> toKey(e.getChain(), e.getCurrency()), e -> e));
                return ListMap.init(list, map);
            });

    @Override
    public List<ChainContractDto> loadAll() {
        return chainContractMapper.selectAll()
                .stream()
                .map(e -> ObjectUtils.copy(e, new ChainContractDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChainContractDto> getAll() {
        return rules.get(ALL).getList();
    }

    @Override
    public List<ChainContractDto> getAllByChain(Chain chain) {
        return rules.get(ALL).getList().stream().filter(e -> e.getChain() == chain.getCode()).collect(Collectors.toList());
    }

    @Override
    public Map<String, ChainContractDto> getAllMap() {
        return rules.get(ALL).getMap();
    }

    @Override
    public ChainContractDto get(int chain, String currency) {
        log.info("chain {} ，currency {}，contractMap {} ", chain, currency, getAllMap().get(toKey(chain, currency)));
        return getAllMap().get(toKey(chain, currency));
    }

    private String toKey(int chain, String currency) {
        return chain + ":" + currency;
    }

    @Override
    public void insert(ChainContractDto chainContractDto) {
        log.info("insert chainContractDto={}", chainContractDto);
        ChainContract chainContract = ObjectUtils.copy(chainContractDto, new ChainContract());
        chainContract.setCreateTime(System.currentTimeMillis());
        chainContract.setUpdateTime(System.currentTimeMillis());
        chainContract.setVersion(AccountConstants.DEFAULT_VERSION);
        chainContract.setStatus(CommonStatus.ENABLED.getCode());
        chainContractMapper.insert(chainContract);
    }

    @Override
    public void update(ChainContractDto chainContractDto) {
        log.info("update chainContractDto={}", chainContractDto);
        ChainContract chainContract = chainContractMapper.getByCurrencyAndAddress(chainContractDto.getChain(),
                chainContractDto.getCurrency(), chainContractDto.getAddress());
        if (chainContract == null) {
            throw new MissingElementException();
        }
        ObjectUtils.copy(chainContractDto, chainContract);
        chainContract.setUpdateTime(System.currentTimeMillis());
        chainContract.setVersion(chainContract.getVersion() + 1);
        chainContractMapper.updateByPrimaryKey(chainContract);
    }
}
