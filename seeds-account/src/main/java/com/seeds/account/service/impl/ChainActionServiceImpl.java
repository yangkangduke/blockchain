package com.seeds.account.service.impl;

import com.google.common.collect.Lists;
import com.seeds.account.AccountConstants;
import com.seeds.account.anno.SingletonLock;
import com.seeds.account.chain.dto.NativeChainBlockDto;
import com.seeds.account.chain.dto.NativeChainTransactionDto;
import com.seeds.account.chain.service.IChainService;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.SystemWalletAddressDto;
import com.seeds.account.enums.*;
import com.seeds.account.mapper.ChainBlockMapper;
import com.seeds.account.mapper.ChainDepositAddressMapper;
import com.seeds.account.mapper.ChainDepositWithdrawHisMapper;
import com.seeds.account.model.ChainBlock;
import com.seeds.account.model.ChainDepositAddress;
import com.seeds.account.model.ChainDepositWithdrawHis;
import com.seeds.account.service.*;
import com.seeds.account.util.ObjectUtils;
import com.seeds.common.enums.Chain;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static com.seeds.account.AccountConstants.DEPOSIT_DECIMALS;

/**
 * @program: seeds-java
 * @description: chain action
 * @author: yk
 * @create: 2022-09-28 17:36
 **/
@Slf4j
@Service
public class ChainActionServiceImpl implements IChainActionService {

    @Autowired
    private IActionControlService actionControlService;
    @Autowired
    private ChainDepositAddressMapper chainDepositAddressMapper;
    @Autowired
    ISystemConfigService systemConfigService;
    @Autowired
    private IChainService chainService;
    @Autowired
    private IChainActionPersistentService chainActionPersistentService;
    @Autowired
    private ChainBlockMapper chainBlockMapper;
    @Autowired
    private IChainDepositService chainDepositService;
    @Autowired
    private ChainDepositWithdrawHisMapper chainDepositWithdrawHisMapper;
    @Autowired
    private ISystemWalletAddressService systemWalletAddressService;
    @Autowired
    private IBlacklistAddressService blacklistAddressService;

    @Override
    @SingletonLock(key = "/seeds/account/chain/scan-idle-addresses")
    public void scanAndCreateAddresses() throws Exception {
        boolean enabled = actionControlService.isEnabled(AccountActionControl.CREATE_CHAIN_ADDRESS);
        if (!enabled) {
            return;
        }
        // 2021-04-27 Milo 扫描需要创建地址的链 (遇到BSC链就映射成ETH链)
        for (Chain chain : Chain.SUPPORT_CREATE_ADDRESS_LIST) {
            chain = Chain.mapChain(chain);
            int chainCode = chain.getCode();

            int idles = chainDepositAddressMapper.countIdleAddresses(chainCode);
            int minIdles = Integer.parseInt(systemConfigService.getValue(AccountSystemConfig.CHAIN_MIN_IDLE_ADDRESS, "10"));
            if (idles >= minIdles) {
                continue;
            }
            int slot = minIdles - idles;
            if (slot <= 0) {
                continue;
            }
            try {
                log.info("checkAndCreateAddresses chain={} minIdles={} idles={} slot={}", chain, minIdles, idles, slot);
                List<String> addresses = chainService.createAddresses(chain, slot);
                if (addresses.size() > 0) {
                    List<ChainDepositAddress> list = addresses.stream()
                            .map(address -> ChainDepositAddress.builder()
                                    .createTime(System.currentTimeMillis())
                                    .updateTime(System.currentTimeMillis())
                                    .version(AccountConstants.DEFAULT_VERSION)
                                    .chain(String.valueOf(chainCode))
                                    .address(address)
                                    .userId(AccountConstants.NOBODY_USER_ID)
                                    .status(CommonStatus.ENABLED.getCode())
                                    .build()
                            ).collect(Collectors.toList());
                    // 交给一个事务service
                    chainActionPersistentService.insert(list);
                }
            } catch (Exception e) {
                log.error("checkAndCreateAddresses chain={} minIdles={} idles={} slot={}", chain, minIdles, idles, slot, e);
            }
        }
    }

