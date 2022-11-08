package com.seeds.admin.controller;

import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.admin.annotation.MgtAuthority;
import com.seeds.admin.dto.MgtBlacklistAddressDto;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.service.MgtRiskService;
import com.seeds.common.dto.GenericDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/risk")
@Api(tags = "黑地址管理")
public class SysRiskController {

    @Autowired
    MgtRiskService mgtRiskService;

    @GetMapping("/deposit/blacklist/get")
    @ApiOperation("获取充币黑名单列表")
    // @MgtAuthority(path = "/monitor/manual-action/deposit-disabled/blacklist/")
    public GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getDepositBlackList(@RequestParam(value = "reason",
            required = false) String reason) {
        return mgtRiskService.getBlackList(1, reason);
    }

    @PostMapping("/deposit/blacklist/add")
    @ApiOperation("新增充币黑名单")
    // @MgtAuthority(path = "/monitor/manual-action/deposit-disabled/blacklist/:add")
    public GenericDto<Boolean> addBlackList(@RequestBody @Valid MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setType(1);
        return mgtRiskService.addDepositBlackList(blacklistAddressDto);
    }

    @PostMapping("/deposit/blacklist/update")
    @ApiOperation("编辑充币黑名单")
    // @MgtAuthority(path = "/monitor/manual-action/deposit-disabled/blacklist/:edit")
    public GenericDto<Boolean> updateBlackList(@RequestBody @Valid MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setType(1);
        return mgtRiskService.updateDepositBlackList(blacklistAddressDto);
    }

    @PostMapping("/deposit/blacklist/delete")
    @ApiOperation("删除充币黑名单")
    // @MgtAuthority(path = "/monitor/manual-action/deposit-disabled/blacklist/:delete")
    public GenericDto<Boolean> deleteBlackList(@RequestBody @Valid MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setType(1);
        return mgtRiskService.deleteDepositBlackList(blacklistAddressDto);
    }

    @GetMapping("/withdraw/blacklist/get")
    @ApiOperation("获取提币黑名单列表")
    @MgtAuthority(path = "/monitor/manual-action/withdraw-disabled/blacklist/")
    public GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getWithdrawBlackList(@RequestParam(value = "reason",
            required = false) String reason) {
        return mgtRiskService.getBlackList(2, reason);
    }

    @PostMapping("/withdraw/blacklist/add")
    @ApiOperation("新增提币黑名单")
    @MgtAuthority(path = "/monitor/manual-action/withdraw-disabled/blacklist/:add")
    public GenericDto<Boolean> addWithdrawBlackList(@RequestBody @Valid MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setType(2);
        return mgtRiskService.addWithdrawBlackList(blacklistAddressDto);
    }

    @PostMapping("/withdraw/blacklist/update")
    @ApiOperation("修改提币黑名单")
    @MgtAuthority(path = "/monitor/manual-action/withdraw-disabled/blacklist/:edit")
    public GenericDto<Boolean> updateWithdrawBlackList(@RequestBody @Valid MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setType(2);
        return mgtRiskService.updateWithdrawBlackList(blacklistAddressDto);
    }

    @PostMapping("/withdraw/blacklist/delete")
    @ApiOperation("删除提币黑名单")
    @MgtAuthority(path = "/monitor/manual-action/withdraw-disabled/blacklist/:delete")
    public GenericDto<Boolean> deleteWithdrawBlackList(@RequestBody @Valid MgtBlacklistAddressDto blacklistAddressDto) {
        blacklistAddressDto.setType(2);
        return mgtRiskService.deleteWithdrawBlackList(blacklistAddressDto);
    }
}
