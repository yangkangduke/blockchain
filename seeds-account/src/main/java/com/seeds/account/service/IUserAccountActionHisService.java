package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.enums.AccountAction;
import com.seeds.account.model.UserAccountActionHis;

import java.util.List;

/**
 * <p>
 * t_user_account_action_his 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IUserAccountActionHisService extends IService<UserAccountActionHis> {

    List<UserAccountActionHis> querySuccessByActionAndSourceAndTime(AccountAction action, Long nftId, Long startTime, Long endTime);

}
