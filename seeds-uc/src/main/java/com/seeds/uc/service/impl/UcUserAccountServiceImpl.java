package com.seeds.uc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.uc.dto.LoginUserDTO;
import com.seeds.uc.dto.request.AccountActionHistoryReq;
import com.seeds.uc.dto.request.AccountActionReq;
import com.seeds.uc.dto.response.AccountActionResp;
import com.seeds.uc.mapper.UcUserAccountMapper;
import com.seeds.uc.model.UcUserAccount;
import com.seeds.uc.model.UcUserAccountActionHistory;
import com.seeds.uc.service.IUcUserAccountActionHistoryService;
import com.seeds.uc.service.IUcUserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IUcUserAccountActionHistoryService ucUserAccountActionHistoryService;

    /**
     * 冲/提币
     *
     * @param accountActionReq
     * @param loginUser
     * @return
     */
    @Override
    public void action(AccountActionReq accountActionReq, LoginUserDTO loginUser) {
        // 往用户账户表、用户账户行动历史表中添加数据 todo 等确认前端传的参数后调整
        UcUserAccountActionHistory history = UcUserAccountActionHistory.builder().build();
        BeanUtil.copyProperties(accountActionReq, history);
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
}
