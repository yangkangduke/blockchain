package com.seeds.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seeds.account.dto.WithdrawLimitRuleDto;
import com.seeds.account.dto.req.ListReq;
import com.seeds.account.dto.req.WithdrawLimitSaveOrUpdateReq;
import com.seeds.account.model.WithdrawLimitRule;

import java.util.List;

/**
 * <p>
 * 提币限额规则 服务类
 * </p>
 *
 * @author yk
 * @since 2022-10-08
 */
public interface IWithdrawLimitRuleService extends IService<WithdrawLimitRule> {

    List<WithdrawLimitRuleDto> getList();

    Boolean add(WithdrawLimitSaveOrUpdateReq req);

    Boolean update(WithdrawLimitSaveOrUpdateReq req);

    Boolean delete(ListReq req);
}
