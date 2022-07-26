package com.seeds.uc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.dto.LoginUser;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.mapper.UcUserAccountMapper;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.service.IUcUserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 冲/提币
     * @param accountActionReq
     * @param loginUser
     * @return
     */
    @Override
    public Void action(AccountActionReq accountActionReq, LoginUser loginUser) {
        // 往用户账户表、用户账户行动历史表中添加数据
        return null;
    }

    /**
     * 充/提币历史分页
     * @param historyReq
     * @return
     */
    @Override
    public Page<AccountActionResp> actionHistory(AccountActionHistoryReq historyReq) {
        return null;
    }
}