    @Override
    @SingletonLock(key = "/seeds/account/chain/scan-block")
    public void scanBlock() throws Exception {
        for (Chain chain : Chain.SUPPORT_LIST) {
            try {
                long l1 = System.currentTimeMillis();
                scanBlock(chain);
                long l2 = System.currentTimeMillis();
                log.info("scanBlock chain={} took={}", chain, (l2 - l1));
            } catch (Exception e) {
                log.error("scanBlock chain={}", chain, e);
            }
        }
    }

    private void scanBlock(Chain chain) throws Exception {
        boolean enabled = actionControlService.isEnabled("chain", String.format("scan_block_%s", chain.getName().toLowerCase()));
        if (!enabled) {
            return;
        }
        // 获取数据库中当前链记录的最有一个块
        ChainBlock chainBlock = chainBlockMapper.getLatestBlock(chain);
        if (chainBlock == null) {
            return;
        }

        // 查询此链支持的DEFI充提的信息
        Chain defiChain = Chain.supportedDefiChain(chain);

        // 读取这个块链上的信息用于判断链是否回滚
        NativeChainBlockDto nativeChainBlockDto = chainService.getChainNativeBlock(chain, chainBlock.getBlockNumber());
        if (nativeChainBlockDto == null) {
            log.info("rollback block as can not find chainBlock={}", chainBlock);
            // 回滚链提币
            rollbackBlock(chain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            // 回滚DEFI链提币
            if (defiChain != null) {
                rollbackBlock(defiChain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            }
            return;
        }
        Assert.isTrue(nativeChainBlockDto.getChain() != null, "missing chain info");

        if (!ObjectUtils.isAddressEquals(nativeChainBlockDto.getBlockHash(), chainBlock.getBlockHash())) {
            // block hash not identical
            log.info("rollback block as block hash changed db-block={} native-chain-block={}", chainBlock, nativeChainBlockDto);
            rollbackBlock(chain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            if (defiChain != null) {
                rollbackBlock(defiChain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            }
            return;
        }
        long nextBlockNumber = chainBlock.getBlockNumber() + 1;
        long chainBlockNumber = chainService.getLatestBlockNumber(chain);
        // not to read the highest block as it's the most rollback often
        if (nextBlockNumber >= chainBlockNumber) {
            return;
        }

        long pendingBlocks = chainBlockNumber - chainBlock.getBlockNumber();
        log.info("scanBlock chain={} defi-chain={} my-last-block-number={} chain-last-block-number={} pendingBlocks={}", chain, defiChain, chainBlock.getBlockNumber(), chainBlockNumber, pendingBlocks);

        // 使用指标记录一下
        Metrics.summary("chain-pending-blocks", Tags.of("chain", chain.getName())).record(pendingBlocks);

        // 读取链上下一个块
        NativeChainBlockDto newNativeChainBlockDto = chainService.getChainNativeBlock(chain, nextBlockNumber);
        if (newNativeChainBlockDto == null) {
            return;
        }
        log.info("scanBlock newBlockDto={}", newNativeChainBlockDto);
        if (!ObjectUtils.isAddressEquals(chainBlock.getBlockHash(), newNativeChainBlockDto.getParentHash())) {
            // new block's parent not link to the block in db
            log.info("rollback block as block parent hash changed db-block={} new-chain-block={}", chainBlock, newNativeChainBlockDto);
            rollbackBlock(chain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            if (defiChain != null) {
                rollbackBlock(defiChain, chainBlock.getBlockNumber(), chainBlock.getBlockHash());
            }
            return;
        }

        // 获取绑定用户此链上的地址 (需要兼容BSC使用了ETH的地址)
        List<ChainDepositAddress> assignedList = chainDepositAddressMapper.getAssignedAddresses(Chain.mapChain(chain).getCode());
        // 获取绑定用户的地址
        List<String> assignedAddresses = assignedList.size() == 0
                ? Lists.newArrayList()
                : assignedList.stream().map(ChainDepositAddress::getAddress).collect(Collectors.toList());

        // 获取新块的Deposit交易（链上）
        List<NativeChainTransactionDto> transactions = assignedAddresses.size() == 0
                ? Lists.newArrayList()
                : chainService.getTransactions(chain, defiChain, newNativeChainBlockDto.getBlockNumber(), assignedAddresses);

        if (transactions.size() > 0) {
            // 处理充币精度
            transactions.forEach(e -> e.setAmount(e.getAmount().setScale(DEPOSIT_DECIMALS, RoundingMode.DOWN)));
            // 过滤掉数据太小的
            // 过滤掉没有充币规则或者充币禁用的
            transactions = transactions.stream().filter(this::allowDeposit).collect(Collectors.toList());
        }

        if (transactions.size() > 0) {
            // 读取系统钱包地址
            List<SystemWalletAddressDto> systemWalletAddresses = systemWalletAddressService.getAll();
            // 从系统热钱包地址打币不能给用户上账 milo 2021-02-04
            transactions = transactions.stream()
                    .filter(e -> systemWalletAddresses.stream().noneMatch(s -> s.getType() == WalletAddressType.HOT.getCode() && ObjectUtils.isAddressEquals(e.getFromAddress(), s.getAddress())))
                    .collect(Collectors.toList());
        }

        long canConfirmBlockNumber = nextBlockNumber - chainService.getConfirmBlocks(chain);
        // 获取等待确认的Deposit（数据库）
        List<ChainDepositWithdrawHis> pendingConfirmList = Lists.newArrayList();
        pendingConfirmList.addAll(chainDepositWithdrawHisMapper.getChainDepositWithdrawEarlierThanBlockNumber(chain, ChainAction.DEPOSIT.getCode(), DepositStatus.CREATED.getCode(), canConfirmBlockNumber));
        if (defiChain != null) {
            pendingConfirmList.addAll(chainDepositWithdrawHisMapper.getChainDepositWithdrawEarlierThanBlockNumber(defiChain, ChainAction.DEPOSIT.getCode(), DepositStatus.CREATED.getCode(), canConfirmBlockNumber));
        }

        // 读取充币黑地址列表，如果没有新的deposit就没必要读取
        List<String> blacklistAddresses = pendingConfirmList.size() == 0
                ? Lists.newArrayList()
                : blacklistAddressService.getAll()
                .stream()
                .filter(e -> e.getType() == ChainAction.DEPOSIT.getCode() && e.getStatus() == CommonStatus.ENABLED)
                .map(BlacklistAddressDto::getAddress)
                .collect(Collectors.toList());

        // 黑名单的需要标记(针对fromAddress)
        List<ChainDepositWithdrawHis> toSuspendedList = blacklistAddresses.size() == 0
                ? Lists.newArrayList()
                : pendingConfirmList.stream().filter(o -> anyMatch(blacklistAddresses, o.getFromAddress())).collect(Collectors.toList());

        // 不在黑名单的可以确认(针对fromAddress)
        List<ChainDepositWithdrawHis> toConfirmList = blacklistAddresses.size() == 0
                ? pendingConfirmList
                : pendingConfirmList.stream().filter(o -> !anyMatch(blacklistAddresses, o.getFromAddress())).collect(Collectors.toList());

        // 进行数据库事务操作
        chainActionPersistentService.processNewBlock(newNativeChainBlockDto, assignedList, transactions, toSuspendedList, toConfirmList);
    }

    private boolean anyMatch(List<String> blacklistAddresses, String address) {
        return blacklistAddresses.stream().anyMatch(o -> ObjectUtils.isAddressEquals(o, address));
    }

    /**
     * 回滚块
     *
     * @param chain
     * @param blockNumber
     * @param blockHash
     */
    private void rollbackBlock(Chain chain, long blockNumber, String blockHash) {
        chainActionPersistentService.rollbackBlock(chain, blockNumber, blockHash);
    }

    /**
     * 判断是否允许充币
     *
     * @param e
     * @return
     */
    private boolean allowDeposit(NativeChainTransactionDto e) {
        if (e.getAmount().signum() > 0) {
            DepositRuleDto rule = chainDepositService.getDepositRule(e.getChain(), e.getCurrency());
            return rule != null && rule.getStatus() == CommonStatus.ENABLED;
        }
        return false;
    }
}
