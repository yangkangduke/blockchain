package com.seeds.admin.controller;

import com.seeds.admin.service.SysKolService;
import com.seeds.common.dto.GenericDto;
import com.seeds.common.web.inner.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * KOL管理内部调用
 * @author hang.yu
 * @date 2023/4/26
 */
@Slf4j
@Api(tags = "KOL管理内部调用")
@RestController
@RequestMapping("/internal-kol")
public class InterKolController {

    @Autowired
    private SysKolService sysKolService;

    @GetMapping("invite-code")
    @ApiOperation("获取邀请码")
    @Inner
    public GenericDto<String> inviteCode(@RequestParam String inviteNo) {
        return GenericDto.success(sysKolService.inviteCode(inviteNo));
    }

}
