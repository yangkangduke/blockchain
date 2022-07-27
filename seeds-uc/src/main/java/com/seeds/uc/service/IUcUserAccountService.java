package com.seeds.uc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.uc.dto.LoginUser;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.model.UcUserAccount;

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
     * @param accountActionReq
     * @param loginUser
     * @return
     */
    void action(AccountActionReq accountActionReq, LoginUser loginUser);

    /**
     * 充/提币历史分页
     * @param historyReq
     * @return
     */
    Page<AccountActionResp> actionHistory(Page page, AccountActionHistoryReq historyReq);
}
