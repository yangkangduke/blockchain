package com.seeds.game.controller;

import com.seeds.common.dto.GenericDto;
import com.seeds.uc.dto.response.UcUserAccountAmountResp;
import com.seeds.uc.feign.RemoteUserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提供外部调用的账户相关接口
 * @author hang.yu
 * @date 2022/10/11
 */
@Slf4j
@Api(tags = "账户外部调用授权")
@RestController
@RequestMapping("/account")
public class OpenAccountController {

    @Autowired
    private RemoteUserAccountService remoteUserAccountService;

    @GetMapping("/amount-info")
    @ApiOperation(value = "账户金额详情", notes = "账户金额详情")
    public GenericDto<List<UcUserAccountAmountResp>> amountInfo(@RequestParam String accessKey,
                                                                @RequestParam String signature,
                                                                @RequestParam Long timestamp) {
        return remoteUserAccountService.amountInfo();
    }

}
