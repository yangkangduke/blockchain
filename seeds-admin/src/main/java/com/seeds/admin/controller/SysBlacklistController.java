package com.seeds.admin.controller;
import com.seeds.account.dto.BlacklistAddressDto;
import com.seeds.account.dto.req.BlackListAddressSaveOrUpdateReq;
import com.seeds.account.enums.ChainAction;
import com.seeds.account.model.SwitchReq;
import com.seeds.admin.dto.MgtPageDto;
import com.seeds.admin.service.ISysRiskService;
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
@RequestMapping("/blacklist")
@Api(tags = "黑地址管理")
public class SysBlacklistController {

    @Autowired
    ISysRiskService ISysRiskService;

    @GetMapping("/deposit/get")
    @ApiOperation("获取充币黑名单列表")
    public GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getDepositBlackList(@RequestParam(value = "reason",
            required = false) String reason) {
        return ISysRiskService.getBlackList(ChainAction.DEPOSIT.getCode(), reason);
    }

    @PostMapping("/deposit/add")
    @ApiOperation("新增充币黑名单")
    public GenericDto<Boolean> addBlackList(@RequestBody  BlackListAddressSaveOrUpdateReq req) {
        req.setType(ChainAction.DEPOSIT.getCode());
        return ISysRiskService.addDepositBlackList(req);
    }

    @PostMapping("/deposit/update")
    @ApiOperation("更新充币黑名单")
    public GenericDto<Boolean> updateBlackList(@RequestBody  BlackListAddressSaveOrUpdateReq req) {
        req.setType(ChainAction.DEPOSIT.getCode());
        return ISysRiskService.updateDepositBlackList(req);
    }

    @GetMapping("/withdraw/get")
    @ApiOperation("获取提币黑名单列表")
    public GenericDto<MgtPageDto<List<BlacklistAddressDto>>> getWithdrawBlackList(@RequestParam(value = "reason",
            required = false) String reason) {
        return ISysRiskService.getBlackList(ChainAction.WITHDRAW.getCode(), reason);
    }

    @PostMapping("/withdraw/add")
    @ApiOperation("新增提币黑名单")
    public GenericDto<Boolean> addWithdrawBlackList(@RequestBody BlackListAddressSaveOrUpdateReq req) {
        req.setType(ChainAction.WITHDRAW.getCode());
        return ISysRiskService.addWithdrawBlackList(req);
    }

    @PostMapping("/withdraw/update")
    @ApiOperation("修改提币黑名单")
    public GenericDto<Boolean> updateWithdrawBlackList(@RequestBody  BlackListAddressSaveOrUpdateReq req) {
        req.setType(ChainAction.WITHDRAW.getCode());
        return ISysRiskService.updateWithdrawBlackList(req);
    }

    @PostMapping("/switch")
    @ApiOperation("启用/停用黑名单")
    public GenericDto<Boolean> deleteBlackList(@RequestBody @Valid SwitchReq req) {
        return ISysRiskService.deleteBlackList(req);
    }
}
