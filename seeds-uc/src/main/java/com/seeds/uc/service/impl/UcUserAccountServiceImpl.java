package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.redis.LoginUserDTO;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.dto.response.UcUserAddressInfoResp;
import com.seeds.uc.enums.UcErrorCodeEnum;
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

    /**
     * 冲/提币
     *
     * @param accountActionReq
     * @return
     */
    @Override
    public void action(AccountActionReq accountActionReq) {
        Long currentUserId = UserContext.getCurrentUserId();
        // 往用户账户表、用户账户行动历史表中添加数据
        Integer action = accountActionReq.getAction();
        BigDecimal amount = accountActionReq.getAmount();
        UcUserAccountInfoResp info = this.getInfo();
        BigDecimal  balance;
        if (info == null) {
            balance = new BigDecimal(0);
        } else {
            balance = info.getBalance();
        }
        if (action == 1) {
            // 账户中加钱
            this.updateById(UcUserAccount.builder()
                            .id(currentUserId)
                            .balance(balance.add(amount))
                    .build());
        } else if (action == 2) {
            // 账户中减钱
            this.updateById(UcUserAccount.builder()
                    .id(currentUserId)
                    .balance(balance.subtract(amount))
                    .build());
        } else {
            // 报错
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS);
        }
        // 添加历史信息
        UcUserAccountActionHistory history = UcUserAccountActionHistory.builder().build();
        BeanUtil.copyProperties(accountActionReq, history);
        history.setUserId(currentUserId);
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
                .accountType(1)
                .createTime(currentTimeMillis)
                .currency("USDC")
                .freeze(new BigDecimal(0))
                .balance(new BigDecimal(0))
                .build();
        this.save(ucUserAccount);
        String randomSalt = RandomUtil.getRandomSalt();
        // 创建地址
        UcUserAddress ucUserAddress = UcUserAddress.builder()
                .address("0x40141cf4756a72df8d8f81c1e0c2" + randomSalt)
                .currency("USDC")
                .chain("")
                .comments("备注")
                .createTime(currentTimeMillis)
                .userId(userId)
                .build();
        ucUserAddressService.save(ucUserAddress);
    }
}
