package com.seeds.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seeds.account.dto.ChainTxnDto;
import com.seeds.account.dto.ChainTxnReplaceDto;
import com.seeds.account.dto.req.ChainTxnPageReq;
import com.seeds.admin.annotation.RequiredPermission;
import com.seeds.admin.dto.MgtChainTxnDto;
import com.seeds.admin.dto.MgtChainTxnReplaceDto;
import com.seeds.admin.dto.MgtChainTxnReplayDto;
import com.seeds.admin.dto.MgtOriginOrderReplaceDto;
import com.seeds.admin.service.ChainReplaceService;
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
    private ChainReplaceService chainReplaceService;

    @PostMapping("/txn-replay")
    @ApiOperation("执行重发功能")
    @RequiredPermission("sys:chainTxn:replay")
    public GenericDto<Boolean> executeReplay(@RequestBody MgtChainTxnDto dto) {
        MgtChainTxnReplayDto chainTxnReplayDto = MgtChainTxnReplayDto.builder()
                .id(dto.getId())
                .type(dto.getType())
                .comment("重发")
                .txHash(dto.getTxHash())
                .build();
        switch (dto.getType()) {
            case 1:
                return chainReplaceService.executeWithdraw(chainTxnReplayDto);
            case 2:
                return chainReplaceService.executeWalletTransfer(chainTxnReplayDto);
            case 3:
                return chainReplaceService.executeCreateOrder(chainTxnReplayDto);
            case 4:
                return chainReplaceService.executeCreateGasFeeOrder(chainTxnReplayDto);
            default:
                log.info("no type match with {}", dto.getType());
                return GenericDto.success(true);
        }
    }

    @PostMapping("/txn-origin-page")
    @ApiOperation("获取原始交易分页列表")
    @RequiredPermission("sys:chainTxn:originPage")
    public GenericDto<Page<ChainTxnDto>> queryOriginOrder(@RequestBody @Valid ChainTxnPageReq req) {
        return chainReplaceService.getChainTxnList(req);
    }

    @PostMapping("/txn-replace")
    @ApiOperation("执行替换功能")
    @RequiredPermission("sys:chainTxn:replace")
    public GenericDto<Boolean> executeReplace(@RequestBody @Valid ChainTxnReplaceDto dto) {
        chainReplaceService.recordOriginReplaceOrder(MgtOriginOrderReplaceDto.builder()
                .gasLimit(dto.getGasLimit())
                .gasPrice(dto.getGasPrice())
                .txHash(dto.getTxHash())
                .type(dto.getType())
                .chain(dto.getChain())
                .build());

        return chainReplaceService.executeChainReplacement(MgtChainTxnReplaceDto.builder()
                .gasLimit(dto.getGasLimit())
                .gasPrice(dto.getGasPrice())
                .txHash(dto.getTxHash())
                .type(dto.getType())
                .chain(dto.getChain())
                .build());
    }

    @PostMapping("/txn-pend-order")
    @ApiOperation("获取进行中的进行中的交易列表")
    @RequiredPermission("sys:chainTxn:pendPage")
    public GenericDto<Page<ChainTxnDto>> queryPendingOrder(@RequestBody @Valid ChainTxnPageReq req) {
        return chainReplaceService.getChainTxnReplaceList(req);
    }

}
