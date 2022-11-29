package com.seeds.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.enums.CommonActionStatus;
import com.seeds.account.mapper.UserAccountActionHisMapper;
import com.seeds.account.model.UserAccountActionHis;
import com.seeds.account.service.IUserAccountActionHisService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * t_user_account_action_his 服务实现类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
@Service
public class UserAccountActionHisServiceImpl extends ServiceImpl<UserAccountActionHisMapper, UserAccountActionHis> implements IUserAccountActionHisService {

    @Override
    public List<UserAccountActionHis> querySuccessByActionAndSourceAndTime(AccountAction action, Long nftId, Long startTime, Long endTime) {
        LambdaQueryWrapper<UserAccountActionHis> wrapper = new QueryWrapper<UserAccountActionHis>().lambda()
                .eq(UserAccountActionHis::getSource, nftId)
                .eq(UserAccountActionHis::getStatus, CommonActionStatus.SUCCESS)
                .eq(UserAccountActionHis::getAction, AccountAction.NFT_TRADE)
                .between(UserAccountActionHis::getUpdateTime, startTime, endTime);
        return list(wrapper);
    }
}
