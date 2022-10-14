package com.seeds.account.controller;

import com.seeds.account.service.IAddressCollectService;
import com.seeds.account.service.IChainActionService;
import com.seeds.account.util.Utils;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账户系统提供的内部调用接口，调用方包括
 * 1. seeds-account-job-service
 *
 * @author yk
 */
@RestController
@Slf4j
@Api(tags = "账户-内部调用")
@RequestMapping("/account-internal")
public class AccountInternalController {

    @Autowired
    private IChainActionService chainActionService;
    @Autowired
    IAddressCollectService addressCollectService;

    @PostMapping("/job/scan-and-create-addresses")
    @ApiOperation("扫描并创建空闲地址")
    public GenericDto<Boolean> scanAndCreateAddresses() {
        try {
            chainActionService.scanAndCreateAddresses();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanAndCreateAddresses", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/job/scan-block")
    @ApiOperation("扫描新块")
    public GenericDto<Boolean> scanBlock() {
        try {
            chainActionService.scanBlock();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanBlock", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/job/execute-withdraw")
    @ApiOperation("执行提币上链")
    public GenericDto<Boolean> executeWithdraw() {
        try {
            chainActionService.executeWithdraw();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("executeWithdraw", e);
            return Utils.returnFromException(e);
        }
    }

    @PostMapping("/job/scan-withdraw")
    @ApiOperation("扫描提币，归集，空投状态")
    public GenericDto<Boolean> scanWithdraw() {
        try {
            // 处理提币
            chainActionService.scanWithdraw();
            // 处理归集
            addressCollectService.scanCollect();
            return GenericDto.success(true);
        } catch (Exception e) {
            log.error("scanWithdraw", e);
            return Utils.returnFromException(e);
        }
    }
}
