package com.seeds.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.DepositRuleDto;
import com.seeds.account.dto.req.DepositRulePageReq;
import com.seeds.account.dto.req.DepositRuleSaveOrUpdateReq;
import com.seeds.account.dto.req.ListReq;
import com.seeds.common.dto.GenericDto;

/**
 * @author: hewei
 * @date 2022/11/8
 */
public interface ISysDepositWithdrawConfigService {
    GenericDto<Boolean> add(DepositRuleSaveOrUpdateReq req);

    GenericDto<Page<DepositRuleDto>> getList(DepositRulePageReq req);

    GenericDto<Boolean> update(DepositRuleSaveOrUpdateReq req);

    GenericDto<Boolean> delete(ListReq req);
}
