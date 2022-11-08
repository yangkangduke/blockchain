package com.seeds.account.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.AddressCollectHisDto;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRulePageReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.model.DepositRule;

/**
 * <p>
 * 充提规则 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-10
 */
public interface IDepositRuleService extends IService<DepositRule> {

    IPage<DepositRuleDto> getList(DepositRulePageReq req);

    Boolean add(DepositRuleSaveOrUpdateReq req);

    Boolean update(DepositRuleSaveOrUpdateReq req);

    Boolean delete(ListReq req);
}
