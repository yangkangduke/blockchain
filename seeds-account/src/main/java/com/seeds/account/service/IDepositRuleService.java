package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRuleReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.model.DepositRule;
import com.seeds.account.model.SwitchReq;

import java.util.List;

/**
 * <p>
 * 充提规则 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-10
 */
public interface IDepositRuleService extends IService<DepositRule> {

    List<DepositRuleDto> getList(DepositRuleReq req);

    Boolean add(DepositRuleSaveOrUpdateReq req);

    Boolean update(DepositRuleSaveOrUpdateReq req);

    Boolean delete(SwitchReq req);
}
