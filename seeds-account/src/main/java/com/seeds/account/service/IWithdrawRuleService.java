package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.WithdrawRuleDto;
import com.seeds.account.dto.req.WithdrawRuleReq;
import com.seeds.account.dto.req.WithdrawRuleSaveOrUpdateReq;
import com.seeds.account.model.SwitchReq;
import com.seeds.account.model.WithdrawRule;

import java.util.List;

/**
 * <p>
 * 提币规则 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IWithdrawRuleService extends IService<WithdrawRule> {
    List<WithdrawRuleDto> getList(WithdrawRuleReq req);

    Boolean add(WithdrawRuleSaveOrUpdateReq req);

    Boolean update(WithdrawRuleSaveOrUpdateReq req);

    Boolean delete(SwitchReq req);
}
