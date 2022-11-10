package com.seeds.uc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.admin.dto.response.SysNftDetailResp;
import com.seeds.common.enums.TargetSource;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.request.NFTBuyReq;
import com.seeds.uc.dto.request.NFTDeductGasFeeReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.dto.response.UcUserAccountAmountResp;
import com.seeds.uc.dto.response.UcUserAccountInfoResp;
import com.seeds.uc.enums.CurrencyEnum;
import com.seeds.uc.model.UcUserAccount;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户账户表 服务类
 * </p>
 *
 * @author yk
 * @since 2022-07-26
 */
public interface IUcUserAccountService extends IService<UcUserAccount> {

    /**
     * 冲/提币
     *
     * @param accountActionReq
     * @return
     */
    void action(AccountActionReq accountActionReq);

    /**
     * 充/提币历史分页
     *
     * @param historyReq
     * @return
     */
    IPage<AccountActionResp> actionHistory(Page page, AccountActionHistoryReq historyReq);

    /**
     * 账户详情
     * @param UserId 用户id
     * @return
     */
    UcUserAccountInfoResp getInfo(Long UserId);

    /**
     * 账户详情
     * @param userId 用户id
     * @return
     */
    UcUserAccountInfoResp getInfoByUserId(Long userId);

    /**
     * 根据用户id创建账户
     * @param userId
     */
    void creatAccountByUserId(Long userId);

    /**
     *  检查账户里面的金额是否足够支付
     * @param currentUserId
     * @param amount
     * @param currency
     * @return
     */
    Boolean checkBalance(Long currentUserId, BigDecimal amount, CurrencyEnum currency);

    /**
     * 购买nft
     * @param buyReq
     * @param sysNftDetail NFT详情
     */
    void buyNFT(NFTBuyReq buyReq, SysNftDetailResp sysNftDetail);

    /**
     * 购买nft
     * @param buyReq
     */
    void buyNFTFreeze(SysNftDetailResp buyReq, TargetSource source, Long currentUserId);

    /**
     * 账户金额详情
     * @param userId 用户id
     * @return 账户金额
     */
    List<UcUserAccountAmountResp> amountInfo(Long userId);

    /**
     * 手续费扣除
     * @param req
     */
    void deductGasFee(NFTDeductGasFeeReq req);

}
