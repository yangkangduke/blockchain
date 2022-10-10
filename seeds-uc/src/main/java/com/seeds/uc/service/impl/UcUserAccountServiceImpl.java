package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.enums.RequestSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.dto.response.UcUserAddressInfoResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserAccountMapper;
import com.seeds.uc.model.*;
import com.seeds.uc.service.*;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private IUcUserService ucUserService;

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
            this.update(UcUserAccount.builder()
                        .balance(balance.add(amount))
                    .build(), new LambdaQueryWrapper<UcUserAccount>()
                    .eq(UcUserAccount::getUserId, currentUserId)
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));
        } else if (action.equals(AccountActionEnum.WITHDRAW)) {
            if (!address.equals(fromAddress)) {
                throw new GenericException(UcErrorCodeEnum.ERR_18003_ACCOUNT_ADDRESS_ERROR);
            }
            // 账户中减钱, 先判断账户中剩余的钱够不够减
            if (balance.compareTo(amount) == -1) {
                throw new GenericException(UcErrorCodeEnum.ERR_18001_ACCOUNT_BALANCE_INSUFFICIENT);
            }
            this.update(UcUserAccount.builder()
                    .balance(balance.subtract(amount))
                    .build(), new LambdaQueryWrapper<UcUserAccount>()
                    .eq(UcUserAccount::getUserId, currentUserId)
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDC));
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
    public IPage<AccountActionResp> actionHistory(Page page, AccountActionHistoryReq historyReq) {
        Page<AccountActionResp> respPage = baseMapper.actionHistory(page, historyReq);
       return respPage.convert(p -> {
            AccountActionResp resp = new AccountActionResp();
            BeanUtils.copyProperties(p, resp);
            resp.setAmount(new BigDecimal(p.getAmount()).toPlainString());
            resp.setFee(new BigDecimal(p.getFee()).toPlainString());
            return resp;
        });

    }

    @Override
    public UcUserAccountInfoResp getInfo() {
        return getInfoByUserId(UserContext.getCurrentUserId());
    }

    @Override
    public UcUserAccountInfoResp getInfoByUserId(Long userId) {
        UcUserAccountInfoResp info = null;
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
     *
     * @param nftDetail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyNFTFreeze(SysNftDetailResp nftDetail, RequestSource source) {
        Long nftId = nftDetail.getId();
        long currentTimeMillis = System.currentTimeMillis();
        BigDecimal amount = nftDetail.getPrice();
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
        UcUser buyer = ucUserService.getById(currentUserId);
        if (null != buyer) {
            // 买家名字、地址
            nftOwnerChangeReq.setOwnerName(buyer.getNickname());
            nftOwnerChangeReq.setToAddress(buyer.getPublicAddress());
        }
        nftOwnerChangeReq.setSource(source);
        nftOwnerChangeReq.setOwnerId(currentUserId);
        nftOwnerChangeReq.setId(nftId);
        nftOwnerChangeReq.setActionHistoryId(actionHistoryId);
        nftOwnerChangeReq.setOwnerType(nftDetail.getOwnerType());
        if (nftDetail.getOwnerType() == 1) {
            // 卖家地址
            UcUser saler = ucUserService.getById(nftDetail.getOwnerId());
            nftOwnerChangeReq.setFromAddress(saler.getPublicAddress());
        }
        list.add(nftOwnerChangeReq);
        remoteNftService.ownerChange(list);
    }

}
