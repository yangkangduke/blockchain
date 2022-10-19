package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.admin.dto.request.NftOwnerChangeReq;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.admin.enums.WhetherEnum;
import com.seeds.admin.feign.RemoteNftService;
import com.seeds.common.constant.mq.KafkaTopic;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.RequestSource;
import com.seeds.common.web.context.UserContext;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.dto.response.UcUserAccountAmountResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.dto.response.UcUserAddressInfoResp;
import com.seeds.uc.enums.*;
import com.seeds.uc.exceptions.GenericException;
import com.seeds.uc.exceptions.InvalidArgumentsException;
import com.seeds.uc.mapper.UcUserAccountMapper;
import com.seeds.uc.model.*;
import com.seeds.uc.mq.producer.KafkaProducer;
import com.seeds.uc.service.*;
import com.seeds.uc.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private KafkaProducer kafkaProducer;

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
        UcUserAccountInfoResp info = this.getInfo(currentUserId);
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
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT));
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
                    .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT));
        } else {
            throw new InvalidArgumentsException(UcErrorCodeEnum.ERR_502_ILLEGAL_ARGUMENTS);
        }
        // 添加记录信息
        UcUserAccountActionHistory history = UcUserAccountActionHistory.builder().build();
        BeanUtil.copyProperties(accountActionReq, history);
        history.setUserId(currentUserId);
        history.setCreateTime(currentTimeMillis);
        history.setAccountType(AccountTypeEnum.ACTUALS);
        history.setCurrency(CurrencyEnum.USDT);
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
    public UcUserAccountInfoResp getInfo(Long userId) {
        return getInfoByUserId(userId);
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
                .currency(CurrencyEnum.USDT)
                .freeze(new BigDecimal(0))
                .balance(new BigDecimal(0))
                .build();
        this.save(ucUserAccount);
        String randomSalt = RandomUtil.getRandomSalt();
        // 创建地址
        UcUserAddress ucUserAddress = UcUserAddress.builder()
                .address("0x40141cf4756a72df8d8f81c1e0c2" + randomSalt)
                .currency(CurrencyEnum.USDT)
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
    public Boolean checkBalance(Long currentUserId, BigDecimal amount, CurrencyEnum currency) {
        UcUserAccount one = this.getOne(new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, currentUserId)
                .eq(UcUserAccount::getCurrency, currency));
        if (one == null || one.getBalance().compareTo(new BigDecimal(0))  != 1 || one.getBalance().compareTo(amount)  != 1) {
            return false;
        }
        return true;
    }

    @Override
    public void buyNFT(NFTBuyReq buyReq, SysNftDetailResp sysNftDetailResp) {
        Long currentUserId = buyReq.getUserId();
        if (currentUserId == null) {
            currentUserId = UserContext.getCurrentUserId();
        }
        BigDecimal price = sysNftDetailResp.getPrice();

        //  判断nft是否是上架状态、nft是否已经购买过了
        if (!Objects.isNull(sysNftDetailResp)) {
            if (sysNftDetailResp.getStatus() != WhetherEnum.YES.value()) {
                throw new GenericException(UcErrorCodeEnum.ERR_18006_ACCOUNT_BUY_FAIL_INVALID_NFT_STATUS);
            }
            // 判断NFT是否已锁定
            if (WhetherEnum.YES.value() == sysNftDetailResp.getLockFlag()) {
                throw new GenericException(UcErrorCodeEnum.ERR_18007_ACCOUNT_BUY_FAIL_NFT_LOCKED);
            }
            // 买家是否是归属人
            if (Objects.equals(sysNftDetailResp.getOwnerId(), currentUserId)) {
                throw new GenericException(UcErrorCodeEnum.ERR_18008_YOU_ALREADY_OWN_THIS_NFT);
            }
        }


        // 检查账户里面的金额是否足够支付
        if (!checkBalance(currentUserId, price, CurrencyEnum.USDT)) {
            throw new GenericException(UcErrorCodeEnum.ERR_18004_ACCOUNT_BALANCE_INSUFFICIENT);
        }
        buyNFTFreeze(sysNftDetailResp, buyReq.getSource(), currentUserId);
    }

    /**
     * 购买nft
     *
     * @param nftDetail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyNFTFreeze(SysNftDetailResp nftDetail, RequestSource source, Long currentUserId) {
        Long nftId = nftDetail.getId();
        long currentTimeMillis = System.currentTimeMillis();
        BigDecimal amount = nftDetail.getPrice();
        if (currentUserId == null) {
            currentUserId = UserContext.getCurrentUserId();
        }
        // todo 远程调用钱包接口
        // 冻结金额
        LambdaQueryWrapper<UcUserAccount> wrapper = new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, currentUserId)
                .eq(UcUserAccount::getCurrency, CurrencyEnum.USDT);
        UcUserAccount account = getOne(wrapper);
        account.setBalance(account.getBalance().subtract(amount));
        account.setFreeze(account.getFreeze().add(amount));
        updateById(account);

        // 添加记录信息
        UcUserAccountActionHistory ucUserAccountActionHistory = UcUserAccountActionHistory.builder()
                .userId(currentUserId)
                .createTime(currentTimeMillis)
                .actionEnum(AccountActionEnum.BUY_NFT)
                .accountType(AccountTypeEnum.ACTUALS)
                .currency(CurrencyEnum.USDT)
                .status(AccountActionStatusEnum.PROCESSING)
                .amount(amount)
                .build();
        ucUserAccountActionHistoryService.save(ucUserAccountActionHistory);
        Long actionHistoryId = ucUserAccountActionHistory.getId();

        // 远程调用admin端归属人变更接口
        List<NftOwnerChangeReq> list = new ArrayList<>();
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
        nftOwnerChangeReq.setAmount(amount);
        if (nftDetail.getOwnerType() == 1) {
            // 卖家地址
            UcUser saler = ucUserService.getById(nftDetail.getOwnerId());
            nftOwnerChangeReq.setFromAddress(saler.getPublicAddress());
        }
        list.add(nftOwnerChangeReq);
        kafkaProducer.send(KafkaTopic.UC_NFT_OWNER_CHANGE, JSONUtil.toJsonStr(list));
    }

    @Override
    public List<UcUserAccountAmountResp> amountInfo(Long userId) {
        List<UcUserAccountAmountResp> resList = new ArrayList<>();
        List<UcUserAccount> list = this.list(new LambdaQueryWrapper<UcUserAccount>()
                .eq(UcUserAccount::getUserId, userId));
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(p -> {
                UcUserAccountAmountResp info = UcUserAccountAmountResp.builder().build();
                BeanUtil.copyProperties(p, info);
                resList.add(info);
            });
        }
        return resList;
    }

}
