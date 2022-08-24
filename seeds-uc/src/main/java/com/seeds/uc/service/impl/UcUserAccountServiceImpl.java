package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.request.NFTBuyCallbackReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.dto.response.UcUserAddressInfoResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserAccountMapper;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.model.UcUserAccountActionHistory;
import com.seeds.uc.model.UcUserAddress;
import com.seeds.uc.service.IUcUserAccountActionHistoryService;
import com.seeds.uc.service.IUcUserAccountService;
import com.seeds.uc.service.IUcUserAddressService;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户账户表 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
@Service
@Slf4j
@Transactional
public class UcUserAccountServiceImpl extends ServiceImpl<UcUserAccountMapper, UcUserAccount> implements IUcUserAccountService {

    @Autowired
    private IUcUserAccountActionHistoryService ucUserAccountActionHistoryService;
    @Autowired
    private IUcUserAddressService ucUserAddressService;
    @Autowired
    private RemoteNftService remoteNftService;

    /**
     * 冲/提币
     *
     * @param accountActionReq
     * @return
     */
    @Override
    public void action(AccountActionReq accountActionReq) {
        // todo 后面改成需要冻结金额
        Long currentUserId = UserContext.getCurrentUserId();
        long currentTimeMillis = System.currentTimeMillis();
        AccountActionEnum action = accountActionReq.getAction();
        String fromAddress = accountActionReq.getFromAddress();
        String toAddress = accountActionReq.getToAddress();
        BigDecimal amount = accountActionReq.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) != 1) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_AMOUNT_ERROR);
        }
        UcUserAccountInfoResp info = this.getInfo();
        if (info == null) {
            throw new GenericException(UcErrorCodeEnum.ERR_18002_ACCOUNT_NOT);
        }

        String address = info.getUserAddressResp().getAddress();
        BigDecimal  balance = info.getBalance();
        if (action.equals(AccountActionEnum.DEPOSIT)) {
            // 账户中加钱，判断账户地址是否正确
            if (!address.equals(toAddress)) {
                throw new GenericException(UcErrorCodeEnum.ERR_18003_ACCOUNT_ADDRESS_ERROR);
            }
            this.updateById(UcUserAccount.builder()
                            .userId(currentUserId)
                            .currency(CurrencyEnum.USDC)
                            .balance(balance.add(amount))
                    .build());
        } else if (action.equals(AccountActionEnum.WITHDRAW)) {
            if (!address.equals(fromAddress)) {
                throw new GenericException(UcErrorCodeEnum.ERR_18003_ACCOUNT_ADDRESS_ERROR);
            }
            // 账户中减钱, 先判断账户中剩余的钱够不够减
            if (balance.compareTo(amount) == -1) {
                throw new GenericException(UcErrorCodeEnum.ERR_18001_ACCOUNT_BALANCE_INSUFFICIENT);
            }
            this.updateById(UcUserAccount.builder()
                    .userId(currentUserId)
                    .currency(CurrencyEnum.USDC)
                    .balance(balance.subtract(amount))
                    .build());
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS);
        }
        // 添加记录信息
        UcUserAccountActionHistory history = UcUserAccountActionHistory.builder().build();
        BeanUtil.copyProperties(accountActionReq, history);
        history.setUserId(currentUserId);
        history.setCreateTime(currentTimeMillis);
        history.setAccountType(AccountTypeEnum.ACTUALS);
        history.setCurrency(CurrencyEnum.USDC);
        ucUserAccountActionHistoryService.save(history);
    }

    /**
     * 充/提币历史分页
     *
     * @param historyReq
     * @return
     */
    @Override
    public Page<AccountActionResp> actionHistory(Page page, AccountActionHistoryReq historyReq) {
        return baseMapper.actionHistory(page, historyReq);
    }

    @Override
    public UcUserAccountInfoResp getInfo() {
        UcUserAccountInfoResp info = null;
        Long userId = UserContext.getCurrentUserId();
        UcUserAccount ucUserAccount = this.getOne(new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, userId));
        UcUserAddress ucUserAddress = ucUserAddressService.getOne(new LambdaQueryWrapper<UcUserAddress>()
                .eq(UcUserAddress::getUserId, userId));
        if (ucUserAccount != null) {
            info = UcUserAccountInfoResp.builder().build();
            BeanUtil.copyProperties(ucUserAccount,info);
        }
        if (ucUserAddress != null) {
            UcUserAddressInfoResp ucUserAddressInfoResp = UcUserAddressInfoResp.builder().build();
            BeanUtil.copyProperties(ucUserAddress,ucUserAddressInfoResp);
            info.setUserAddressResp(ucUserAddressInfoResp);
        }
        return info;
    }

    @Override
    public void creatAccountByUserId(Long userId) {
        long currentTimeMillis = System.currentTimeMillis();
        // 创建账户
        UcUserAccount ucUserAccount = UcUserAccount.builder()
                .userId(userId)
                .accountType(AccountTypeEnum.ACTUALS)
                .createTime(currentTimeMillis)
                .currency(CurrencyEnum.USDC)
                .freeze(new BigDecimal(0))
                .balance(new BigDecimal(0))
                .build();
        this.save(ucUserAccount);
        String randomSalt = RandomUtil.getRandomSalt();
        // 创建地址
        UcUserAddress ucUserAddress = UcUserAddress.builder()
                .address("0x40141cf4756a72df8d8f81c1e0c2" + randomSalt)
                .currency(CurrencyEnum.USDC)
                .createTime(currentTimeMillis)
                .userId(userId)
                .build();
        ucUserAddressService.save(ucUserAddress);
    }

    /**
     * 检查账户里面的金额是否足够支付
     * @param currentUserId
     * @param amount
     * @return
     */
    @Override
    public Boolean checkBalance(Long currentUserId, BigDecimal amount) {
        UcUserAccount one = this.getOne(new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, currentUserId));
        if (one == null || one.getBalance().compareTo(new BigDecimal(0))  != 1 || one.getBalance().compareTo(amount)  != 1) {
            return false;
        }
        return true;
    }

    /**
     * 购买nft
     * @param buyReq
     */
    @Override
    public void buyNFTFreeze(SysNftDetailResp buyReq) {
        Long nftId = buyReq.getId();
        long currentTimeMillis = System.currentTimeMillis();
        BigDecimal amount = buyReq.getPrice();
        Long currentUserId = UserContext.getCurrentUserId();
        // todo 远程调用钱包接口
        // 冻结金额
        UcUserAccountInfoResp info = this.getInfo();
        this.update(UcUserAccount.builder()
                    .balance(info.getBalance().subtract(amount))
                    .freeze(info.getFreeze().add(amount))
                .build(), new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, currentUserId)
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));

        // 添加记录信息
        UcUserAccountActionHistory ucUserAccountActionHistory = UcUserAccountActionHistory.builder()
                .userId(currentUserId)
                .createTime(currentTimeMillis)
                .actionEnum(AccountActionEnum.BUY_NFT)
                .accountType(AccountTypeEnum.ACTUALS)
                .currency(CurrencyEnum.USDC)
                .status(AccountActionStatusEnum.PROCESSING)
                .build();
        ucUserAccountActionHistoryService.save(ucUserAccountActionHistory);
        Long actionHistoryId = ucUserAccountActionHistory.getId();

        // 远程调用admin端归属人变更接口
        List list = new ArrayList<NftOwnerChangeReq>();
        NftOwnerChangeReq nftOwnerChangeReq = new NftOwnerChangeReq();
        nftOwnerChangeReq.setOwnerId(currentUserId);
        nftOwnerChangeReq.setId(nftId);
        nftOwnerChangeReq.setActionHistoryId(actionHistoryId);
        remoteNftService.ownerChange(list);
    }

    /**
     * 购买回调
     * @param buyReq
     */
    @Override
    public void buyNFTCallback(NFTBuyCallbackReq buyReq) {
        BigDecimal amount = buyReq.getAmount();
        // todo 如果是admin端mint的nft，在uc端不用记账该账户到uc_user_account，都是uc端的用户则需要记账
        // 买家减少 卖家增加
        UcUserAccountInfoResp info = this.getInfo();
        this.update(UcUserAccount.builder()
                .freeze(info.getFreeze().subtract(amount))
                .build(), new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, buyReq.getToUserId())
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));

        this.update(UcUserAccount.builder()
                .balance(info.getBalance().add(amount))
                .build(), new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, buyReq.getFromUserId())
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));

        // 改变交易记录的状态及其他信息
        ucUserAccountActionHistoryService.updateById(UcUserAccountActionHistory.builder()
                        .status(buyReq.getActionStatusEnum())
                        .fromAddress(buyReq.getFromAddress())
                        .toAddress(buyReq.getToAddress())
                        .chain(buyReq.getChain())
                        .txHash(buyReq.getTxHash())
                        .blockNumber(buyReq.getBlockNumber())
                        .blockHash(buyReq.getBlockHash())
                        .id(buyReq.getActionHistoryId())
                .build());
    }
}
