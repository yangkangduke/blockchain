package com.seeds.account.controller;

import com.seeds.account.AccountConstants;
import com.seeds.account.service.IChainDepositService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.enums.Chain;
import com.seeds.common.web.context.UserContext;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: seeds-java
 * @description: 账户
 * @author: yk
 * @create: 2022-09-28 10:17
 **/
@RestController
@Slf4j
@RequestMapping("/account")
public class AccountController {

    @Autowired
    IChainDepositService chainDepositService;

    /**
     * 获取充币地址
     *
     * @param chain default to Chain.ETH.getCode()
     * @return
     */
    @GetMapping("/deposit-address")
    @ApiOperation("获取用户的充币地址")
    public GenericDto<String> getDepositAddress(@RequestParam(value = "chain", defaultValue = "1") int chain) {
        try {
            long userId = getUserId();
            return GenericDto.success(chainDepositService.getDepositAddress(Chain.fromCode(chain), userId, true));
        } catch (Exception e) {
            log.error("getDepositAddress", e);
            return Utils.returnFromException(e);
        }
    }

    /**
     * 从网关获取当前用户的Id做初步校验
     *
     * @return
     */
    private long getUserId() {
        long currentUserId = UserContext.getCurrentUserId();
        log.debug("currentUserId={}", currentUserId);
        Assert.isTrue(currentUserId >= AccountConstants.getClientUserId(), "invalid user");
        return currentUserId;
    }
}
