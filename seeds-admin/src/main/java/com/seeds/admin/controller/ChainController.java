package com.seeds.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplayDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.account.feign.AccountFeignClient;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 链相关管理
 * @author hang.yu
 * @date 2022/10/25
 */
@Slf4j
@Api(tags = "链相关管理")
@RestController
@RequestMapping("/chain")
public class ChainController {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @PostMapping("/txn-replay")
    @ApiOperation("执行重发功能")
    @RequiredPermission("sys:chainTxn:replay")
    public GenericDto<Boolean> executeReplay(@RequestBody ChainTxnReplayDto dto) {
        switch (dto.getType()) {
            case 1:
                return accountFeignClient.executeChainReplay(dto);
            default:
                log.info("no type match with {}", dto.getType());
                return GenericDto.success(true);
        }
    }

    @PostMapping("/txn-page")
    @ApiOperation("获取原始交易分页列表")
    @RequiredPermission("sys:chainTxn:page")
    public GenericDto<Page<ChainTxnDto>> executeReplay(@RequestBody @Valid ChainTxnPageReq req) {
        return accountFeignClient.getChainTxnList(req);
    }

}
